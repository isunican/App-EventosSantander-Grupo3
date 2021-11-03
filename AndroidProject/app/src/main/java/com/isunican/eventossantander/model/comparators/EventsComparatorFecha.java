package com.isunican.eventossantander.model.comparators;

import java.util.Comparator;
import java.util.Date;

public class EventsComparatorFecha implements Comparator<Date> {
    @Override
    public int compare(Date d1, Date d2) {
        return (d1.compareTo(d2));
    }
}

