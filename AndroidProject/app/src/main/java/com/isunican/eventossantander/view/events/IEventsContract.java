package com.isunican.eventossantander.view.events;

import com.isunican.eventossantander.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface IEventsContract {

    interface Presenter {

        void onEventClicked(int eventIndex);

        void onReloadClicked();

        void onInfoClicked();

        void onOrdenarClicked(int tipoOrdenacion);

        void onFiltrarClicked(List<String> checkboxSeleccionados);

        void onFiltrarDate(LocalDate fechaIni, LocalDate fechaFin);
    }

    interface View {

        void onEventsLoaded(List<Event> events);

        void onLoadError();

        void onLoadSuccess(int elementsLoaded);

        void onLoadNoEventsInDate();

        void openEventDetails(Event event);

        void openInfoView();
    }
}
