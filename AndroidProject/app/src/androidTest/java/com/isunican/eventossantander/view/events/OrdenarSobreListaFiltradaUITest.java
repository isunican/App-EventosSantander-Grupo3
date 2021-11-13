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
 * Clase de prueba de interfaz de usuario de la historia "Ordenar Sobre Lista Filtrada"
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
         * UIT.1C: Se comprueba que al ordenar ascendentemente sobre una lista previamente filtrada,
         * se conservan los filtros.
         */

        //Aplicamos los filtros previos
        onView(ViewMatchers.withId(R.id.btn_filtrar)).perform(click());
        onView(withText("Música")).perform(click());
        onView(withText("Otros")).perform(click());
        onView(withText("APLICAR")).perform(click());

        onView(withId(R.id.eventsListView)).check(matches(withListSize(98)));

        DataInteraction event; // Objeto para referenciar el contenido dentro de los elementos del ListView

        //Comprobamos la posicion de algunos eventos para comprobar despues si funciona la ordenacion
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Concierto de la Camerata Coral de la UC ")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(21);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Alberto Garrido en el ciclo \"En Contexto\"")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(97);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Pablo Solo en directo presentando \"Alondras\"")));

        //Aplicamos la ordenacion ascendente
        onView(ViewMatchers.withId(R.id.btn_ordenar)).perform(click());
        onView(withText("Ascendente (A-Z)")).perform(click());
        onView(withText("APLICAR")).perform(click());

        onView(withId(R.id.eventsListView)).check(matches(withListSize(98)));

        //Comprobamos la posicion de los eventos anteriores par comprobar si funciona la ordenacion
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Concierto de la Camerata Coral de la UC ")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(21);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Omar Montes cierra el Magdalena Deluxe")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(97);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Museo del Agua: Historia sobre el abastecimiento de agua de Santander ")));

        //----------------------------------------------------------------------------------------------------------------
        onView((ViewMatchers.withId(R.id.menu_refresh))).perform(click()); // Recargamos la app para reiniciar los filtros
        //----------------------------------------------------------------------------------------------------------------


        /*
         * UIT.1D: Se comprueba que al ordenar descendentemente sobre una lista previamente filtrada,
         * se conservan los filtros.
         */

        //Aplicamos los filtros previos
        onView(ViewMatchers.withId(R.id.btn_filtrar)).perform(click());
        onView(withText("Música")).perform(click());
        onView(withText("Otros")).perform(click());
        onView(withText("APLICAR")).perform(click());

        onView(withId(R.id.eventsListView)).check(matches(withListSize(98)));

        //Comprobamos la posicion de algunos eventos para comprobar despues si funciona la ordenacion
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Concierto de la Camerata Coral de la UC ")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(21);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Alberto Garrido en el ciclo \"En Contexto\"")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(97);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Pablo Solo en directo presentando \"Alondras\"")));

        //Aplicamos la ordenacion ascendente
        onView(ViewMatchers.withId(R.id.btn_ordenar)).perform(click());
        onView(withText("Descendente (Z-A)")).perform(click());
        onView(withText("APLICAR")).perform(click());

        onView(withId(R.id.eventsListView)).check(matches(withListSize(98)));

        //Comprobamos la posicion de los eventos anteriores par comprobar si funciona la ordenacion
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Museo del Agua: Historia sobre el abastecimiento de agua de Santander ")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Encuentro de creadores")));
        //Al haber solo 6 evento de tipo Otros, el numero 7 debe de ser el ultimo del caso anterior
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(6);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Pablo Solo en directo presentando \"Alondras\"")));
        //El ultimo evento deberá de ser el primero del caso anterior
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(97);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));

        //----------------------------------------------------------------------------------------------------------------
        onView((ViewMatchers.withId(R.id.menu_refresh))).perform(click()); // Recargamos la app para reiniciar los filtros
        //----------------------------------------------------------------------------------------------------------------

        /*
         * UIT.1E: Se comprueba que al filtrar sobre una lista previamente ordenada ascendentemente,
         * se conserva la ordenacion.
         */

        //Aplicamos los filtros previos
        onView(ViewMatchers.withId(R.id.btn_ordenar)).perform(click());
        onView(withText("Ascendente (A-Z)")).perform(click());
        onView(withText("APLICAR")).perform(click());

        onView(withId(R.id.eventsListView)).check(matches(withListSize(345)));

        //Comprobamos la posicion de algunos eventos para comprobar que no coinciden con los casos anteriores
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Palacio de la Magdalena. Visitas guiadas")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Visitas guiadas al Museo de Prehistoria y Arqueología - MUPAC")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(344);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Museo del Agua: Historia sobre el abastecimiento de agua de Santander ")));

        //Aplicamos el filtro de tipo
        onView(ViewMatchers.withId(R.id.btn_filtrar)).perform(click());
        onView(withText("Música")).perform(click());
        onView(withText("Otros")).perform(click());
        onView(withText("APLICAR")).perform(click());

        onView(withId(R.id.eventsListView)).check(matches(withListSize(98)));

        //Comprobamos la posicion de los eventos anteriores para comprobar si funciona la ordenacion ascendente
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Concierto de la Camerata Coral de la UC ")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(21);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Omar Montes cierra el Magdalena Deluxe")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(97);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Museo del Agua: Historia sobre el abastecimiento de agua de Santander ")));

        //----------------------------------------------------------------------------------------------------------------
        onView((ViewMatchers.withId(R.id.menu_refresh))).perform(click()); // Recargamos la app para reiniciar los filtros
        //----------------------------------------------------------------------------------------------------------------

        /*
         * UIT.1F: Se comprueba que al filtrar sobre una lista previamente ordenada descendentemente,
         * se conserva la ordenacion.
         */

        //Aplicamos los filtros previos
        onView(ViewMatchers.withId(R.id.btn_ordenar)).perform(click());
        onView(withText("Descendente (Z-A)")).perform(click());
        onView(withText("APLICAR")).perform(click());

        onView(withId(R.id.eventsListView)).check(matches(withListSize(345)));

        //Comprobamos la posicion de algunos eventos para comprobar que no coinciden con los casos anteriores
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Museo del Agua: Historia sobre el abastecimiento de agua de Santander ")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Encuentro de creadores")));
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(344);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Palacio de la Magdalena. Visitas guiadas")));

        //Aplicamos el filtro de tipo
        onView(ViewMatchers.withId(R.id.btn_filtrar)).perform(click());
        onView(withText("Música")).perform(click());
        onView(withText("Otros")).perform(click());
        onView(withText("APLICAR")).perform(click());

        onView(withId(R.id.eventsListView)).check(matches(withListSize(98)));

        //Comprobamos la posicion de los eventos anteriores para comprobar si funciona la ordenacion ascendente
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Alberto Garrido en el ciclo \"En Contexto\"")));
        //Al haber solo 6 evento de tipo Otros, el numero 7 debe de ser el ultimo del caso anterior
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(6);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        //El ultimo evento deberá de ser el primero del caso anterior
        event = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(97);
        event.onChildView(withId(R.id.item_event_title)).check(matches(withText("Pablo Solo en directo presentando \"Alondras\"")));
    }
}