package com.isunican.eventossantander.model.comparators;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isunican.eventossantander.model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;

public class EventsComparatorHora implements Comparator<Event> {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compare(Event e1, Event e2) {
        int comparator = LocalDateParser(cutDate(e1.getFecha())).compareTo(LocalDateParser(cutDate(e2.getFecha())));
        if (comparator == 0) {
            //return (cortarHoraDeComienzo(e1.getFecha()).compareToIgnoreCase(cortarHoraDeComienzo(e2.getFecha())));
            return LocalTimeParser(cortarHoraDeComienzo(e1.getFecha())).compareTo(LocalTimeParser(cortarHoraDeComienzo(e2.getFecha())));
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

    private LocalDate LocalDateParser (String date) {
        String [] fechaSeparada = date.split("/");
        int dia = Integer.parseInt(fechaSeparada[0]);
        int mes = Integer.parseInt(fechaSeparada[1]);
        int anho = Integer.parseInt(fechaSeparada[2]);
        return LocalDate.of(anho,mes,dia);
    }

    private LocalTime LocalTimeParser (String time) {
        String [] tiempoSeparado = time.split(":");
        int hora;
        if (tiempoSeparado[0].indexOf(" ") != -1) {
            hora = Integer.parseInt(tiempoSeparado[0].substring(1));
        } else {
            hora = Integer.parseInt(tiempoSeparado[0]);
        }
        int minuto = Integer.parseInt(tiempoSeparado[1]);
        return LocalTime.of(hora, minuto);
    }

}