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
import com.isunican.eventossantander.view.events.EventsActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/*
 * Clase de prueba de interfaz de usuario de la historia "Mantener estado de filtros"
 * @author Juan Vélez Velasco
 */
public class MantenerEstadoDeFiltrosUITest {

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
            new ActivityScenarioRule(EventsActivity.class);


    @Test
    public void filtrarEventosPorTipo(){

        /*
         * UIT.1A: Se comprueba no seleccionar ningun tipo, se muestran todos los eventos
         */

        onView(ViewMatchers.withId(R.id.btn_filtrar)).perform(click()); // Se selecciona el botón de filtrar
        //TOdo comprobar que no hay ningun checkbox marcado
        onView(withText("APLICAR")).perform(click()); // Se selecciona el botón de aplicar
        // Comprobamos que se muestra la lista de eventos original
        DataInteraction evento; // Objeto para referenciar el contenido dentro de los elementos del ListView
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Menéndez Pelayo y José Echegaray en la polémica de la Ciencia Española")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(3);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Gabinete de estampas virtual de la UC")));

        /*
         * UIT.1B: Se comprueba que tras marcar un tipo de evento en las
         * opciones de filtrado, se muestran solo los eventos de ese tipo
         */

        onView(withId(R.id.btn_filtrar)).perform(click()); // Se selecciona el botón de filtrar
        //TOdo comprobar que no hay ningun checkbox marcado
        onView(withText("Música")).perform(click()); // Se marca el checkbox de "Música"
        onView(withText("APLICAR")).perform(click()); // Se selecciona el botón de aplicar
        // Comprobamos que solo se muestran los eventos de tipo Música
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(3);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("The Guitar Conference")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(5);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Alain Jean-Marie Quintet")));

        /*
         * UIT.1C: Se comprueba que tras marcar un tipo, aplicar y volver a abrir, se mantiene el checkbox y que tras pulsar mas checkbos la lista se actualiza.
         */

        onView(withId(R.id.btn_filtrar)).perform(click()); // Se selecciona el botón de filtrar
        //TOdo comprobar que hay un checkbox marcado
        onView(withText("Online")).perform(click()); // Se marca el checkbox de "Arquitectura"
        onView(withText("Artes plásticas")).perform(click()); // Se marca el checkbox de "Artes plásticas"
        onView(withText("APLICAR")).perform(click()); // Se selecciona el botón de aplicar
        // Comprobamos que se muestran los eventos de todos los tipos
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Menéndez Pelayo y José Echegaray en la polémica de la Ciencia Española")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(5);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Maria Sybilla Merian y Alida Withoos: Mujeres, Arte y Ciencia en la Edad Moderna")));


        /*
         * UIT.1D: Se comprueba que tras marcar unos tipos, cerrar y volver abrir, se elimina un tipo de los seleccionados y se actualiza la lista.
         */
        onView(withId(R.id.menu_refresh)).perform(click());
        onView(withId(R.id.btn_filtrar)).perform(click()); // Se selecciona el botón de filtrar
        onView(withText("Online")).perform(click());
        onView(withText("Artes plásticas")).perform(click());
        onView(withText("Arquitectura")).perform(click());
        onView(withText("Música")).perform(click());
        onView(withText("APLICAR")).perform(click()); // Se selecciona el botón de aplicar
        onView(withId(R.id.btn_filtrar)).perform(click()); // Se selecciona el botón de filtrar
        //TODO comprobar checbox
        onView(withText("Arquitectura")).perform(click());//Se deselecciona el tipo arquitectura
        

        // Comprobamos que se muestran los eventos de todos los tipos
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Menéndez Pelayo y José Echegaray en la polémica de la Ciencia Española")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(5);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Maria Sybilla Merian y Alida Withoos: Mujeres, Arte y Ciencia en la Edad Moderna")));
    }
}
