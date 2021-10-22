package com.isunican.eventossantander;

import static org.junit.Assert.assertTrue;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.events.EventsActivity;
import com.isunican.eventossantander.view.events.IEventsContract;
import com.isunican.eventossantander.presenter.events.EventsPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
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
public class EventsPresenterITest {

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

    }


    /*
     * Test del método onFiltrarClickedTest
     * @author Álvaro López Alonso
     */
    @Test
    public void onFiltrarClickedTest() throws InterruptedException {

        // IT.1A: Se comprueba que si la lista de tipos de evento introducida contiene un
        // tipo de evento, los eventos filtrados corresponderán solo a los del tipo de evento seleccionado.
        sut.onFiltrarClicked(listaConElemento);
        assertTrue(sut.getFilteredEvents().size() == 92);
        assertTrue(sut.getFilteredEvents().get(5).getCategoria().equals("Música"));
        assertTrue(sut.getFilteredEvents().get(24).getCategoria().equals("Música"));
        assertTrue(sut.getFilteredEvents().get(68).getCategoria().equals("Música"));

        // IT.1B: Se comprueba que si la lista de tipos de evento introducida está vacía,
        // los eventos filtrados sean igual a los eventos cacheados.
        sut.onFiltrarClicked(listaVacia);
        assertTrue(sut.getFilteredEvents().equals(sut.getCachedEvents()));
        assertTrue(sut.getFilteredEvents().size() == 345);

        // IT.1C: Se comprueba que si la lista de tipos de evento introducida contiene
        // todos los tipos de evento, los eventos filtrados sean todos los eventos
        // caheados menos aquellos que no tengan tipo.
        sut.onFiltrarClicked(listaLlena);
        assertTrue(sut.getFilteredEvents().size() == 310);

        // IT.1D: Se comprueba que si la lista introducida contiene un tipo de evento
        // no existente, los eventos filtrados sean igual a los eventos cacheados.
        sut.onFiltrarClicked(listaErronea);
        assertTrue(sut.getFilteredEvents().equals(sut.getCachedEvents()));
        assertTrue(sut.getFilteredEvents().size() == 345);

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
        assertTrue(sut.getCachedEvents().size() == 345);
        assertTrue(sut.getCachedEvents().get(0).getCategoria().equals("Música"));
        assertTrue(sut.getCachedEvents().get(0).getNombre().equals("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea"));
        assertTrue(sut.getCachedEvents().get(3).getCategoria().equals("Artes plásticas"));
        assertTrue(sut.getCachedEvents().get(3).getNombre().equals("Gabinete de estampas virtual de la UC"));

        // IT.2B: Se comprueba que cuando no se puede acceder a la base de datos, no se
        // cargan los eventos
        EventsRepository.setFakeSource();
        sut = new EventsPresenter(mockView);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(sut.getCachedEvents() == null);

    }

}
