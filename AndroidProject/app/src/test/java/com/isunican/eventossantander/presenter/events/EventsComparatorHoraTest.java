package com.isunican.eventossantander.presenter.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.comparators.EventsComparatorHora;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class EventsComparatorHoraTest {

    // Creación del sut
    private EventsComparatorHora sut;

    @Before
    public void setUp() {
        sut = new EventsComparatorHora();

    }

    /*
     * Test del método compare
     * @author David Moreno Pérez
     */
    @Test
    public void onCompareTest() {

        int result;
        Event e1 = new Event();
        e1.setFecha("Lunes 02/08/2021, a las 0:00h. ");
        Event e2 = new Event();
        e2.setFecha("Domingo 01/08/2021, de 10:30 a 12:30h. ");
        Event e3 = new Event();
        e3.setFecha("Lunes 02/08/2021, a las 0:00h. ");

        //IT.2A Fecha evento1 superior a Fecha evento2
        result = sut.compare(e1, e2);
        assertTrue(result > 0);

        //IT.2B Fecha evento2 inferior a Fecha evento1
        result = sut.compare(e2, e1);
        assertTrue(result < 0);

        //IT.2C Fechas de eventos iguales
        result = sut.compare(e1, e3);
        assertEquals(0,  result);

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

    /*
     * Test del método cortarHoraDeComienzo(String fecha)
     * @author Sergio Gallego Álvarez
     */
    @Test
    public void onCortarHoraDeComienzoTest() {

        //IT.1A fecha = null
        assertEquals("23:59", sut.cortarHoraDeComienzo(null));

        //IT.1B fecha sin hora
        assertEquals("00:00", sut.cortarHoraDeComienzo("Jueves 18/11/2021"));

        //IT.1C fecha con hora
        assertEquals("10:00", sut.cortarHoraDeComienzo("Jueves 18/11/2021, de 10:00 a 13:00h."));
    }
}
