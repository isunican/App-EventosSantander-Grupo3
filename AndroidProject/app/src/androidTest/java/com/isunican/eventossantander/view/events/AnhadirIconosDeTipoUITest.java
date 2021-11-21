package com.isunican.eventossantander.view.events;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.EventsRepository;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/*
 * Clase de prueba de interfaz de usuario de la historia "Anhadir iconos de tipo"
 * @author
 */
public class AnhadirIconosDeTipoUITest {

    /**
     * Load known events json
     * https://personales.unican.es/rivasjm/resources/agenda_cultural.json
     */
    @BeforeClass
    public static void setUp() {
        EventsRepository.setLocalSource();
        IdlingRegistry.getInstance().register(EventsRepository.getIdlingResource());
    }

    @AfterClass
    public static void clean() {
        EventsRepository.setOnlineSource();
        IdlingRegistry.getInstance().unregister(EventsRepository.getIdlingResource());
    }

    @Rule
    public ActivityScenarioRule<EventsActivity> activityRule =
            new ActivityScenarioRule<>(EventsActivity.class);


    @Test
    public void anhadirIconosDeTipo() {

        /*
         * UIT.1A
         */

        onView(ViewMatchers.withId(R.id.btn_filtrar)).perform(click()); // Se selecciona el botón de filtrar

        // Se selecciona dos tipos de categoria y se presiona boton aplicar
        onView(withText("Música")).perform(click());
        onView(withText("Online")).perform(click());
        onView(withText("APLICAR")).perform(click());

        DataInteraction evento; // Objeto para referenciar el contenido dentro de los elementos del ListView

        // Evento de musica
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        // Se comprueba que el evento de música tiene la imagen correspondiente a un evento de tipo musica
        evento.onChildView(withId(R.id.item_event_image)).check(matches(withId()));;

        // Evento Online
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        // Se comprueba que el evento online tiene la imagen correspondiente a un evento de tipo online
        evento.onChildView(withId(R.id.item_event_image)).check(matches(withId(R.drawable.online)));;

        onView((ViewMatchers.withId(R.id.menu_refresh))).perform(click()); // Recargamos la app para reiniciar los filtros

        /*
         * UIT.1B
         */
        onView(ViewMatchers.withId(R.id.btn_filtrar)).perform(click()); // Se selecciona el botón de filtrar

        // Se selecciona dos tipos de categoria y se presiona boton aplicar
        onView(withText("Fotografía")).perform(click());
        onView(withText("APLICAR")).perform(click());

        // Se comprueba que todos los eventos de tipo Fotografia contienen la misma imagen

        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        // Se comprueba que el evento de música tiene la imagen correspondiente a un evento de tipo musica
        evento.onChildView(withId(R.id.item_event_image)).check(matches(withId(R.drawable.fotografia)));;

        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        // Se comprueba que el evento de música tiene la imagen correspondiente a un evento de tipo musica
        evento.onChildView(withId(R.id.item_event_image)).check(matches(withId(R.drawable.fotografia)));;

        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(2);
        // Se comprueba que el evento de música tiene la imagen correspondiente a un evento de tipo musica
        evento.onChildView(withId(R.id.item_event_image)).check(matches(withId(R.drawable.fotografia)));;

        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(3);
        // Se comprueba que el evento de música tiene la imagen correspondiente a un evento de tipo musica
        evento.onChildView(withId(R.id.item_event_image)).check(matches(withId(R.drawable.fotografia)));;

        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(4);
        // Se comprueba que el evento de música tiene la imagen correspondiente a un evento de tipo musica
        evento.onChildView(withId(R.id.item_event_image)).check(matches(withId(R.drawable.fotografia)));;

        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(5);
        // Se comprueba que el evento de música tiene la imagen correspondiente a un evento de tipo musica
        evento.onChildView(withId(R.id.item_event_image)).check(matches(withId(R.drawable.fotografia)));;

        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(6);
        // Se comprueba que el evento de música tiene la imagen correspondiente a un evento de tipo musica
        evento.onChildView(withId(R.id.item_event_image)).check(matches(withId(R.drawable.fotografia)));;


        onView((ViewMatchers.withId(R.id.menu_refresh))).perform(click()); // Recargamos la app para reiniciar los filtros

        /*
         * UIT.1C
         */

        // Se comprobará que los eventos de distinto tipo tienen asociados imagenes diferentes



    }


}
