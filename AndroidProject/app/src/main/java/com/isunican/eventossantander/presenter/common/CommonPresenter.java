package com.isunican.eventossantander.presenter.common;


import androidx.annotation.NonNull;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.comparators.EventsComparatorCategoria;
import com.isunican.eventossantander.model.comparators.EventsComparatorHora;
import com.isunican.eventossantander.view.events.IEventsContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommonPresenter implements IEventsContract.Presenter {

    private CommonPresenter() {
        throw new IllegalStateException("Utility class");
    }

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

    public static List<Event> onFiltrarClicked(List<String> checkboxSeleccionados, @NonNull List<Event> cachedEvents,
                                               int ordenFiltrado, List<Event> eventosEnDeterminadasFechas) {

        List<Event> filteredEvents = new ArrayList<>();
        List<Event> eventosEnDeterminadosFiltros;

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
