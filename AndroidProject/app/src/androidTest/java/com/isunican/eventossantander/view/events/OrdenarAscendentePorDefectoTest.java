package com.isunican.eventossantander.view.events;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
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
 * Clase de prueba de interfaz de usuario de la historia "Ordenar ascendente por defecto"
 * @author Sergio Gallego Alvarez
 */
public class OrdenarAscendentePorDefectoTest {

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
    public void OrdenarAscendentePorDefecto() {
        /*
         * UIT.1A: Se comprueba que tras entrar al menu de ordenar, ascendentemente esta marcado por defecto.
         */

        onView(ViewMatchers.withId(R.id.btn_ordenar)).perform(click()); // Se selecciona botón ordenar para abrir el menu
        onView(ViewMatchers.withId(R.id.btn_ordenar_ascendente)).check(matches(isChecked())); // Se comprueba que el boton de ascendente esta seleciconado por defecto
        onView(withText("APLICAR")).perform(click()); // Se selecciona el boton de aceptar

        onView(withId(R.id.eventsListView)).check(matches(withListSize(345))); //Comprobamos que se cargan todos los eventos
        // Comprobamos que se muestran los eventos ordenados correctamente
        DataInteraction evento; // Objeto para referenciar el contenido dentro de los elementos del ListView
        // Se comprueba el primer elemento de la lista
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Palacio de la Magdalena. Visitas guiadas")));
        // Se comprueba el segundo elemento de la lista
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Visitas guiadas al Museo de Prehistoria y Arqueología - MUPAC")));
        // Se comprueba el anteultimo elemento de la lista
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(343);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Encuentro de creadores")));

        onView((ViewMatchers.withId(R.id.menu_refresh))).perform(click()); // Recargamos la app para reiniciar los filtros

        /*
         * UIT.1B: Se comprueba que tras entrar al menu de ordenar y seleccionar el boton de cancelar,
         * la lista se mantiene como estaba en un principio.
         */

        onView(ViewMatchers.withId(R.id.btn_ordenar)).perform(click()); // Se selecciona botón ordenar para abrir el menu
        onView(ViewMatchers.withId(R.id.btn_ordenar_ascendente)).check(matches(isChecked())); // Se comprueba que el boton de ascendente esta seleciconado por defecto
        onView(withText("CANCELAR")).perform(click()); // Se selecciona el boton de cancelar

        onView(withId(R.id.eventsListView)).check(matches(withListSize(345))); // Comprobamos que se cargan todos los eventos
        // Comprobamos que se muestran los eventos como en un principio

        // Se comprueba el primer elemento de la lista
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        // Se comprueba el segundo elemento de la lista
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Menéndez Pelayo y José Echegaray en la polémica de la Ciencia Española")));
        // Se comprueba el anteultimo elemento de la lista
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(343);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Huellas de Judas Arrieta en Santa Lucía")));
        // Se comprueba el ultimo elemento de la lista
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(344);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Visiones Urbanas con ArteSantander 2021")));
    }
}
