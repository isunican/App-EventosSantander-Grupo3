package com.isunican.eventossantander.presenter.events;


import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.model.comparators.EventsComparatorCategoria;
import com.isunican.eventossantander.view.Listener;
import com.isunican.eventossantander.view.events.IEventsContract;

import java.util.Collections;

import java.util.ArrayList;

import java.util.List;

public class EventsPresenter implements IEventsContract.Presenter {

    private final IEventsContract.View view;
    private List<Event> cachedEvents;
    private List<Event> cachedEventsOrdenados;
    private List<Event> cachedEventsOriginal;
    private List<Event> filteredEvents;
    private List<Event> filteredEventsCopy;
    private String fechaIni;
    private String fechaFin;

    public EventsPresenter(IEventsContract.View view) {
        this.view = view;
        loadData();
    }

    private void loadData() {
        EventsRepository.getEvents(new Listener<List<Event>>() {
            @Override
            public void onSuccess(List<Event> data) {
                view.onEventsLoaded(data);
                view.onLoadSuccess(data.size());
                cachedEvents = data;
                cachedEventsOriginal = cachedEvents;
                filteredEventsCopy = new ArrayList<>();
            }

            @Override
            public void onFailure() {
                view.onLoadError();
                cachedEvents = null;
            }
        });
    }

    @Override
    public void onEventClicked(int eventIndex) {
        if (cachedEvents != null && eventIndex < cachedEvents.size()) {
            Event event = cachedEvents.get(eventIndex);
            view.openEventDetails(event);
        }
    }

    @Override
    public void onReloadClicked() {
        loadData();
    }

    @Override
    public void onInfoClicked() {
        view.openInfoView();
    }

    @Override
    public void onOrdenarCategoriaClicked(int tipoOrdenacion) {
        if (tipoOrdenacion == 0) { //ascendente
            EventsComparatorCategoria ecc = new EventsComparatorCategoria();
            if (filteredEventsCopy.isEmpty()) {
                Collections.sort(cachedEvents,ecc);
                cachedEventsOrdenados = cachedEvents;
            } else {
                Collections.sort(filteredEventsCopy,ecc);
                cachedEventsOrdenados = filteredEventsCopy;
            }
            view.onEventsLoaded(cachedEventsOrdenados);
        } else if(tipoOrdenacion == 1) { //descendente
            EventsComparatorCategoria ecc = new EventsComparatorCategoria();
            if (filteredEventsCopy.isEmpty()) {
                java.util.Collections.sort(cachedEvents,ecc);
                Collections.reverse(cachedEvents);
                cachedEventsOrdenados = cachedEvents;
            } else {
                java.util.Collections.sort(filteredEventsCopy,ecc);
                Collections.reverse(filteredEventsCopy);
                cachedEventsOrdenados = filteredEventsCopy;
            }
            view.onEventsLoaded(cachedEventsOrdenados);
        }
    }

    @Override
    public void onFiltrarClicked(List<String> checkboxSeleccionados) {
        filteredEvents = new ArrayList<>();
        for (Event e : cachedEvents){
            for (String tipo : checkboxSeleccionados){
                if (e.getCategoria().equals(tipo)){
                    filteredEvents.add(e);
                }
            }
        }
        if (filteredEvents.isEmpty()){
            filteredEvents = cachedEvents;
        }
        view.onEventsLoaded(filteredEvents);
        view.onLoadSuccess(filteredEvents.size());
        filteredEventsCopy = filteredEvents;
    }

        public List<Event> getCachedEventsOrdenados() {
        return cachedEventsOrdenados;

    }

    @Override
    public void onFiltrarDate(int diaInicio, int mesInicio, int anhoInicio, int diaFin, int mesFin, int anhoFin) {
        //TODO: realizar el filtrado de la lista en base a las fechas de inicio y fin proporcionadas por parametros
        fechaIni = (diaInicio + "/" + (mesInicio) + "/" + anhoInicio);
        fechaFin = (diaFin + "/" + (mesFin) + "/" + anhoFin);
        if(filteredEventsCopy.isEmpty()) {
            filteredEvents = new ArrayList<>();
            for (Event e : cachedEvents) {
                if (cutDate(e.getFecha()).equals(fechaIni)) {
                    filteredEvents.add(e);
                } else if (cutDate(e.getFecha()).equals(fechaFin)) {
                    filteredEvents.add(e);
                }
            }
            if (filteredEvents.isEmpty()) {
                filteredEvents = cachedEvents;
            }
        }else{
            filteredEvents = new ArrayList<>();
            for (Event e : filteredEventsCopy) {
                if (cutDate(e.getFecha()).equals(fechaIni)) {
                    filteredEvents.add(e);
                } else if (cutDate(e.getFecha()).equals(fechaFin)) {
                    filteredEvents.add(e);
                }
            }
            if (filteredEvents.isEmpty()) {
                filteredEvents = filteredEventsCopy;
            }
        }
        view.onEventsLoaded(filteredEvents);
        view.onLoadSuccess(filteredEvents.size());
        filteredEventsCopy = filteredEvents;
    }

    public List<Event> getFilteredEvents() {
        return filteredEvents;
    }

    public List<Event> getCachedEvents() {
        return cachedEvents;
    }
    private String cutDate(String fecha) {
        String[] date1 = fecha.split(" ");
        String[] dateDefinitive = date1[1].split(",");
        return dateDefinitive[0];
    }
}
