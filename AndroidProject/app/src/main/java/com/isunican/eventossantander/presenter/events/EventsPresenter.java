package com.isunican.eventossantander.presenter.events;


import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.model.comparators.EventsComparatorCategoria;
import com.isunican.eventossantander.view.Listener;
import com.isunican.eventossantander.view.events.IEventsContract;

import java.util.List;

public class EventsPresenter implements IEventsContract.Presenter {

    private final IEventsContract.View view;
    private List<Event> cachedEvents;
    private List<Event> cachedEventsOrdenados;
    private List<Event> cachedEventsOriginal;



    public EventsPresenter(IEventsContract.View view) {
        this.view = view;
        loadData();
    }

    private void loadData() {
        EventsRepository.getEvents(new Listener<List<Event>>() {
            @Override
            public void onSuccess(List<Event> data) {
                view.onEventsLoaded(data);
                view.onLoadSuccess(data.size());
                cachedEvents = data;
                cachedEventsOriginal = cachedEvents;
            }

            @Override
            public void onFailure() {
                view.onLoadError();
                cachedEvents = null;
            }
        });
    }

    @Override
    public void onEventClicked(int eventIndex) {
        if (cachedEvents != null && eventIndex < cachedEvents.size()) {
            Event event = cachedEvents.get(eventIndex);
            view.openEventDetails(event);
        }
    }

    @Override
    public void onReloadClicked() {
        loadData();
    }

    @Override
    public void onInfoClicked() {
        view.openInfoView();
    }

    @Override
    public void onOrdenarCategoriaAscendenteClicked() {
        EventsComparatorCategoria ecc = new EventsComparatorCategoria();
        java.util.Collections.sort(cachedEvents,ecc);
        cachedEventsOrdenados = cachedEvents;
        view.onEventsLoaded(cachedEventsOrdenados);
    }

    @Override
    public void onOrdenarCategoriaDescendenteClicked() {
        EventsComparatorCategoria ecc = new EventsComparatorCategoria();
        java.util.Collections.sort(cachedEvents,ecc);
        java.util.Collections.sort(cachedEvents, java.util.Collections.reverseOrder());
        cachedEventsOrdenados = cachedEvents;
        view.onEventsLoaded(cachedEventsOrdenados);
    }

    @Override
    public void onFiltrarClicked(List<String> checkboxSeleccionados){

    }
}
