package com.isunican.eventossantander.presenter.events;

import static org.junit.Assert.assertEquals;

import android.os.Build;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.presenter.today.TodayEventsPresenter;
import com.isunican.eventossantander.view.events.IEventsContract;
import com.isunican.eventossantander.view.today.ITodayEventsContract;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class TodayEventsPresenterITest {
    // Creación del sut
    private TodayEventsPresenter sut;

    // Mocks
    @Mock
    ITodayEventsContract.View mockView;
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
     * @author Sergio Pérez Landaburu
     */
    @Test
    public void onEventosHoyTest(){

        sut = new TodayEventsPresenter(mockView);
        lock.arriveAndAwaitAdvance();
        List<Event> eventosHoyTest = sut.eventosHoy();

        String[] date1Test = eventosHoyTest.get(0).getFecha().split(" ");
        String[] dateDefinitiveTest = date1Test[1].split(",");
        String[] dateSeparadaTest = dateDefinitiveTest[0].split("/");

        int diaEvento = Integer.parseInt(dateSeparadaTest[0]);
        int mesEvento = Integer.parseInt(dateSeparadaTest[1]);
        int anhoEvento = Integer.parseInt(dateSeparadaTest[2]);

        LocalDate fechaEventoTest = LocalDate.of(anhoEvento, mesEvento, diaEvento);

        assertEquals(fechaEventoTest, LocalDate.now());
    }
}
