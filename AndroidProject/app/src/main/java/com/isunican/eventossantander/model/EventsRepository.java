package com.isunican.eventossantander.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.isunican.eventossantander.model.network.EventsAPI;
import com.isunican.eventossantander.model.network.EventsAPIResponse;
import com.isunican.eventossantander.model.network.EventsAPIService;
import com.isunican.eventossantander.view.Listener;

import java.util.List;
import java.util.concurrent.Phaser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsRepository {
    private EventsRepository() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * The data source URL can be modified. This is useful for tests.
     */
    private static EventsAPIService.Source source = EventsAPIService.Source.AYTO;


    /*
    Problem: in some UI Tests, the tests fails because Espresso does not wait until the data
    is downloaded.
    A simple way to solve this is with a sleep in the test, but this may also fail if the
    device is too slow.
    The proper way to fix this is using Idling Resources.
    Idling Resources are special objects that Espresso can track. In a UI Test (AndroidTest),
    Espresso will automatically wait until every IdlingResource has finished.

    I use a CountingIdlingResource to make Espresso aware of when Retrofit has finished
    retrieving the data from the online service.

    The idling resource must be registered with Espresso in the UI Test, for example in the
    @BeforeClass section

    References:
    https://stackoverflow.com/questions/30733718/how-to-use-espresso-idling-resource-for-network-calls
    https://developer.android.com/training/testing/espresso/idling-resource
     */
    private static final String RESOURCE = "RESOURCE";
    private static final CountingIdlingResource idlingResource = new CountingIdlingResource(RESOURCE);

    // Semaforo
    private static final Phaser lock = new Phaser(1);

    public static void getEvents(Listener<List<Event>> listener) {
        // signal Espresso that Retrofit has started execution. Espresso will wait until the
        // idling resource is finished (with the decrement call below)
        incrementIdlingResource();

        EventsAPI eventosService = EventsAPIService.getEventsServiceInstance(source);
        Call<EventsAPIResponse> callEvents = eventosService.getEventosResponse();

        callEvents.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<EventsAPIResponse> call, @NonNull Response<EventsAPIResponse> response) {
                EventsAPIResponse body = response.body();
                if (body != null) {
                    listener.onSuccess(body.getEvents());
                    decrementIdlingResource();  // signal Espresso that Retrofit has finished
                }
            }

            @Override
            public void onFailure(@NonNull Call<EventsAPIResponse> call, @NonNull Throwable t) {
                Log.d("DEBUG", "RETROFIT FAILURE");
                listener.onFailure();
                decrementIdlingResource(); // signal Espresso that Retrofit has finished
            }
        });
    }

    /**
     * Set the source of data to the repository hosted unican.es with known data
     */
    public static void setLocalSource() {
        source = EventsAPIService.Source.UNICAN;
    }

    /**
     * Set the source of data to the repository hosted by the Ayuntamiento de Santander
     */
    public static void setOnlineSource() {
        source = EventsAPIService.Source.AYTO;
    }

    /**
     * Set the source of data to the repository hosted by the Ayuntamiento de Santander
     */
    public static void setFakeSource() {
        source = EventsAPIService.Source.FAKE;
    }
    
    /*
     * Idling Resources methods
     */

    public static IdlingResource getIdlingResource() {
        return idlingResource;
    }

    private static void incrementIdlingResource() {
        idlingResource.increment();
        lock.register();
    }

    private static void decrementIdlingResource() {
        if (!idlingResource.isIdleNow()) {
            idlingResource.decrement();
        }
        lock.arriveAndDeregister();
    }

    public static Phaser getAsyncCounter() {
        return lock;
    }

}
