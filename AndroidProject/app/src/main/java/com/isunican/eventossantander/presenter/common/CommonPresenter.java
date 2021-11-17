package com.isunican.eventossantander.presenter.common;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.comparators.EventsComparatorCategoria;
import com.isunican.eventossantander.model.comparators.EventsComparatorHora;
import com.isunican.eventossantander.view.events.IEventsContract;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommonPresenter implements IEventsContract.Presenter {

    public static void onEventClicked(int eventIndex, List<Event> cachedEvents, IEventsContract.View view) {
        if (eventIndex >= cachedEvents.size() || eventIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        Event event = cachedEvents.get(eventIndex);
        view.openEventDetails(event);
    }

    public static void onInfoClicked(IEventsContract.View view) {
        view.openInfoView();
    }

    public static List<Event> onOrdenarClicked(int tipoOrdenacion, List<Event> eventosEnFiltrosCombinados, List<Event> eventosCacheados) {

        List<Event> eventosConFiltrosCombinados = eventosEnFiltrosCombinados;
        List<Event> cachedEvents = eventosCacheados;

        EventsComparatorCategoria ecc;
        EventsComparatorHora ech;

        switch(tipoOrdenacion){
            case 0:
                ecc = new EventsComparatorCategoria();
                if (eventosConFiltrosCombinados.isEmpty()) {
                    Collections.sort(cachedEvents,ecc);
                    eventosConFiltrosCombinados = cachedEvents;
                } else {
                    Collections.sort(eventosConFiltrosCombinados,ecc);
                }
                break;
            case 1:
                ecc = new EventsComparatorCategoria();
                if (eventosConFiltrosCombinados.isEmpty()) {
                    java.util.Collections.sort(cachedEvents,ecc);
                    Collections.reverse(cachedEvents);
                    eventosConFiltrosCombinados = cachedEvents;
                } else {
                    java.util.Collections.sort(eventosConFiltrosCombinados, ecc);
                    Collections.reverse(eventosConFiltrosCombinados);
                }
                break;
            case 2:
                ech = new EventsComparatorHora();
                if (eventosConFiltrosCombinados.isEmpty()) {
                    Collections.sort(cachedEvents,ech);
                    eventosConFiltrosCombinados = cachedEvents;
                } else {
                    Collections.sort(eventosConFiltrosCombinados,ech);
                }
                break;
            case 3:
                ech = new EventsComparatorHora();
                if (eventosConFiltrosCombinados.isEmpty()) {
                    java.util.Collections.sort(cachedEvents,ech);
                    Collections.reverse(cachedEvents);
                    eventosConFiltrosCombinados = cachedEvents;
                } else {
                    java.util.Collections.sort(eventosConFiltrosCombinados, ech);
                    Collections.reverse(eventosConFiltrosCombinados);
                }
                break;
            default:
                break;
        }
        return eventosConFiltrosCombinados;
    }

    public static List<Event> onFiltrarClicked(List<String> checkboxSeleccionados, List<Event> cachedEvents, List<Event> eventosEnDeterminadosFiltros,
                                        int ordenFiltrado, List<Event> eventosEnDeterminadasFechas) {

        List<Event> filteredEvents = new ArrayList<>();

        for (Event e : cachedEvents) {
            for (String tipo : checkboxSeleccionados) {
                if (e.getCategoria().equals(tipo)) {
                    filteredEvents.add(e);
                }
            }
        }
        if (filteredEvents.isEmpty()) {
            eventosEnDeterminadosFiltros = cachedEvents;
        } else {
            eventosEnDeterminadosFiltros = filteredEvents;
        }
        List<Event> eventosEnFiltrosCombinados = combinaFiltros(eventosEnDeterminadosFiltros, eventosEnDeterminadasFechas);

        if(ordenFiltrado != 2) {
            eventosEnFiltrosCombinados = onOrdenarClicked(ordenFiltrado, eventosEnFiltrosCombinados, cachedEvents);
        }
        return eventosEnFiltrosCombinados;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Event> onFiltrarDate(LocalDate fechaIni, LocalDate fechaFin, List<Event> cachedEvents,
                              List<Event> eventosEnDeterminadosFiltros, IEventsContract.View view) {

        LocalDate fechaEvento;
        List<Event> filteredEvents = new ArrayList<>();
        List<Event> eventosEnDeterminadasFechas = new ArrayList<Event>();
        List<Event> eventosEnFiltrosCombinados = new ArrayList<Event>();

        for (Event e : cachedEvents) {
            String[] date1 = e.getFecha().split(" ");
            String[] dateDefinitive = date1[1].split(",");
            String[] dateSeparada = dateDefinitive[0].split("/");

            int diaEvento = Integer.parseInt(dateSeparada[0]);
            int mesEvento = Integer.parseInt(dateSeparada[1]);
            int anhoEvento = Integer.parseInt(dateSeparada[2]);
            fechaEvento = LocalDate.of(anhoEvento, mesEvento, diaEvento);

            if (fechaEvento.compareTo(fechaIni) >= 0 && fechaEvento.compareTo(fechaFin) <= 0) {
                filteredEvents.add(e);
            }
        }
        if (filteredEvents.isEmpty()) {
            eventosEnDeterminadasFechas = cachedEvents;
            combinaFiltros(eventosEnDeterminadosFiltros, eventosEnDeterminadasFechas);
            view.onLoadNoEventsInDate();
        } else {
            eventosEnDeterminadasFechas = filteredEvents;
            eventosEnFiltrosCombinados = combinaFiltros(eventosEnDeterminadosFiltros, eventosEnDeterminadasFechas);
            view.onLoadSuccess(eventosEnFiltrosCombinados.size());
        }
        view.onEventsLoaded(eventosEnFiltrosCombinados);

        return eventosEnFiltrosCombinados;
    }

    public static List<Event> combinaFiltros(List<Event> eventosEnDeterminadosFiltros, List<Event> eventosEnDeterminadasFechas) {

        List<Event> eventosEnFiltrosCombinados = new ArrayList<>();

        if (eventosEnDeterminadosFiltros.isEmpty()) {
            eventosEnFiltrosCombinados = eventosEnDeterminadasFechas;
        } else if (eventosEnDeterminadasFechas.isEmpty()){
            eventosEnFiltrosCombinados = eventosEnDeterminadosFiltros;
        }

        for(Event i : eventosEnDeterminadosFiltros) {
            for(Event j : eventosEnDeterminadasFechas) {
                if(i == j) {
                    eventosEnFiltrosCombinados.add(j);
                }
            }
        }

        return eventosEnFiltrosCombinados;
    }




}
