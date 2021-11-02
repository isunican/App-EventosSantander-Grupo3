package com.isunican.eventossantander.view.events;

import com.isunican.eventossantander.model.Event;

import java.util.List;

public interface IEventsContract {

    public interface Presenter {

        void onEventClicked(int eventIndex);

        void onReloadClicked();

        void onInfoClicked();

        void onOrdenarCategoriaClicked(int tipoOrdenacion);

        void onFiltrarClicked(List<String> checkboxSeleccionados);

        void onFiltrarDate(int diaInicio, int mesInicio, int anhoInicio, int diaFin, int mesFin, int anhoFin);
    }

    public interface View {

        void onEventsLoaded(List<Event> events);

        void onLoadError();

        void onLoadSuccess(int elementsLoaded);

        void openEventDetails(Event event);

        void openInfoView();
    }
}
