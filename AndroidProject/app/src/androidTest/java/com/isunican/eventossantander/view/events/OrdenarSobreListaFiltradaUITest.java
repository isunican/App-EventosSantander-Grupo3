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
 * Clase de prueba de interfaz de usuario de la historia "Ordenar sobre lista filtrada"
 * @author Adrián García Cubas
 */
public class OrdenarSobreListaFiltradaUITest {
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
    public void ordenarSobreListaFiltrada(){
        /*
         * UIT.1C: Se comprueba que tras ordenar sobre una lista previamente filtrada, se aplica la
         * ordenación correctamente y se mantiene el filtro aplicado
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
    }
}