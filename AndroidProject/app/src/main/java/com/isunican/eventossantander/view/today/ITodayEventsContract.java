package com.isunican.eventossantander.view.today;

import com.isunican.eventossantander.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface ITodayEventsContract {

    public interface Presenter {

        void onEventClicked(int eventIndex);

        void onReloadClicked();

        void onInfoClicked();

        void onOrdenarCategoriaClicked(int tipoOrdenacion);

        void onFiltrarClicked(List<String> checkboxSeleccionados);

        void onFiltrarDate(LocalDate fechaIni, LocalDate fechaFin);

        List<Event>eventosHoy();
    }

    public interface View {

        void onEventsLoaded(List<Event> events);

        void onLoadError();

        void onLoadSuccess(int elementsLoaded);

        void onLoadNoEventsInDate();

        void openEventDetails(Event event);

        void openInfoView();
    }
}
