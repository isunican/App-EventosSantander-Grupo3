package com.isunican.eventossantander.model.comparators;

import com.isunican.eventossantander.model.Event;

import java.util.Comparator;

public class EventsComparatorCategoria implements Comparator<Event> {

    @Override
    public int compare(Event e1, Event e2) {
        //si quiere orden descendente se hace Collections.sort(colours, Collections.reverseOrder())
        // despues de llamar a java.util.Collections.sort(arrayList,icc)
        return (e1.getCategoria().compareTo(e2.getCategoria()));
    }
}

