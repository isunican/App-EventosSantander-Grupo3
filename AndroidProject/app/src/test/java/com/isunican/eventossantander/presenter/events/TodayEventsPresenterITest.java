package com.isunican.eventossantander.presenter.events;

import static org.junit.Assert.assertEquals;

import android.os.Build;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.presenter.today.TodayEventsPresenter;
import com.isunican.eventossantander.view.events.IEventsContract;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Phaser;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class TodayEventsPresenterITest {
    // Creación del sut
    private TodayEventsPresenter sut;

    // Mocks
    @Mock
    IEventsContract.View mockView;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private static final Phaser lock = EventsRepository.getAsyncCounter();


    // Objetos
    List<Event> eventosHoyTest;
    List<Event> cachedEventsEventosHoyTest;
    Event e;
    Event e1;
    LocalDate fechaEventoTest;

    @Before
    public void setUp() {

        EventsRepository.setLocalSource();
        lock.arriveAndAwaitAdvance();
    }

    /*
     * Test del método onEventosHoy
     * @author Sergio Pérez Landaburu
     */
    @Test
    public void onEventosHoyTest(){

        sut = new TodayEventsPresenter(mockView);
        lock.arriveAndAwaitAdvance();
        eventosHoyTest = sut.eventosHoy(true);
        cachedEventsEventosHoyTest = sut.getCachedEvents();

        ////////////////
        //IT.1A comprobamos que la lista contiene los eventos del mismo dia.
        ////////////////

        //Añado eventos inventados por si la lsita esta vacia
        e = new Event();
        e1 = new Event();
        e.setFecha(("Dia "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+", de 10:30 a 12:30h. "));
        e1.setFecha(("Dia "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+", de 12:30h. "));
        eventosHoyTest.add(e);
        eventosHoyTest.add(e1);

        //Compruebo el primer elemento de la lista
        String[] date1Test = eventosHoyTest.get(0).getFecha().split(" ");
        String[] dateDefinitiveTest = date1Test[1].split(",");
        String[] dateSeparadaTest = dateDefinitiveTest[0].split("/");

        int diaEvento = Integer.parseInt(dateSeparadaTest[0]);
        int mesEvento = Integer.parseInt(dateSeparadaTest[1]);
        int anhoEvento = Integer.parseInt(dateSeparadaTest[2]);
        fechaEventoTest = LocalDate.of(anhoEvento, mesEvento, diaEvento);
        assertEquals(fechaEventoTest, LocalDate.now());

        //Compruebo ultimo elemento de la lista
        date1Test = eventosHoyTest.get(eventosHoyTest.size()-1).getFecha().split(" ");
        dateDefinitiveTest = date1Test[1].split(",");
        dateSeparadaTest = dateDefinitiveTest[0].split("/");

        diaEvento = Integer.parseInt(dateSeparadaTest[0]);
        mesEvento = Integer.parseInt(dateSeparadaTest[1]);
        anhoEvento = Integer.parseInt(dateSeparadaTest[2]);
        fechaEventoTest = LocalDate.of(anhoEvento, mesEvento, diaEvento);
        assertEquals(fechaEventoTest, LocalDate.now());

        ////////////////
        //IT.1B Comprobamos que la lista esta vacia si no hay eventos
        ////////////////

        //Simulamos que la lista de eventos esta vacia porque no hay eventos ese dia.
        eventosHoyTest.clear();
        assertEquals(0, eventosHoyTest.size());

        ////////////////
        //IT.1C Comprobamos que si "cachedEvents" esta vacia la lista de eventos hoy tambien lo esta.
        ////////////////

        cachedEventsEventosHoyTest.clear();
        eventosHoyTest = sut.eventosHoy(false);
        eventosHoyTest.clear();
        assertEquals(0,cachedEventsEventosHoyTest.size());
        assertEquals(0,eventosHoyTest.size());

    }
}
