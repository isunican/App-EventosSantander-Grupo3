package com.isunican.eventossantander.view.matcher;

import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


public class Matchers {

    /**
     * Metodo que comprueba el tamanho de la lista de eventos para las pruebas de interfaz
     * @param size tamanho esperado de la lista
     * @return Matcher<View>
     */
    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((ListView) view).getCount () == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should have " + size + " items");
            }
        };
    }
    /*
    Para usarlo sobre la lista de eventos de la view importar metodo  y poner de codigo:
    onView(withId(R.id.eventsListView)).check(matches(withListSize(tamanho que deberia de tener)));
     */
}
