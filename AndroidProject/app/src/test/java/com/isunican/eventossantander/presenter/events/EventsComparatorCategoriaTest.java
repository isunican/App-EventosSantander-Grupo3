package com.isunican.eventossantander.presenter.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.comparators.EventsComparatorCategoria;

import org.junit.Before;
import org.junit.Test;

public class EventsComparatorCategoriaTest {

    // Creación del sut
    private EventsComparatorCategoria sut;

    @Before
    public void setUp() {
        sut = new EventsComparatorCategoria();
    }


    /*
     * Test del método compare
     * @author David Moreno Pérez
     */
    @Test
    public void onCompareTest() {

        int result;
        Event e1 = new Event();
        e1.setCategoria("Online");
        Event e2 = new Event();
        e2.setCategoria("Arquitectura");
        Event e3 = new Event();
        e3.setCategoria("Online");

        //IT.2A Categoria evento1 superior a Categoria evento2
        result = sut.compare(e1, e2);
        assertTrue(result > 0);

        //IT.2B Categoria evento2 inferior a Categoria evento1
        result = sut.compare(e2, e1);
        assertTrue(result < 0);

        //IT.2C Categorias de eventos iguales
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
}
