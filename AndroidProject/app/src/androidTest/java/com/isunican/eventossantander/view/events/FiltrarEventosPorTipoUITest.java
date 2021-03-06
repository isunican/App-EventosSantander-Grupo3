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
 * Clase de prueba de interfaz de usuario de la historia "Filtrar eventos por tipo"
 * @author Álvaro López Alonso
 */
public class FiltrarEventosPorTipoUITest {

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
    public void filtrarEventosPorTipo(){

        /*
         * UIT.1A: Se comprueba que tras no marcar ningún tipo de evento en las
         * opciones de filtrado, se muestra la lista de eventos original sin ningún filtrado.
         */

        onView(ViewMatchers.withId(R.id.btn_filtrar)).perform(click()); // Se selecciona el botón de filtrar
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
         * UIT.1C: Se comprueba que tras marcar todos los tipos de evento en las
         * opciones de filtrado, se muestran los eventos de todos los tipos
         */

        onView(withId(R.id.btn_filtrar)).perform(click()); // Se selecciona el botón de filtrar
        onView(withText("Arquitectura")).perform(click()); // Se marca el checkbox de "Arquitectura"
        onView(withText("Artes plásticas")).perform(click()); // Se marca el checkbox de "Artes plásticas"
        onView(withText("Cine/Audiovisual")).perform(click()); // Se marca el checkbox de "Cine/Audiovisual"
        onView(withText("Edición/Literatura")).perform(click()); // Se marca el checkbox de "Edición/Literatura"
        onView(withText("Formación/Talleres")).perform(click()); // Se marca el checkbox de "Formación/Talleres"
        onView(withText("Fotografía")).perform(click()); // Se marca el checkbox de "Fotografía"
        onView(withText("Infantil")).perform(click()); // Se marca el checkbox de "Infantil"
        //onView(withText("Música")).perform(click()); // Se marca el checkbox de "Música"
        onView(withText("Online")).perform(click()); // Se marca el checkbox de "Online"
        onView(withText("Otros")).perform(click()); // Se marca el checkbox de "Otros"
        onView(withText("APLICAR")).perform(click()); // Se selecciona el botón de aplicar
        // Comprobamos que se muestran los eventos de todos los tipos
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Menéndez Pelayo y José Echegaray en la polémica de la Ciencia Española")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(5);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Maria Sybilla Merian y Alida Withoos: Mujeres, Arte y Ciencia en la Edad Moderna")));

    }
}
