package com.isunican.eventossantander.presenter.today;


import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.model.comparators.EventsComparatorCategoria;
import com.isunican.eventossantander.view.Listener;
import com.isunican.eventossantander.view.today.ITodayEventsContract;

import java.time.LocalDate;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class TodayEventsPresenter implements ITodayEventsContract.Presenter {

    private final ITodayEventsContract.View view;
    private List<Event> cachedEvents;
    private List<Event> filteredEvents;
    private List<Event> eventosEnDeterminadasFechas;
    private List<Event> eventosEnDeterminadosFiltros;
    private List<Event> eventosEnFiltrosCombinados;
    private List<Event> datosHoy;
    private int ordenFiltrado;

    public TodayEventsPresenter(ITodayEventsContract.View view) {
        this.view = view;
        loadData();
    }

    private void loadData() {
        EventsRepository.getEvents(new Listener<>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(List<Event> data) {

                eventosEnDeterminadosFiltros = new ArrayList<>();
                eventosEnDeterminadasFechas = new ArrayList<>();
                eventosEnFiltrosCombinados = new ArrayList<>();
                datosHoy = new ArrayList<>();

                cachedEvents = data;
                ordenFiltrado = 2;
                datosHoy = eventosHoy();

                if (datosHoy.isEmpty()) {
                    view.onLoadNoEventsInDate();
                } else {
                    view.onEventsLoaded(datosHoy);
                    view.onLoadSuccess(datosHoy.size());
                }
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
        if (eventIndex >= cachedEvents.size() || eventIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        cachedEvents.size();
        Event event = cachedEvents.get(eventIndex);
        view.openEventDetails(event);
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

        ordenFiltrado = tipoOrdenacion;

        if (tipoOrdenacion == 0) { //ascendente
            EventsComparatorCategoria ecc = new EventsComparatorCategoria();
            if (eventosEnFiltrosCombinados.isEmpty()) {
                Collections.sort(cachedEvents, ecc);
                eventosEnFiltrosCombinados = cachedEvents;
            } else {
                Collections.sort(eventosEnFiltrosCombinados, ecc);
            }
        } else if (tipoOrdenacion == 1) { //descendente
            EventsComparatorCategoria ecc = new EventsComparatorCategoria();
            if (eventosEnFiltrosCombinados.isEmpty()) {
                java.util.Collections.sort(cachedEvents, ecc);
                Collections.reverse(cachedEvents);
                eventosEnFiltrosCombinados = cachedEvents;
            } else {
                java.util.Collections.sort(eventosEnFiltrosCombinados, ecc);
                Collections.reverse(eventosEnFiltrosCombinados);
            }
        }
        view.onEventsLoaded(eventosEnFiltrosCombinados);
    }

    @Override
    public void onFiltrarClicked(List<String> checkboxSeleccionados) {

        filteredEvents = new ArrayList<>();

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
        combinaFiltros();

        if (ordenFiltrado != 2) {
            onOrdenarCategoriaClicked(ordenFiltrado);
        }

        view.onEventsLoaded(eventosEnFiltrosCombinados);
        view.onLoadSuccess(eventosEnFiltrosCombinados.size());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onFiltrarDate(LocalDate fechaIni, LocalDate fechaFin) {

        LocalDate fechaEvento;

        filteredEvents = new ArrayList<>();

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
            combinaFiltros();
            view.onLoadNoEventsInDate();
        } else {
            eventosEnDeterminadasFechas = filteredEvents;
            combinaFiltros();
            view.onLoadSuccess(eventosEnFiltrosCombinados.size());
        }
        view.onEventsLoaded(eventosEnFiltrosCombinados);
    }

    public void combinaFiltros() {
        if (eventosEnDeterminadasFechas == null || eventosEnDeterminadosFiltros == null) {
            throw new NullPointerException();
        }

        eventosEnFiltrosCombinados = new ArrayList<>();

        if (eventosEnDeterminadosFiltros.isEmpty()) {
            eventosEnFiltrosCombinados = eventosEnDeterminadasFechas;
        } else if (eventosEnDeterminadasFechas.isEmpty()) {
            eventosEnFiltrosCombinados = eventosEnDeterminadosFiltros;
        }

        for (Event i : eventosEnDeterminadosFiltros) {
            for (Event j : eventosEnDeterminadasFechas) {
                if (i == j) {
                    eventosEnFiltrosCombinados.add(j);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Event> eventosHoy() {
        List<Event> eventosHoy = new ArrayList<>();
        for (Event e : cachedEvents) {
            String[] date1 = e.getFecha().split(" ");
            String[] dateDefinitive = date1[1].split(",");
            String[] dateSeparada = dateDefinitive[0].split("/");

            int diaEvento = Integer.parseInt(dateSeparada[0]);
            int mesEvento = Integer.parseInt(dateSeparada[1]);
            int anhoEvento = Integer.parseInt(dateSeparada[2]);

            LocalDate fechaEvento = LocalDate.of(anhoEvento, mesEvento, diaEvento);

            if (fechaEvento.equals(LocalDate.now())) {
                eventosHoy.add(e);
            }
        }
        return eventosHoy;

        
    }
}




