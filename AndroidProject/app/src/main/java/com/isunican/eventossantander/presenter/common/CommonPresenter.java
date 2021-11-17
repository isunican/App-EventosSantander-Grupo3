package com.isunican.eventossantander.presenter.common;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.comparators.EventsComparatorCategoria;
import com.isunican.eventossantander.model.comparators.EventsComparatorHora;
import com.isunican.eventossantander.view.events.IEventsContract;

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

    public static List<Event> onOrdenarClicked(int tipoOrdenacion, List<Event> eefc, List<Event> ce) {

        List<Event> eventosEnFiltrosCombinados = eefc;
        List<Event> cachedEvents = ce;

        EventsComparatorCategoria ecc;
        EventsComparatorHora ech;

        switch(tipoOrdenacion){
            case 0:
                ecc = new EventsComparatorCategoria();
                if (eventosEnFiltrosCombinados.isEmpty()) {
                    Collections.sort(cachedEvents,ecc);
                    eventosEnFiltrosCombinados = cachedEvents;
                } else {
                    Collections.sort(eventosEnFiltrosCombinados,ecc);
                }
                break;
            case 1:
                ecc = new EventsComparatorCategoria();
                if (eventosEnFiltrosCombinados.isEmpty()) {
                    java.util.Collections.sort(cachedEvents,ecc);
                    Collections.reverse(cachedEvents);
                    eventosEnFiltrosCombinados = cachedEvents;
                } else {
                    java.util.Collections.sort(eventosEnFiltrosCombinados, ecc);
                    Collections.reverse(eventosEnFiltrosCombinados);
                }
                break;
            case 2:
                ech = new EventsComparatorHora();
                if (eventosEnFiltrosCombinados.isEmpty()) {
                    Collections.sort(cachedEvents,ech);
                    eventosEnFiltrosCombinados = cachedEvents;
                } else {
                    Collections.sort(eventosEnFiltrosCombinados,ech);
                }
                break;
            case 3:
                ech = new EventsComparatorHora();
                if (eventosEnFiltrosCombinados.isEmpty()) {
                    java.util.Collections.sort(cachedEvents,ech);
                    Collections.reverse(cachedEvents);
                    eventosEnFiltrosCombinados = cachedEvents;
                } else {
                    java.util.Collections.sort(eventosEnFiltrosCombinados, ech);
                    Collections.reverse(eventosEnFiltrosCombinados);
                }
                break;
            default:
                break;
        }
        return eventosEnFiltrosCombinados;
    }


}
