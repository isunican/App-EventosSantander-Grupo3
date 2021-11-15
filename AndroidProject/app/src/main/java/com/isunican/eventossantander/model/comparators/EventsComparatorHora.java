package com.isunican.eventossantander.model.comparators;

import android.util.Log;

import com.isunican.eventossantander.model.Event;

import java.util.Comparator;

public class EventsComparatorHora implements Comparator<Event> {

    @Override
    public int compare(Event e1, Event e2) {
        int comparator = cutDate(e1.getFecha()).compareToIgnoreCase(cutDate(e2.getFecha()));
        if (comparator == 0) {
            return (cortarHoraDeComienzo(e1.getFecha()).compareToIgnoreCase(cortarHoraDeComienzo(e2.getFecha())));
        }
        return comparator;
    }

    public static String cortarHoraDeComienzo(String fecha){

        if (fecha == null) {
            return "23:59";
        }

        int index = fecha.indexOf(":");

        if (index == -1) {
            return "00:00";
        }

        String hora = "";
        hora = hora.concat(String.valueOf(fecha.charAt(index - 2)));
        hora = hora.concat(String.valueOf(fecha.charAt(index - 1)));
        hora = hora.concat(String.valueOf(fecha.charAt(index)));
        hora = hora.concat(String.valueOf(fecha.charAt(index + 1)));
        hora = hora.concat(String.valueOf(fecha.charAt(index + 2)));
        return hora;
    }

    private String cutDate(String fecha) {
        String[] date1 = fecha.split(" ");
        String[] dateDefinitive = date1[1].split(",");
        return dateDefinitive[0];
    }
}