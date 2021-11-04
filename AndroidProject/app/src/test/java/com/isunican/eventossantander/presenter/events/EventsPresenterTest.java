package com.isunican.eventossantander.presenter.events;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.events.IEventsContract;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import android.os.Build;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class EventsPresenterTest {

    // Creación del sut
    private EventsPresenter sut;

    // Mocks
    @Mock
    IEventsContract.View mockView;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


    // Objetos
    List<String> listaVacia;
    List<String> listaConElemento;
    List<String> listaLlena;
    List<String> listaErronea;

    List<Event> listaEventos;


    @Before
    public void setUp() {

        EventsRepository.setLocalSource();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }


    /*
     * Test del método onFiltrarClickedTest
     * @author Álvaro López Alonso
     */
    @Test
    public void onFiltrarClickedTest() throws InterruptedException {

        sut = new EventsPresenter(mockView);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        listaVacia = new ArrayList<String>();
        listaConElemento = new ArrayList<String>();
        listaLlena = new ArrayList<String>();
        listaErronea = new ArrayList<String>();

        listaConElemento.add("Música");
        listaLlena.add("Arquitectura");
        listaLlena.add("Artes plásticas");
        listaLlena.add("Cine/Audiovisual");
        listaLlena.add("Edición/Literatura");
        listaLlena.add("Formación/Talleres");
        listaLlena.add("Fotografía");
        listaLlena.add("Infantil");
        listaLlena.add("Música");
        listaLlena.add("Online");
        listaLlena.add("Otros");
        listaErronea.add("Marionetas");

        listaEventos = new ArrayList<Event>();

        // IT.1A: Se comprueba que si la lista de tipos de evento introducida contiene un
        // tipo de evento, los eventos filtrados corresponderán solo a los del tipo de evento seleccionado.
        sut.onFiltrarClicked(listaConElemento);
        assertEquals(92, sut.getFilteredEvents().size());
        assertEquals(sut.getFilteredEvents().get(5).getCategoria(),("Música"));
        assertEquals(sut.getFilteredEvents().get(24).getCategoria(),("Música"));
        assertEquals(sut.getFilteredEvents().get(68).getCategoria(),("Música"));

        // IT.1B: Se comprueba que si la lista de tipos de evento introducida está vacía,
        // los eventos filtrados sean igual a los eventos cacheados.
        sut.onFiltrarClicked(listaVacia);
        assertEquals(sut.getFilteredEvents(),(sut.getCachedEvents()));
        assertEquals(345, sut.getFilteredEvents().size());

        // IT.1C: Se comprueba que si la lista de tipos de evento introducida contiene
        // todos los tipos de evento, los eventos filtrados sean todos los eventos
        // caheados menos aquellos que no tengan tipo.
        sut.onFiltrarClicked(listaLlena);
        assertEquals(310, sut.getFilteredEvents().size());

        // IT.1D: Se comprueba que si la lista introducida contiene un tipo de evento
        // no existente, los eventos filtrados sean igual a los eventos cacheados.
        sut.onFiltrarClicked(listaErronea);
        assertEquals(sut.getFilteredEvents(),(sut.getCachedEvents()));
        assertEquals(345, sut.getFilteredEvents().size());
    }

    /*
     * Test del método loadData
     * @author Álvaro López Alonso
     */
    @Test
    public void loadData() {

        // IT.2A: Se comprueba que cuando se puede acceder a la base de datos, se cargan
        // los eventos de dicha base de datos
        sut = new EventsPresenter(mockView);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(345, sut.getCachedEvents().size());
        assertEquals(sut.getCachedEvents().get(0).getCategoria(),("Música"));
        assertEquals(sut.getCachedEvents().get(0).getNombre(),("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea"));
        assertEquals(sut.getCachedEvents().get(3).getCategoria(),("Artes plásticas"));
        assertEquals(sut.getCachedEvents().get(3).getNombre(),("Gabinete de estampas virtual de la UC"));

        // IT.2B: Se comprueba que cuando no se puede acceder a la base de datos, no se
        // cargan los eventos
        EventsRepository.setFakeSource();
        sut = new EventsPresenter(mockView);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(sut.getCachedEvents(),null);

    }

    @Test
    public void testOrdenar() {

        sut = new EventsPresenter(mockView);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // IT.1A: Se comprueba que la lista esta ordeanada ascendentemente

        // Se ordena la lista por categoria por orden ascendente
        sut.onOrdenarCategoriaClicked(0);

        //Se consigue la lista y se iterara sobre ella para saber si esta ordenada
        List<Event> listaordenada = sut.getCachedEventsOrdenados();
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

        // IT.1B: Se comprueba que la lista esta ordenada de manera descendenete

        // Se ordena la lista por categoria por orden ascendente
        sut.onOrdenarCategoriaClicked(1);

        //Se consigue la lista y se iterara sobre ella para saber si esta ordenada
        listaordenada = sut.getCachedEventsOrdenados();
        iter = listaordenada.listIterator();

        // En el caso de que este vacia, la lista siempre estaria ordenada
        if (!iter.hasNext()) {
            assertTrue(true);
        }

        // En el caso de que la lista tenga elementos
        evento1 = iter.next();

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
