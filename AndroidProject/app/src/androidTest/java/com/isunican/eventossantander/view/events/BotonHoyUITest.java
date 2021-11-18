package com.isunican.eventossantander.view.events;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.today.TodayEventsActivity;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/*
 * Clase de prueba de interfaz de usuario de la historia "Boton de hoy"
 * @author Sergio Pérez Landaburu
 */

public class BotonHoyUITest {
    private View decorView;

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

    @Before
    public void setUp2(){
        activityRule.getScenario().onActivity(
                activity -> decorView = activity.getWindow().getDecorView()
        );
    }

    @Rule
    public ActivityScenarioRule<EventsActivity> activityRule =
            new ActivityScenarioRule(EventsActivity.class);

    @Test
    public void botonHoyTest(){

        /*
         *UIT.A : Se comprobará que cuando se pulsa el boton HOY situado
         *        en la parte inferior de la interfaz se cambia de pestaña
         *        y que se muestra la lista correcta de eventos.
         */
        // Comprobamos que se muestra la lista de eventos original
        DataInteraction evento; // Objeto para referenciar el contenido dentro de los elementos del ListView

        onView(withId(R.id.menu_refresh)).perform(click());
        onView(withText("Loaded 345 events")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));
        //Comprobamos los eventos
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Menéndez Pelayo y José Echegaray en la polémica de la Ciencia Española")));

        //Clicamos el boton Hoy
        onView(withId(R.id.btn_rojo)).perform(click());
        onView(withText("Loaded 1 events")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));
        //Comprobamos el evento
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Mario Crespo nos adentra en la obra de Álvaro Pombo")));

        /*
         *UIT.B : Se comprobara que una vez en la pestaña correspondiente a
         *        a los eventos de hoy se vuelve a la pestaña principal,
         *        pulsando la funcion nueva añadida en el menu de los tres
         *        puntos y la pestaña se encuentra en el estado anterior.
         */

        onView(withClassName(containsString(TodayEventsActivity.class.getSimpleName())));
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText("Pantalla principal")).perform(click());
        //Comprobamos que estamos en la pantalla principal.
        onView(withId(R.id.btn_rojo)).check(matches(withText("HOY")));
        //Comprobamos los eventos
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(0);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea")));
        evento = onData(anything()).inAdapterView(withId(R.id.eventsListView)).atPosition(1);
        evento.onChildView(withId(R.id.item_event_title)).check(matches(withText("Menéndez Pelayo y José Echegaray en la polémica de la Ciencia Española")));


    }
}
