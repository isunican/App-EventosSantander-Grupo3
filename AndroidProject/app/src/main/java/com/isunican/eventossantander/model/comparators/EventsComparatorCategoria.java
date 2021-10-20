package com.isunican.eventossantander.model.comparators;

import android.util.Log;

import com.isunican.eventossantander.model.Event;

import java.util.Comparator;

public class EventsComparatorCategoria implements Comparator<Event> {

    @Override
    public int compare(Event e1, Event e2) {
        //si quiere orden descendente se hace Collections.sort(colours, Collections.reverseOrder())
        // despues de llamar a java.util.Collections.sort(arrayList,icc)
        //System.out.println(e1.getCategoria());
        return (e1.getCategoria().compareToIgnoreCase(e2.getCategoria()));
    }
}

