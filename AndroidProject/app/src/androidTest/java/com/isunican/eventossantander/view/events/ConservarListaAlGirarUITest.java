
package com.isunican.eventossantander.view.events;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import android.os.RemoteException;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.uiautomator.UiDevice;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.EventsRepository;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;


/*
 * Clase de prueba de interfaz de usuario de la historia "Conservar lista al girar"
 * @author Álvaro López Alonso
 */
public class ConservarListaAlGirarUITest {

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
    public void ConservarListaAlGirar() throws RemoteException {

//     //UiDevice device = UiDevice.getInstance(getInstrumentation());

        /*
         * UIT.1A: Se rota el móvil en el estado inicial de la aplicación.
         */
//        device.setOrientationRight();

        // Se rota de nuevo a la posición normal
//        device.setOrientationNatural();

        /*
         * UIT.1B: Se aplica un filtro antes del rotar el móvil.
         */
//        onView(withId(R.id.btn_filtrar)).perform(click()); // Se selecciona el botón de filtrar
//        onView(withText("Música")).perform(click()); // Se marca el checkbox de "Música"
//        onView(withText("APLICAR")).perform(click()); // Se selecciona el botón de aplicar
//        device.setOrientationRight();

        // Se rota de nuevo a la posición normal
//        device.setOrientationNatural();

        /*
         * UIT.1C: Se aplica un filtro con el móvil en apaisado
         */
//        device.setOrientationRight();
//        onView(withId(R.id.btn_filtrar)).perform(click()); // Se selecciona el botón de filtrar
//        onView(withText("Arquitectura")).perform(click()); // Se marca el checkbox de "Arquitectura"
//        onView(withText("APLICAR")).perform(click()); // Se selecciona el botón de aplicar

        // Se rota de nuevo a la posición normal
//        device.setOrientationNatural();
    }
}
