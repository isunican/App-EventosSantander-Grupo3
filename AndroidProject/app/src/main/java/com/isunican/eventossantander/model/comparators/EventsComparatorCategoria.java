package com.isunican.eventossantander.model.comparators;

import com.isunican.eventossantander.model.Event;

import java.util.Comparator;

public class EventsComparatorCategoria implements Comparator<Event> {

    @Override
    public int compare(Event e1, Event e2) {
        return (e1.getCategoria().compareToIgnoreCase(e2.getCategoria()));
    }
}

