package com.isunican.eventossantander.view.events;

import android.app.Dialog;

import androidx.appcompat.app.AlertDialog;

import com.isunican.eventossantander.model.Event;

import java.util.List;

public interface IEventsContract {

    public interface Presenter {

        void onEventClicked(int eventIndex);

        void onReloadClicked();

        void onInfoClicked();

        void onOrdenarCategoriaClicked(int tipoOrdenacion);
    }

    public interface View {

        void onEventsLoaded(List<Event> events);

        void onLoadError();

        void onLoadSuccess(int elementsLoaded);

        void openEventDetails(Event event);

        void openInfoView();

        AlertDialog onFilterAlertDialog();

    }
}
