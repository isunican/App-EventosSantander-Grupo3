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
     * @author Adrián García Cubas
     */
    @Test
    public void onFiltrarClickedTest() throws InterruptedException {

        sut = new EventsPresenter(mockView);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Inicilizamos las listas
        listaVacia = new ArrayList<String>();
        listaConElemento = new ArrayList<String>();
        listaLlena = new ArrayList<String>();
        listaErronea = new ArrayList<String>();

        //Las rellenamos como correspondan
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


        // IT.1A: Se comprueba que si la lista de tipos de evento esta vacía, los eventos filtrados
        // son igual a los eventos cacheados y no estarán ordenados.
        sut.onFiltrarClicked(listaVacia);
        assertEquals(345, sut.getFilteredEvents().size());
        assertEquals(sut.getFilteredEvents(), (sut.getCachedEvents()));


        // IT.1B: Se comprueba que si la lista de tipos de evento introducida contiene el tipo musica,
        // los eventos filtrados son de tipo música y estan ordenados de manera ascendente
        sut.onOrdenarCategoriaClicked(0); //Ordenamos ascendentemente los eventos
        sut.onFiltrarClicked(listaConElemento);
        assertEquals(sut.getFilteredEvents().get(0).getCategoria(), "Música");
        assertEquals(sut.getFilteredEvents().get(20).getCategoria(), "Música");
        assertEquals(sut.getFilteredEvents().get(45).getCategoria(), "Música");
        assertEquals(92, sut.getFilteredEvents().size()); //Comprobamos que solo hay eventos de tipo musica

        // IT.1C: Se comprueba que si la lista de tipos de evento introducida contiene
        // todos los tipos de evento, los eventos filtrados sean todos los eventos
        // cacheados menos aquellos que no tengan tipo y ordenados de manera descendente.
        sut.onOrdenarCategoriaClicked(1); //Ordenamos descendentemente los eventos
        sut.onFiltrarClicked(listaLlena);
        assertEquals(310, sut.getFilteredEvents().size());//Comprobamos que no estan los eventos sin tipo

        /*
        * Aqui comprobamos con todos los tipos de evento para comprbar que todos funcionan correctamente,
        * despues de estas comprobaciones, se comprobarán solo 4 tipos de eventos para mayor legibilidad.
        */
        assertEquals(sut.getFilteredEvents().get(0).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(6).getCategoria(), "Online");
        assertEquals(sut.getFilteredEvents().get(12).getCategoria(), "Música");
        assertEquals(sut.getFilteredEvents().get(104).getCategoria(), "Infantil");
        assertEquals(sut.getFilteredEvents().get(120).getCategoria(), "Fotografía");
        assertEquals(sut.getFilteredEvents().get(127).getCategoria(), "Formación/Talleres");
        assertEquals(sut.getFilteredEvents().get(145).getCategoria(), "Edición/Literatura");
        assertEquals(sut.getFilteredEvents().get(165).getCategoria(), "Cine/Audiovisual");
        assertEquals(sut.getFilteredEvents().get(223).getCategoria(), "Artes plásticas");
        assertEquals(sut.getFilteredEvents().get(301).getCategoria(), "Arquitectura");

        // IT.1D: Se comprueba que si la lista de tipos de evento esta vacia, estarán todos los
        // eventos ordenados de manera ascendente
        sut.onOrdenarCategoriaClicked(0); //Ordenamos ascendentemente los eventos
        sut.onFiltrarClicked(listaVacia);
        assertEquals(345, sut.getFilteredEvents().size()); //Comprobamos que estan todos los eventos
        assertEquals(sut.getFilteredEvents().get(340).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(334).getCategoria(), "Online");
        assertEquals(sut.getFilteredEvents().get(328).getCategoria(), "Música");
        assertEquals(sut.getFilteredEvents().get(236).getCategoria(), "Infantil");

        // IT.1E: Se comprueba que si la lista contiene un evento no existente, se devuelve
        // la lista de eventos original
        sut.onFiltrarClicked(listaErronea);
        assertEquals(345, sut.getFilteredEvents().size()); //Comprobamos que estan todos los eventos
        assertEquals(sut.getFilteredEvents(), (sut.getCachedEvents()));

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

    /*
     * Test del método onFiltrarClickedTest
     * @author Adrián García Cubas
     */
    @Test
    public void onOrdenarCategoriaClickedTest() {

        sut = new EventsPresenter(mockView);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Inicializamos las listas
        listaVacia = new ArrayList<String>();
        listaConElemento = new ArrayList<String>();

        //Las rellenamos como correspondan
        listaConElemento.add("Música");
        listaConElemento.add("Otros");

        // IT.2A: Se comprueba que si la lista eventosEnFiltrosCombinados esta ordenada de manera
        // ascendente correctamente con una lista de filtros vacia
        sut.onFiltrarClicked(listaVacia);
        sut.onOrdenarCategoriaClicked(0);
        assertEquals(345, sut.getFilteredEvents().size()); //Comprobamos que estan todos los eventos
        assertEquals(sut.getFilteredEvents().get(340).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(334).getCategoria(), "Online");
        assertEquals(sut.getFilteredEvents().get(328).getCategoria(), "Música");
        assertEquals(sut.getFilteredEvents().get(236).getCategoria(), "Infantil");

        // IT.2B: Se comprueba que si la lista eventosEnFiltrosCombinados esta ordenada de manera
        // descendente correctamente con una lista de filtros vacia
        sut.onFiltrarClicked(listaVacia);
        sut.onOrdenarCategoriaClicked(1);
        assertEquals(345, sut.getFilteredEvents().size()); //Comprobamos que estan todos los eventos
        assertEquals(sut.getFilteredEvents().get(0).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(6).getCategoria(), "Online");
        assertEquals(sut.getFilteredEvents().get(12).getCategoria(), "Música");
        assertEquals(sut.getFilteredEvents().get(104).getCategoria(), "Infantil");

        // IT.2C: Se comprueba que si la lista eventosEnFiltrosCombinados esta ordenada de manera
        // ascendente correctamente con una lista de filtros no vacia
        sut.onFiltrarClicked(listaConElemento);
        sut.onOrdenarCategoriaClicked(0);
        assertEquals(98, sut.getFilteredEvents().size()); //Comprobamos que estan todos los eventos
        assertEquals(sut.getFilteredEvents().get(97).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(96).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(95).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(94).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(93).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(92).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(91).getCategoria(), "Música");
        assertEquals(sut.getFilteredEvents().get(30).getCategoria(), "Música");
        assertEquals(sut.getFilteredEvents().get(0).getCategoria(), "Música");

        // IT.2D: Se comprueba que si la lista eventosEnFiltrosCombinados esta ordenada de manera
        // descendente correctamente con una lista de filtros no vacia
        sut.onFiltrarClicked(listaConElemento);
        sut.onOrdenarCategoriaClicked(1);
        assertEquals(98, sut.getFilteredEvents().size()); //Comprobamos que estan todos los eventos
        assertEquals(sut.getFilteredEvents().get(0).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(1).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(2).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(3).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(4).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(5).getCategoria(), "Otros");
        assertEquals(sut.getFilteredEvents().get(6).getCategoria(), "Música");
        assertEquals(sut.getFilteredEvents().get(30).getCategoria(), "Música");
        assertEquals(sut.getFilteredEvents().get(91).getCategoria(), "Música");

        // IT.2E: Se comprueba que si se le pasa un indice distino de 0 o 1 al metodo onOrdenarCategoriaCLicked
        // no se actualiza nada, la lista se queda como si no se hubiese llamado al metodo
        sut.onFiltrarClicked(listaVacia);
        sut.onOrdenarCategoriaClicked(2);
        assertEquals(345, sut.getFilteredEvents().size()); //Comprobamos que estan todos los eventos
        assertEquals(sut.getFilteredEvents(), sut.getCachedEvents());

    }

}
