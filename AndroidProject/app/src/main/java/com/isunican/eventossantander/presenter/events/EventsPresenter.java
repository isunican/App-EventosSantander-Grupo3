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
    private List<Event> filteredEvents;
    private List<Event> filteredEventsCopy;
    private List<Event> filteredEventsCopyDate;

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
                filteredEventsCopy = new ArrayList<>();
                filteredEventsCopyDate = new ArrayList<>();
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
        if(filteredEventsCopyDate.isEmpty()) {
            for (Event e : cachedEvents) {
                for (String tipo : checkboxSeleccionados) {
                    if (e.getCategoria().equals(tipo)) {
                        filteredEvents.add(e);
                    }
                }
            }
            if (filteredEvents.isEmpty()) {
                filteredEvents = cachedEvents;
            }
        }else{
            for (Event e : filteredEventsCopyDate) {
                for (String tipo : checkboxSeleccionados) {
                    if (e.getCategoria().equals(tipo)) {
                        filteredEvents.add(e);
                    }
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

        public List<Event> getCachedEventsOrdenados() {
        return cachedEventsOrdenados;

    }

    @Override
    public void onFiltrarDate(int diaInicio, int mesInicio, int anhoInicio, int diaFin, int mesFin, int anhoFin) {
        filteredEvents = new ArrayList<>();

        if(filteredEventsCopy.isEmpty()) {
            for (Event e : cachedEvents) {

                if (dateCompare(e.getFecha(), diaInicio, mesInicio, anhoInicio, true) &&
                        dateCompare(e.getFecha(), diaFin, mesFin, anhoFin, false)) {
                    filteredEvents.add(e);
                }
            }
            if (filteredEvents.isEmpty()) {
                filteredEvents = cachedEvents;
            }
        }else{
            for (Event e : filteredEventsCopy) {

                if (dateCompare(e.getFecha(), diaInicio, mesInicio, anhoInicio, true) &&
                        dateCompare(e.getFecha(), diaFin, mesFin, anhoFin, false)) {
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
        filteredEventsCopyDate=filteredEvents;
    }

    public List<Event> getFilteredEvents() {
        return filteredEvents;
    }

    public List<Event> getCachedEvents() {
        return cachedEvents;
    }

    /**
     *
     * @param fechaEvento fecha del evento que se desea comparar en formato String
     * @param dia dia con el que se desea comparar la fecha del evento
     * @param mes mes con el que se desea comparar la fecha del evento
     * @param anho anho con el que se desea comparar la fecha del evento
     * @param eventoMayor true si el evento es mas reciente o igual que las fechas
     *                    proporcionadas y false en caso contrario
     * @return true si el evento es mas reciente o no en funcion de lo indicado en el paramentro eventoMayor
     */
    private boolean dateCompare(String fechaEvento, int dia, int mes, int anho, boolean eventoMayor) {
        mes++;
        String[] date1 = fechaEvento.split(" ");
        String[] dateDefinitive = date1[1].split(",");
        String[] dateSeparada = dateDefinitive[0].split("/");

        int diaEvento = Integer.parseInt(dateSeparada[0]);
        int mesEvento = Integer.parseInt(dateSeparada[1]);
        int anhoEvento = Integer.parseInt(dateSeparada[2]);

        if (eventoMayor) {
            if (anho < anhoEvento) {
                return true;
            } else if (anho == anhoEvento) {
                if((mes) < mesEvento) {
                    return true;
                }else if((mes) == mesEvento && dia <= diaEvento) {
                    return true;
                }
            }
            return false;
        } else {
            if (anho > anhoEvento) {
                return true;
            } else if (anho == anhoEvento) {
                if((mes) > mesEvento) {
                    return true;
                }else if((mes) == mesEvento && dia >= diaEvento) {
                    return true;
                }
            }
            return false;
        }
    }
}
