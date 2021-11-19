package com.isunican.eventossantander.view.common;

import android.os.Bundle;

import java.util.List;


public class CommonEventsActivity {

    private static CommonAtributes commonAtributes;

    public static void onSaveInstanceState(Bundle outState){
        outState.putInt("DIAINICIO",commonAtributes.getDiaInicio());
        outState.putInt("MESINICIO",commonAtributes.getMesInicio());
        outState.putInt("ANHOINICIO",commonAtributes.getAnhoInicio());

        outState.putInt("DIAFIN",commonAtributes.getDiaFin());
        outState.putInt("MESFIN",commonAtributes.getMesFin());
        outState.putInt("ANHOFIN",commonAtributes.getAnhoFin());

        outState.putInt("DIAINICIOPREVIO",commonAtributes.getDiaInicioPrevio());
        outState.putInt("MESINICIOPREVIO",commonAtributes.getMesInicioPrevio());
        outState.putInt("ANHOINICIOPREVIO", commonAtributes.getAnhoInicioPrevio());

        outState.putInt("DIAFINPREVIO",commonAtributes.getDiaFinPrevio());
        outState.putInt("MESFINPREVIO",commonAtributes.getMesFinPrevio());
        outState.putInt("ANHOFINPREVIO",commonAtributes.getAnhoFinPrevio());

        outState.putStringArrayList("TIPOSSLECCIONADOS", commonAtributes.getTiposSeleccionados());
        outState.putStringArrayList("TIPOSSLECCIONADOSPREVIO", commonAtributes.getTiposSeleccionadosPrevio());
    }

    public static  void onRestoreInstanceState(Bundle savedInstanceState){
        // Guarda la informacion de los filtros por fecha
        commonAtributes.setDiaInicio(savedInstanceState.getInt("DIAINICIO"));
        commonAtributes.setMesInicio(savedInstanceState.getInt("MESINICIO"));
        commonAtributes.setAnhoInicio(savedInstanceState.getInt("ANHOINICIO",commonAtributes.getAnhoInicio()));

        commonAtributes.setDiaFin(savedInstanceState.getInt("DIAFIN"));
        commonAtributes.setMesFin(savedInstanceState.getInt("MESFIN"));
        commonAtributes.setAnhoFin(savedInstanceState.getInt("ANHOFIN"));

        commonAtributes.setDiaInicioPrevio(savedInstanceState.getInt("DIAINICIOPREVIO"));
        commonAtributes.setMesInicioPrevio(savedInstanceState.getInt("MESINICIOPREVIO"));
        commonAtributes.setAnhoInicioPrevio(savedInstanceState.getInt("ANHOINICIOPREVIO"));

        commonAtributes.setDiaFinPrevio(savedInstanceState.getInt("DIAFINPREVIO"));
        commonAtributes.setMesFinPrevio(savedInstanceState.getInt("MESFINPREVIO"));
        commonAtributes.setAnhoFinPrevio(savedInstanceState.getInt("ANHOFINPREVIO"));

        // Guarda la informacion de los filtros por tipo
        commonAtributes.setTiposSeleccionados(savedInstanceState.getStringArrayList("TIPOSSLECCIONADOS"));
        commonAtributes.setTiposSeleccionadosPrevio(savedInstanceState.getStringArrayList("TIPOSSLECCIONADOSPREVIO"));

        commonAtributes.setEventosEnFiltrosCombinados(savedInstanceState.getParcelableArrayList("FILTEREDEVENTS"));
    }

    public static void anhadirTiposeventos(List<String> tipostotales){
        tipostotales.add("Arquitectura");
        tipostotales.add("Artes plásticas");
        tipostotales.add("Cine/Audiovisual");
        tipostotales.add("Edición/Literatura");
        tipostotales.add("Formación/Talleres");
        tipostotales.add("Fotografía");
        tipostotales.add("Infantil");
        tipostotales.add("Música");
        tipostotales.add("Online");
        tipostotales.add("Otros");
    }

}
