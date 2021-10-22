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
            Collections.sort(cachedEvents,ecc);
            cachedEventsOrdenados = cachedEvents;
            for(Event e: cachedEventsOrdenados) {
                System.out.println("Categoria: "+e.getCategoria());
            }
            view.onEventsLoaded(cachedEventsOrdenados);
        } else if(tipoOrdenacion == 1) { //descendente
            EventsComparatorCategoria ecc = new EventsComparatorCategoria();
            java.util.Collections.sort(cachedEvents,ecc);
            Collections.reverse(cachedEvents);
            cachedEventsOrdenados = cachedEvents;
            view.onEventsLoaded(cachedEventsOrdenados);
        }
    }

    @Override
    public void onFiltrarClicked(List<String> checkboxSeleccionados){
        filteredEvents = new ArrayList<>();
        for (Event e : cachedEvents){
            for (String tipo : checkboxSeleccionados){
                if (e.getCategoria().equals(tipo)){
                    filteredEvents.add(e);
                }
            }
        }

        if (filteredEvents.size() == 0){
            filteredEvents = cachedEvents;
        }

        view.onEventsLoaded(filteredEvents);
        view.onLoadSuccess(filteredEvents.size());

    }

    public List<Event> getCachedEventsOrdenados() {
        return cachedEventsOrdenados;

    }

    public List<Event> getFilteredEvents() {
        return filteredEvents;
    }

    public List<Event> getCachedEvents() {
        return cachedEvents;
    }
}
