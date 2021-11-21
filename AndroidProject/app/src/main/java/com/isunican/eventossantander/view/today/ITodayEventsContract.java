package com.isunican.eventossantander.view.today;

import com.isunican.eventossantander.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface ITodayEventsContract {

     interface Presenter {

        void onEventClicked(int eventIndex);

        void onReloadClicked();

        void onInfoClicked();

        void onOrdenarClicked(int tipoOrdenacion);

        void onFiltrarClicked(List<String> checkboxSeleccionados);

        void onFiltrarDate(LocalDate fechaIni, LocalDate fechaFin);

        List<Event>eventosHoy(Boolean test);

        List<Event> getCachedEventsOrdenados();

        void setCachedEventsOrdenados(List<Event> events);
    }

}
