package com.isunican.eventossantander.presenter.events;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import android.os.Build;

import androidx.constraintlayout.utils.widget.MockView;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.view.events.IEventsContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Iterator;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class EventsPresenterTest {

    private EventsPresenter eventsPresenter;

    @Mock
    private IEventsContract.View mockView;

    @Before
    public void setUp() {

        mockView = mock(IEventsContract.View.class);
        eventsPresenter = new EventsPresenter(mockView);
    }

    @Test
    public void testOrdenarAscendente() {

        // Se ordena la lista por categoria por orden ascendente
        eventsPresenter.onOrdenarCategoriaClicked(0);

        //Se consigue la lista y se iterara sobre ella para saber si esta ordenada
        List<Event> listaordenada = eventsPresenter.getCachedEventsOrdenados();
        Iterator<Event> iter = listaordenada.listIterator();

        // En el caso de que este vacia, la lista siempre estaria ordenada
        if (!iter.hasNext()) {
            assertTrue(true);
        }

        // En el caso de que la lista tenga elementos
        Event evento1 = iter.next();

        while (iter.hasNext()) {
            Event evento2 = iter.next();
            if (evento1.getCategoria().compareTo(evento2.getCategoria()) > 0) {
                // En el caso de que se encuentre una discrepancia, fallaria el test
                fail("Lista no esta ordenada ascendentemente correctamente");
            }
            evento1 = evento2;
        }
        assertTrue(true);
    }

    @Test
    public void testOrdenarDescendente() {

        // Se ordena la lista por categoria por orden ascendente
        eventsPresenter.onOrdenarCategoriaClicked(0);

        //Se consigue la lista y se iterara sobre ella para saber si esta ordenada
        List<Event> listaordenada = eventsPresenter.getCachedEventsOrdenados();
        Iterator<Event> iter = listaordenada.listIterator();

        // En el caso de que este vacia, la lista siempre estaria ordenada
        if (!iter.hasNext()) {
            assertTrue(true);
        }

        // En el caso de que la lista tenga elementos
        Event evento1 = iter.next();

        while (iter.hasNext()) {
            Event evento2 = iter.next();
            if (evento1.getCategoria().compareTo(evento2.getCategoria()) < 0) {
                // En el caso de que se encuentre una discrepancia, fallaria el test
                fail("Lista no esta ordenada descendentemente correctamente");
            }
            evento1 = evento2;
        }
        assertTrue(true);
    }
}