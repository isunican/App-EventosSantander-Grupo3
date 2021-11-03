package com.isunican.eventossantander.view.events;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;

import android.view.View;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.events.EventsActivity;

import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/*
 * Clase de prueba de interfaz de usuario de la historia "Filtrar eventos por fecha"
 * @author Juan Vélez Velasco
 */
public class FiltrarEventosPorFechaUITest {

    /**
     * Load known events json
     * https://personales.unican.es/rivasjm/resources/agenda_cultural.json
     */
    @BeforeClass
    public static void setUp() {
        EventsRepository.setLocalSource();
        IdlingRegistry.getInstance().register(EventsRepository.getIdlingResource());

        activityRule.getScenario().onActivity(
                activity -> decorView = activity.getWindow().getDecorView());
    }
    @AfterClass
    public static void clean() {
        EventsRepository.setOnlineSource();
        IdlingRegistry.getInstance().unregister(EventsRepository.getIdlingResource());
    }
    @Rule
    public static ActivityScenarioRule<EventsActivity> activityRule =
            new ActivityScenarioRule(EventsActivity.class);
    private static View decorView;

    @Test
    public void filtrarEventosPorFecha() {

        /*
         * UIT.1A: Se comprueba que tras no indicar ninguna fecha de inicio
         * ni de fin, se muestra un pop-up indicicando que se debe introducir
         * ambas fechas.
         */

        onView(withId(R.id.menu_filter_date)).perform(click()); // Se selecciona el botón de filtrar
        onView(withId(R.id.filtrar_fecha_inicio_texto)).check(matches(withText("")));
        onView(withId(R.id.filtrar_fecha_fin_texto)).check(matches(withText("")));
        onView(withText("ACEPTAR")).perform(click());// Se selecciona el botón de aplicar
        onView(withText("Ambas fechas deben estar seleccionadas")).inRoot(RootMatchers.withDecorView((Matcher<View>) decorView)).check(matches(isDisplayed()));
        /*
         * UIT.1B: Se comprueba que tras introducir fecha de inicio pero no de fin,
         * se muestra un pop-up indicicando que se debe introducir ambas fechas.
         */
/*
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
         * UIT.1C: Se comprueba que introducir un rango de fechas en en el que
         * no hay eventos, se muestra toda la lista de eventos.
         */
/*
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
    /*
     * UIT.1D: Se comprueba que tras introducir una fecha válida se muestran los
     * eventos que deberían mostrarse.
    */


        /*
         * UIT.1E: Se comprueba que tras seleccionar fechas y después pulsar el botón de
         * cancelar, se muestra la lista sin aplicar ningun filtro.
         */
    }
}

