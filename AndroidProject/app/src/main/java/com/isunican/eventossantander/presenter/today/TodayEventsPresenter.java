package com.isunican.eventossantander.presenter.today;


import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.presenter.common.CommonPresenter;
import com.isunican.eventossantander.view.Listener;
import com.isunican.eventossantander.view.events.IEventsContract;
import com.isunican.eventossantander.view.today.ITodayEventsContract;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TodayEventsPresenter implements ITodayEventsContract.Presenter {

    private final IEventsContract.View view;
    private List<Event> cachedEvents;
    private List<Event> eventosEnDeterminadasFechas;
    private List<Event> eventosEnDeterminadosFiltros;
    private List<Event> eventosEnFiltrosCombinados;
    private List<Event> datosHoy;
    private int ordenFiltrado;

    public TodayEventsPresenter(IEventsContract.View view) {
        this.view = view;
        loadData();
        eventosEnDeterminadosFiltros = new ArrayList<>();
        eventosEnDeterminadasFechas = new ArrayList<>();
        eventosEnFiltrosCombinados = new ArrayList<>();
    }


    private void loadData() {
        EventsRepository.getEvents(new Listener<>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(List<Event> data) {

                datosHoy = new ArrayList<>();

                cachedEvents = data;
                ordenFiltrado = 2;
                datosHoy = eventosHoy(false);
                cachedEvents = datosHoy;


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
        CommonPresenter.onEventClicked(eventIndex, cachedEvents, view);
    }

    @Override
    public void onReloadClicked() {
        eventosEnDeterminadosFiltros.clear();
        eventosEnDeterminadasFechas.clear();
        eventosEnFiltrosCombinados.clear();
        loadData();
    }

    @Override
    public void onInfoClicked() {
        CommonPresenter.onInfoClicked(view);
    }

    @Override
    public void onOrdenarClicked(int tipoOrdenacion) {
        ordenFiltrado = tipoOrdenacion;
        eventosEnFiltrosCombinados = CommonPresenter.onOrdenarClicked(tipoOrdenacion, eventosEnFiltrosCombinados, cachedEvents);
        view.onEventsLoaded(eventosEnFiltrosCombinados);
    }


    @Override
    public void onFiltrarClicked(List<String> checkboxSeleccionados) {
        eventosEnFiltrosCombinados = CommonPresenter.onFiltrarClicked(checkboxSeleccionados, cachedEvents,
                ordenFiltrado, eventosEnDeterminadasFechas);
        view.onEventsLoaded(eventosEnFiltrosCombinados);
        view.onLoadSuccess(eventosEnFiltrosCombinados.size());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onFiltrarDate(LocalDate fechaIni, LocalDate fechaFin) {

        LocalDate fechaEvento;

        List<Event>filteredEvents = new ArrayList<>();

        for (Event e : cachedEvents) {
            String[] date1 = e.getFecha().split(" ");
            String[] dateDefinitive = date1[1].split(",");
            String[] dateSeparada = dateDefinitive[0].split("/");

            int diaEvento = Integer.parseInt(dateSeparada[0]);
            int mesEvento = Integer.parseInt(dateSeparada[1]);
            int anhoEvento = Integer.parseInt(dateSeparada[2]);
            fechaEvento= LocalDate.of(anhoEvento,mesEvento,diaEvento);

            if(fechaEvento.compareTo(fechaIni)>=0 && fechaEvento.compareTo(fechaFin)<=0){
                filteredEvents.add(e);
            }
        }
        if (filteredEvents.isEmpty()) {
            eventosEnDeterminadasFechas = cachedEvents;
            combinaFiltros();
            view.onLoadNoEventsInDate();
        }else {
            eventosEnDeterminadasFechas = filteredEvents;
            combinaFiltros();
            view.onLoadSuccess(eventosEnFiltrosCombinados.size());
        }
        view.onEventsLoaded(eventosEnFiltrosCombinados);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Event> eventosHoy(Boolean test) {
        List<Event> eventosHoy = new ArrayList<>();
        LocalDate now;
        if (!test) {
            now = LocalDate.now();
        }else {
            now = LocalDate.ofYearDay(2021, 323);
        }

        for (Event e : cachedEvents) {
            String[] date1 = e.getFecha().split(" ");
            String[] dateDefinitive = date1[1].split(",");
            String[] dateSeparada = dateDefinitive[0].split("/");

            int diaEvento = Integer.parseInt(dateSeparada[0]);
            int mesEvento = Integer.parseInt(dateSeparada[1]);
            int anhoEvento = Integer.parseInt(dateSeparada[2]);

            LocalDate fechaEvento = LocalDate.of(anhoEvento, mesEvento, diaEvento);

            if (fechaEvento.equals(now)) {
                eventosHoy.add(e);
            }
        }
        return eventosHoy;
    }

    public void combinaFiltros() {
        if (eventosEnDeterminadasFechas == null || eventosEnDeterminadosFiltros == null) {
            throw new NullPointerException();
        }

        eventosEnFiltrosCombinados = new ArrayList<>();

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
    }

    public List<Event> getCachedEvents() {
        return cachedEvents;
    }

    @Override
    public List<Event> getCachedEventsOrdenados() {
        return eventosEnFiltrosCombinados;
    }

    @Override
    public void setCachedEventsOrdenados(List<Event> events) {
        eventosEnFiltrosCombinados = events;
    }
}




