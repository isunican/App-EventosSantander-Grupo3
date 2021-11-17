package com.isunican.eventossantander.presenter.events;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.model.EventsRepository;
import com.isunican.eventossantander.model.comparators.EventsComparatorCategoria;
import com.isunican.eventossantander.model.comparators.EventsComparatorHora;
import com.isunican.eventossantander.presenter.common.CommonPresenter;
import com.isunican.eventossantander.view.Listener;
import com.isunican.eventossantander.view.events.IEventsContract;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EventsPresenter implements IEventsContract.Presenter {

    private final IEventsContract.View view;
    private List<Event> cachedEvents;
    private List<Event> filteredEvents;
    private List<Event> eventosEnDeterminadasFechas;
    private List<Event> eventosEnDeterminadosFiltros;
    private List<Event> eventosEnFiltrosCombinados;
    private int ordenFiltrado;
    

    public void setEventosEnDeterminadosFiltros(List<Event> list) {
         eventosEnDeterminadosFiltros = list;
    }

    public void setEventosEnDeterminadasFechas(List<Event> list) {
        eventosEnDeterminadasFechas = list;
    }

    public EventsPresenter(IEventsContract.View view) {
        this.view = view;
        loadData();
        eventosEnDeterminadosFiltros = new ArrayList<>();
        eventosEnDeterminadasFechas = new ArrayList<>();
        eventosEnFiltrosCombinados = new ArrayList<>();
    }

    private void loadData() {
        EventsRepository.getEvents(new Listener<>() {
            @Override
            public void onSuccess(List<Event> data) {

                cachedEvents = data;
                ordenFiltrado = 2;

                if(!eventosEnFiltrosCombinados.isEmpty()) {
                    view.onEventsLoaded(eventosEnFiltrosCombinados);
                    view.onLoadSuccess(eventosEnFiltrosCombinados.size());
                } else {
                    view.onEventsLoaded(data);
                    view.onLoadSuccess(data.size());
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
        eventosEnFiltrosCombinados = CommonPresenter.onOrdenarClicked(tipoOrdenacion, eventosEnFiltrosCombinados, cachedEvents);
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

        if(ordenFiltrado != 2) {
            onOrdenarClicked(ordenFiltrado);
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

    public static Date obtenerFechaActual(String zonaHoraria) {
        String formato = "yyyy-MM-dd";
        return obtenerFechaConFormato(formato, zonaHoraria);
    }

    @SuppressLint("SimpleDateFormat")
    public static Date obtenerFechaConFormato(String formato, String zonaHoraria) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(formato);
        sdf.setTimeZone(TimeZone.getTimeZone(zonaHoraria));
        return date;
    }
}