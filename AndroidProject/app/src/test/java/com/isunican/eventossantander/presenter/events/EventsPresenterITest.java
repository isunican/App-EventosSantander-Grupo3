package com.isunican.eventossantander.presenter.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

import android.os.Build;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.view.events.IEventsContract;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

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

    private static final Phaser lock = EventsRepository.getAsyncCounter();


    // Objetos
    List<String> listaVacia;
    List<String> listaConElemento;
    List<String> listaLlena;
    List<String> listaErronea;

    @Before
    public void setUp() {

        EventsRepository.setLocalSource();
        lock.arriveAndAwaitAdvance();
    }

    /*
     * Test del método onFiltrarClickedTest
     * @author Adrián García Cubas
     */
    @Test
    public void onFiltrarClickedTest() {

        sut = new EventsPresenter(mockView);
        lock.arriveAndAwaitAdvance();

        //Inicilizamos las listas
        listaVacia = new ArrayList<>();
        listaConElemento = new ArrayList<>();
        listaLlena = new ArrayList<>();
        listaErronea = new ArrayList<>();

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
        assertEquals(345, sut.getEventosEnFiltrosCombinados().size());
        assertEquals(sut.getEventosEnFiltrosCombinados(), (sut.getCachedEvents()));


        // IT.1B: Se comprueba que si la lista de tipos de evento introducida contiene el tipo musica,
        // los eventos filtrados son de tipo música y estan ordenados de manera ascendente
        sut.onOrdenarClicked(0); //Ordenamos ascendentemente los eventos
        sut.onFiltrarClicked(listaConElemento);
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(0).getCategoria());
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(20).getCategoria());
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(45).getCategoria());
        assertEquals(92, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que solo hay eventos de tipo musica

        // IT.1C: Se comprueba que si la lista de tipos de evento introducida contiene
        // todos los tipos de evento, los eventos filtrados sean todos los eventos
        // cacheados menos aquellos que no tengan tipo y ordenados de manera descendente.
        sut.onOrdenarClicked(1); //Ordenamos descendentemente los eventos
        sut.onFiltrarClicked(listaLlena);
        assertEquals(310, sut.getEventosEnFiltrosCombinados().size());//Comprobamos que no estan los eventos sin tipo

        /*
        * Aqui comprobamos con todos los tipos de evento para comprbar que todos funcionan correctamente,
        * despues de estas comprobaciones, se comprobarán solo 4 tipos de eventos para mayor legibilidad.
        */
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(0).getCategoria());
        assertEquals("Online", sut.getEventosEnFiltrosCombinados().get(6).getCategoria());
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(12).getCategoria());
        assertEquals("Infantil", sut.getEventosEnFiltrosCombinados().get(104).getCategoria());
        assertEquals("Fotografía", sut.getEventosEnFiltrosCombinados().get(120).getCategoria());
        assertEquals("Formación/Talleres", sut.getEventosEnFiltrosCombinados().get(127).getCategoria());
        assertEquals("Edición/Literatura", sut.getEventosEnFiltrosCombinados().get(145).getCategoria());
        assertEquals("Cine/Audiovisual", sut.getEventosEnFiltrosCombinados().get(165).getCategoria());
        assertEquals("Artes plásticas", sut.getEventosEnFiltrosCombinados().get(223).getCategoria());
        assertEquals("Arquitectura", sut.getEventosEnFiltrosCombinados().get(301).getCategoria());

        // IT.1D: Se comprueba que si la lista de tipos de evento esta vacia, estarán todos los
        // eventos ordenados de manera ascendente
        sut.onOrdenarClicked(0); //Ordenamos ascendentemente los eventos
        sut.onFiltrarClicked(listaVacia);
        assertEquals(345, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(340).getCategoria());
        assertEquals("Online", sut.getEventosEnFiltrosCombinados().get(334).getCategoria());
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(328).getCategoria());
        assertEquals("Infantil", sut.getEventosEnFiltrosCombinados().get(236).getCategoria());

        // IT.1E: Se comprueba que si la lista contiene un evento no existente, se devuelve
        // la lista de eventos original
        sut.onFiltrarClicked(listaErronea);
        assertEquals(345, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals(sut.getEventosEnFiltrosCombinados(), (sut.getCachedEvents()));

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
        lock.arriveAndAwaitAdvance();
        assertEquals(345, sut.getCachedEvents().size());
        assertEquals(sut.getCachedEvents().get(0).getCategoria(),("Música"));
        assertEquals(sut.getCachedEvents().get(0).getNombre(),("Abierto el plazo de inscripción para el Concurso Internacional de Piano de Santander Paloma O'Shea"));
        assertEquals(sut.getCachedEvents().get(3).getCategoria(),("Artes plásticas"));
        assertEquals(sut.getCachedEvents().get(3).getNombre(),("Gabinete de estampas virtual de la UC"));

        // IT.2B: Se comprueba que cuando no se puede acceder a la base de datos, no se
        // cargan los eventos
        EventsRepository.setFakeSource();
        sut = new EventsPresenter(mockView);
        lock.arriveAndAwaitAdvance();
        assertNull(sut.getCachedEvents());

    }

    /*
     * Test del método onFiltrarClickedTest
     * @author Adrián García Cubas
     */
    @Test
    public void onOrdenarCategoriaClickedTest() {

        sut = new EventsPresenter(mockView);
        lock.arriveAndAwaitAdvance();

        //Inicializamos las listas
        listaVacia = new ArrayList<>();
        listaConElemento = new ArrayList<>();

        //Las rellenamos como correspondan
        listaConElemento.add("Música");
        listaConElemento.add("Otros");

        // IT.2A: Se comprueba que si la lista eventosEnFiltrosCombinados esta ordenada de manera
        // ascendente correctamente con una lista de filtros vacia
        sut.onFiltrarClicked(listaVacia);
        sut.onOrdenarClicked(0);
        assertEquals(345, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(340).getCategoria());
        assertEquals("Online", sut.getEventosEnFiltrosCombinados().get(334).getCategoria());
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(328).getCategoria());
        assertEquals("Infantil", sut.getEventosEnFiltrosCombinados().get(236).getCategoria());

        // IT.2B: Se comprueba que si la lista eventosEnFiltrosCombinados esta ordenada de manera
        // descendente correctamente con una lista de filtros vacia
        sut.onFiltrarClicked(listaVacia);
        sut.onOrdenarClicked(1);
        assertEquals(345, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(0).getCategoria());
        assertEquals("Online", sut.getEventosEnFiltrosCombinados().get(6).getCategoria());
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(12).getCategoria());
        assertEquals("Infantil", sut.getEventosEnFiltrosCombinados().get(104).getCategoria());

        // IT.2C: Se comprueba que si la lista eventosEnFiltrosCombinados esta ordenada de manera
        // ascendente correctamente con una lista de filtros no vacia
        sut.onFiltrarClicked(listaConElemento);
        sut.onOrdenarClicked(0);
        assertEquals(98, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(97).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(96).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(95).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(94).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(93).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(92).getCategoria());
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(91).getCategoria());
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(30).getCategoria());
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(0).getCategoria());

        // IT.2D: Se comprueba que si la lista eventosEnFiltrosCombinados esta ordenada de manera
        // descendente correctamente con una lista de filtros no vacia
        sut.onFiltrarClicked(listaConElemento);
        sut.onOrdenarClicked(1);
        assertEquals(98, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(0).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(1).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(2).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(3).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(4).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(5).getCategoria());
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(6).getCategoria());
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(30).getCategoria());
        assertEquals("Música", sut.getEventosEnFiltrosCombinados().get(91).getCategoria());

        // IT.2E: Se comprueba que si se le pasa un indice distino de 0 o 1 al metodo onOrdenarCategoriaCLicked
        // no se actualiza nada, la lista se queda como si no se hubiese llamado al metodo
        sut.onFiltrarClicked(listaVacia);
        sut.onOrdenarClicked(2);
        assertEquals(345, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals(sut.getEventosEnFiltrosCombinados(), sut.getCachedEvents());

    }

    /*
     * Test del método onOrdenarClicked
     * @author David Moreno Pérez
     */
    @Test
    public void onOrdenarClickedTest(){

        sut = new EventsPresenter(mockView);
        lock.arriveAndAwaitAdvance();

        //Inicializamos las listas
        listaVacia = new ArrayList<>();
        listaConElemento = new ArrayList<>();

        //Las rellenamos como correspondan
        listaConElemento.add("Arquitectura");
        listaConElemento.add("Otros");

        // IT.1A: Se comprueba si la lista eventosEnFiltrosCombinados esta ordenada por categoria
        // ascendente
        sut.onFiltrarClicked(listaConElemento);
        sut.onOrdenarClicked(0);
        assertEquals(15, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals("Arquitectura", sut.getEventosEnFiltrosCombinados().get(0).getCategoria());
        assertEquals("Arquitectura", sut.getEventosEnFiltrosCombinados().get(1).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(11).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(14).getCategoria());


        // IT.1B: Se comprueba si la lista eventosEnFiltrosCombinados esta ordenada por categoria
        // descendentemente
        sut.onFiltrarClicked(listaConElemento);
        sut.onOrdenarClicked(1);
        assertEquals(15, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(0).getCategoria());
        assertEquals("Otros", sut.getEventosEnFiltrosCombinados().get(1).getCategoria());
        assertEquals("Arquitectura", sut.getEventosEnFiltrosCombinados().get(11).getCategoria());
        assertEquals("Arquitectura", sut.getEventosEnFiltrosCombinados().get(14).getCategoria());

        // IT.1C: Se comprueba si la lista eventosEnFiltrosCombinados esta ordenada de manera
        // que los eventos mas proximos a la fecha actual aparezcan primero
        sut.onFiltrarClicked(listaConElemento);
        sut.onOrdenarClicked(2);
        assertEquals(15, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals("Domingo 01/08/2021, de 10:30 a 12:30h. ", sut.getEventosEnFiltrosCombinados().get(0).getFecha());
        assertEquals("Lunes 02/08/2021, a las 0:00h. ", sut.getEventosEnFiltrosCombinados().get(2).getFecha());
        assertEquals("Sábado 31/07/2021, de 10:30 a 12:30h. ", sut.getEventosEnFiltrosCombinados().get(13).getFecha());
        assertEquals("Sábado 31/07/2021, de 11:30 a 14:30h. ", sut.getEventosEnFiltrosCombinados().get(14).getFecha());

        // IT.1D: Se comprueba si la lista eventosEnFiltrosCombinados esta ordenada de manera
        // que los eventos mas lejanos a la fecha actual aparezcan primero.
        sut.onFiltrarClicked(listaConElemento);
        sut.onOrdenarClicked(3);
        assertEquals(15, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals("Sábado 31/07/2021, de 11:30 a 14:30h. ", sut.getEventosEnFiltrosCombinados().get(0).getFecha());
        assertEquals("Sábado 31/07/2021, de 10:30 a 12:30h. ", sut.getEventosEnFiltrosCombinados().get(1).getFecha());
        assertEquals("Viernes 06/08/2021, a las 19:00h. ", sut.getEventosEnFiltrosCombinados().get(9).getFecha());
        assertEquals("Viernes 03/09/2021, de 16:30 a 18:30h. ", sut.getEventosEnFiltrosCombinados().get(11).getFecha());

        // CASOS DE PRUEBA NO VALIDOS

        // IT.1C: Se comprueba si la lista eventosEnFiltrosCombinados(vacia) esta ordenada de manera
        // que los eventos mas proximos a la fecha actual aparezcan primero
        sut.onFiltrarClicked(listaVacia);
        sut.onOrdenarClicked(2);
        assertEquals(345, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals("Domingo 01/08/2021, de 10:30 a 12:30h. ", sut.getEventosEnFiltrosCombinados().get(0).getFecha());
        assertEquals("Domingo 01/08/2021, a las 11:00h. ", sut.getEventosEnFiltrosCombinados().get(1).getFecha());
        assertEquals("Domingo 01/08/2021, a las 12:00h. ", sut.getEventosEnFiltrosCombinados().get(2).getFecha());
        assertEquals("Domingo 01/08/2021, de 15:00 a 01:00h. ", sut.getEventosEnFiltrosCombinados().get(3).getFecha());

        // IT.1D: Se comprueba si la lista eventosEnFiltrosCombinados(vacia) esta ordenada de manera
        // que los eventos mas lejanos a la fecha actual aparezcan primero
        sut.onFiltrarClicked(listaVacia);
        sut.onOrdenarClicked(3);
        assertEquals(345, sut.getEventosEnFiltrosCombinados().size()); //Comprobamos que estan todos los eventos
        assertEquals("Martes 31/08/2021, a las 19:00h. ", sut.getEventosEnFiltrosCombinados().get(0).getFecha());
        assertEquals("Martes 31/08/2021, a las 19:00h. ", sut.getEventosEnFiltrosCombinados().get(1).getFecha());
        assertEquals("Martes 31/08/2021, a las 17:00h. ", sut.getEventosEnFiltrosCombinados().get(2).getFecha());
        assertEquals("Sábado 31/07/2021, a las 23:00h. ", sut.getEventosEnFiltrosCombinados().get(3).getFecha());

        // IT.2E: Se comprueba que si se le pasa un indice distino de 0 o 1 al metodo onOrdenarCategoriaCLicked
        // no se actualiza nada, la lista se queda como si no se hubiese llamado al metodo
        sut.onOrdenarClicked(-1);

    }


    /*
     * Test del método onFiltrarDate
     * @author Juan Vélez Velasco
     */
    @Test
    public void onFiltrarDateTest() {

        sut = new EventsPresenter(mockView);
        lock.arriveAndAwaitAdvance();

        ///////////////////
        // IT.1A: Se comprueba que se actualiza la lista filteredEvents conteniendo los eventos entre fechaInicio < fechaFin
        ///////////////////
        LocalDate dateIni = LocalDate.of(2021, 6, 1);
        LocalDate dateFin = LocalDate.of(2021, 8, 2);
        //Se introduce uan fecha válida
        sut.onFiltrarDate(dateIni, dateFin);
        List<Event> listaFiltrada = sut.getEventosEnFiltrosCombinados();
        assertEquals(134, listaFiltrada.size());


        ///////////////////
        // IT.1B: Se comprueba que se actualiza la lista filteredEvents conteniendo los eventos entre fechaInicio == fechaFin
        ///////////////////

        dateIni = LocalDate.of(2021, 8, 2);
        dateFin = LocalDate.of(2021, 8, 2);
        //Se introduce uan fecha válida
        sut.onFiltrarDate(dateIni, dateFin);
        listaFiltrada = sut.getEventosEnFiltrosCombinados();
        assertEquals(16, listaFiltrada.size());
        ///////////////////
        // IT.1C: Se comprueba que no se actualiza la lista filteredEvents porque fechaInicio > fechaFin
        ///////////////////

        dateIni = LocalDate.of(2021, 7, 2);
        dateFin = LocalDate.of(2021, 7, 1);
        //Se introduce uan fecha inválida
        sut.onFiltrarDate(dateIni, dateFin);
        listaFiltrada = sut.getEventosEnFiltrosCombinados();
        assertEquals(345, listaFiltrada.size());


        ///////////////////
        // IT.1D: Se comprueba que se tira una excepción al ser la fechaIni == null o fechaFin == null
        ///////////////////

        dateIni = LocalDate.of(2021, 7, 1);
        dateFin = LocalDate.of(2021, 7, 2);
        //Se introduce uan fecha inválida
        try {
            sut.onFiltrarDate(null, dateFin);
            fail("No se ha lanzado la excepción NullPointerException");
        } catch(NullPointerException e) {
            assertTrue(true);
        }

        try {
            sut.onFiltrarDate(dateIni, null);
            fail("No se ha lanzado la excepción NullPointerException");
        } catch(NullPointerException e) {
            assertTrue(true);
        }
    }


    /*
     * Test del método onEventClicked
     * @author Juan Vélez Velasco
     */
    @Captor
    ArgumentCaptor<Event> captor;
    @Test
    public void onEventClickedTest() {

        sut = new EventsPresenter(mockView);
        lock.arriveAndAwaitAdvance();
        List<Event> listaOriginal = sut.getCachedEvents();

        ///////////////////
        // IT.2A: Se comprueba que se llama al método openEventDetails de la vista con el evento seleccionado
        ///////////////////

        sut.onEventClicked(0);
        verify(mockView).openEventDetails(captor.capture());
        assertEquals(captor.getValue(), listaOriginal.get(0));
        ///////////////////
        // IT.2B: Se comprueba al introducir un indice < 0 o > que el número de eventos se lanza la excepcion indexOutOfBounds
        ///////////////////
        try {
            sut.onEventClicked(-1);
            sut.onEventClicked(listaOriginal.size()+1);
            fail("No se ha cazado la excepcion");
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    /*
     * Test del método combinaFiltros()
     * @author Juan Vélez Velasco
     */
    @Test
    public void onCombinaFiltros() {

        sut = new EventsPresenter(mockView);
        lock.arriveAndAwaitAdvance();

        // IT.2A: Se comprueba que se ha combinado correctamente con las lista de fechas vacia.
        List<Event> listaLlena  = sut.getCachedEvents();
        List<Event> listaVacia = new ArrayList<>();

        sut.setEventosEnDeterminadosFiltros(listaLlena);
        sut.setEventosEnDeterminadasFechas(listaVacia);

        sut.combinaFiltros();
        List<Event> listaResultante = sut.getEventosEnFiltrosCombinados();
        assertEquals(listaResultante.size(),listaLlena.size());


        // IT.3A: Se comprueba que se ha combinado correctamente con las lista de filtros vacia.

        sut.setEventosEnDeterminadosFiltros(listaVacia);
        sut.setEventosEnDeterminadasFechas(listaLlena);

        sut.combinaFiltros();
        listaResultante = sut.getEventosEnFiltrosCombinados();
        assertEquals(listaResultante.size(),listaLlena.size());


        ///////////////////
        // IT.4A: Se comprueba que se ha combinado correctamente con ambas listas vacias.
        /////////////////////

        List<Event> l1 = listaLlena.subList(0,10);
        List<Event> l2 = listaLlena.subList(5,10);
        sut.setEventosEnDeterminadosFiltros(l1);
        sut.setEventosEnDeterminadasFechas(l2);
        sut.combinaFiltros();
        listaResultante = sut.getEventosEnFiltrosCombinados();
        assertEquals(5, listaResultante.size());

        //IT.1A: Con las dos listas vacias no se actualiza nada
        List<Event> l3 = new ArrayList<>();
        List<Event> l4 = new ArrayList<>();
        sut.setEventosEnDeterminadosFiltros(l3);
        sut.setEventosEnDeterminadasFechas(l4);
        sut.combinaFiltros();
        assertEquals(0, sut.getEventosEnFiltrosCombinados().size());

        //IT.1B: Lista filtros nula tira NullPointerException
        List<Event> l6 = new ArrayList<>();
        sut.setEventosEnDeterminadosFiltros(null);
        sut.setEventosEnDeterminadasFechas(l6);
        try {
            sut.combinaFiltros();
            fail("No se lanza excepcion");
        } catch (NullPointerException e) {
            assertTrue(true);
        }

        //IT.2B: Lista fechas nula tira NullPointerException
        List<Event> l7 = new ArrayList<>();
        sut.setEventosEnDeterminadosFiltros(l7);
        sut.setEventosEnDeterminadasFechas(null);
        try {
            sut.combinaFiltros();
            fail("No se lanza excepcion");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
}
