package com.isunican.eventossantander.presenter.events;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.os.Build;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.model.comparators.EventsComparatorHora;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.Phaser;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class EventsComparatorHoraITest {

    // Creación del sut
    private EventsComparatorHora sut;

    // Mocks
    //@Mock
    //IEventsContract.View mockView;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private static final Phaser lock = EventsRepository.getAsyncCounter();

    @Before
    public void setUp() {

       EventsRepository.setLocalSource();
       lock.arriveAndAwaitAdvance();
    }

    /*
     * Test del método compare
     * @author David Moreno Pérez
     */
    @Test
    public void onCompareTest() {

        sut = new EventsComparatorHora();
        lock.arriveAndAwaitAdvance();

        Event e1 = new Event();
        e1.setFecha("Lunes 02/08/2021, a las 0:00h. ");
        Event e2 = new Event();
        e2.setFecha("Domingo 01/08/2021, de 10:30 a 12:30h. ");
        Event e3 = new Event();
        e3.setFecha("Lunes 02/08/2021, a las 0:00h. ");
        int result;

        //IT.2A Fecha evento1 > Fecha evento2
        result = sut.compare(e1, e2);
        assertTrue(result > 0);

        //IT.2B Fecha evento1 < Fecha evento2
        result = sut.compare(e2, e1);
        assertTrue(result < 0);

        //IT.2C Fechas de eventos iguales
        result = sut.compare(e1, e3);
        assertTrue(result == 0);

        //IT.2D Evento e1=null
        try{
            sut.compare(null, e2);
            fail("No se ha lanzado la excepción NullPointerException");
        }catch (NullPointerException e){}


        //IT.2E Evento e2=null
        try{
            sut.compare(e1, null);
            fail("No se ha lanzado la excepción NullPointerException");
        }catch (NullPointerException e){}
    }
}
