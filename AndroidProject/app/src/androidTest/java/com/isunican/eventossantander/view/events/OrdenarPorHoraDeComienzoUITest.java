package com.isunican.eventossantander.view.events;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.isunican.eventossantander.view.matcher.Matchers.withListSize;

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
 * Clase de prueba de interfaz de usuario de la historia "Ordenar por hora de comienzo"
 * @author David Moreno Pérez
 */
public class OrdenarPorHoraDeComienzoUITest {

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
    public void ordenarPorHoraDeComienzo(){
        /*
         * UIT.1A: Se comprueba que tras marcar la opcion de ordenar por hora de comienzo "Mas proximas primero"
         * aparecen primero los eventos más cercanos a la fecha actual.
         */

        onView(ViewMatchers.withId(R.id.btn_ordenar)).perform(click()); // Se selecciona botón ordenar
        onView(withText("Más próximas primero")).perform(click());
        onView(withText("APLICAR")).perform(click());

        onView(withId(R.id.eventsListView)).check(matches(withListSize(345))); //Comprobamos que se cargan todos los eventos
        // Comprobamos que se muestran los eventos ordenados correctamente
        DataInteraction evento; // Objeto para referenciar el contenido dentro de los elementos del ListView
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        evento.onChildView(withId(R.id.item_event_date)).check(matches(withText("Sábado 31/07/2021, todo el día. ")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Museo virtual \"Luis Quintanilla, arte y memoria\"")));
        evento.onChildView(withId(R.id.item_event_date)).check(matches(withText("Sábado 31/07/2021, todo el día. ")));

        onView((ViewMatchers.withId(R.id.menu_refresh))).perform(click()); // Recargamos la app para reiniciar los filtros

        /*
         * UIT.1B: Se comprueba que tras marcar la opcion de ordenar por hora de comienzo "Menos proximas primero"
         * aparecen primero los eventos más lejanos a la fecha actual.
         */

        onView(ViewMatchers.withId(R.id.btn_ordenar)).perform(click()); // Se selecciona botón ordenar
        onView(withText("Menos próximas primero")).perform(click());
        onView(withText("APLICAR")).perform(click());

        onView(withId(R.id.eventsListView)).check(matches(withListSize(345))); // Comprobamos que se cargan todos los eventos
        // Comprobamos que se muestran los eventos ordenados correctamente
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Masters of Rock")));
        evento.onChildView(withId(R.id.item_event_date)).check(matches(withText("Viernes 11/03/2022, a las 21:00h. ")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Desakato en concierto")));
        evento.onChildView(withId(R.id.item_event_date)).check(matches(withText("Sábado 12/02/2022, a las 21:00h. ")));

        onView((ViewMatchers.withId(R.id.menu_refresh))).perform(click()); // Recargamos la app para reiniciar los filtros

        /*
         * UIT.1C: Se comprueba que tras marcar la opcion de ordenar por hora de comienzo "Mas proximas primero"
         * pulsar el boton de cancelar aparece la lista de eventos sin aplicar ningun tipo de ordenacion.
         */

        onView(ViewMatchers.withId(R.id.btn_ordenar)).perform(click()); // Se selecciona botón ordenar
        onView(withText("Más próximas primero")).perform(click());
        onView(withText("CANCELAR")).perform(click());

        onView(withId(R.id.eventsListView)).check(matches(withListSize(345))); // Comprobamos que se cargan todos los eventos
        // Comprobamos que se muestran los eventos ordenados correctamente
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        evento.onChildView(withId(R.id.item_event_date)).check(matches(withText("Sábado 31/07/2021, todo el día. ")));

        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(5);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Maria Sybilla Merian y Alida Withoos: Mujeres, Arte y Ciencia en la Edad Moderna")));
        evento.onChildView(withId(R.id.item_event_date)).check(matches(withText("Sábado 31/07/2021, todo el día. ")));

        onView((ViewMatchers.withId(R.id.menu_refresh))).perform(click()); // Recargamos la app para reiniciar los filtros

        /*
         * UIT.1D: Se comprueba que tras marcar la opcion de ordenar por hora de comienzo sin marcar ningun tipo de ordenacion
         * y pulsar el boton de cancelar aparece la lista de eventos sin aplicar ningun tipo de ordenacion.
         */

        onView(ViewMatchers.withId(R.id.btn_ordenar)).perform(click()); // Se selecciona botón ordenar
        onView(withText("CANCELAR")).perform(click());

        onView(withId(R.id.eventsListView)).check(matches(withListSize(345))); // Comprobamos que se cargan todos los eventos
        // Comprobamos que se muestran los eventos ordenados correctamente
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        evento.onChildView(withId(R.id.item_event_date)).check(matches(withText("Sábado 31/07/2021, todo el día. ")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Menéndez Pelayo y José Echegaray en la polémica de la Ciencia Española")));
        evento.onChildView(withId(R.id.item_event_date)).check(matches(withText("Lunes 02/08/2021, a las 20:00h. ")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(5);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Maria Sybilla Merian y Alida Withoos: Mujeres, Arte y Ciencia en la Edad Moderna")));
        evento.onChildView(withId(R.id.item_event_date)).check(matches(withText("Sábado 31/07/2021, todo el día. ")));

    }
}
