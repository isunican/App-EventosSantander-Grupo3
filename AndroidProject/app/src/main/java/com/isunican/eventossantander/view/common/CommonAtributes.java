package com.isunican.eventossantander.view.common;

import android.widget.TextView;

import com.isunican.eventossantander.model.Event;

import java.time.LocalDate;
import java.util.ArrayList;

public class CommonAtributes {

    private int posi = 0;

    private static final String APLICAR = "Aplicar";
    private static final String CANCELAR = "Cancelar";

    private ArrayList<String> tiposSeleccionados = new ArrayList<>();
    private ArrayList<String> tiposSeleccionadosPrevio = new ArrayList<>();

    private ArrayList<Event> eventosEnFiltrosCombinados = new ArrayList<>();

    // Variables para filtrar por fecha
    private int diaInicio = 0;
    private int mesInicio = 0;
    private int anhoInicio = 0;

    // Variables para guardar las fechas seleccionadas
    private LocalDate fechaIni = null;
    private LocalDate fechaFin = null;
    private int diaFin = 0;
    private int mesFin = 0;
    private int anhoFin = 0;

    private int diaInicioPrevio = 0;
    private int mesInicioPrevio = 0;
    private int anhoInicioPrevio = 0;

    private int diaFinPrevio = 0;
    private int mesFinPrevio = 0;
    private int anhoFinPrevio = 0;

    private TextView textoFechaInicio = null;
    private TextView textoFechaFin = null;

    //GETTERS
    public int getPosi(){return posi;}

    public String getAplicar(){return APLICAR;}
    public String getCancelar(){return CANCELAR;}

    public ArrayList<String> getTiposSeleccionados(){return tiposSeleccionados;}
    public ArrayList<String> getTiposSeleccionadosPrevio(){return tiposSeleccionadosPrevio;}
    public ArrayList<Event> getEventosEnFiltrosCombinados(){return eventosEnFiltrosCombinados;}

    public int getDiaInicio(){return diaInicio;}
    public int getMesInicio(){return mesInicio;}
    public int getAnhoInicio(){return anhoInicio;}

    public int getDiaFin(){return diaFin;}
    public int getMesFin(){return mesFin;}
    public int getAnhoFin(){return anhoFin;}

    public int getDiaInicioPrevio(){return diaInicioPrevio;}
    public int getMesInicioPrevio(){return mesInicioPrevio;}
    public int getAnhoInicioPrevio(){return anhoInicioPrevio;}

    public int getDiaFinPrevio(){return diaFinPrevio;}
    public int getMesFinPrevio(){return mesFinPrevio;}
    public int getAnhoFinPrevio(){return anhoFinPrevio;}

    public LocalDate getFechaInicio(){return fechaIni;}
    public LocalDate getFechaFin(){return fechaFin;}

    public TextView getTextoFechaInicio(){return textoFechaInicio;}
    public TextView getTextoFechaFin(){return textoFechaFin;}

    //SETTERS
    public void setPosi(int posi){this.posi = posi;}

    public void setTiposSeleccionados(ArrayList<String> tiposSeleccionados){this.tiposSeleccionados = tiposSeleccionados;}
    public void setTiposSeleccionadosPrevio(ArrayList<String> tiposSeleccionadosPrevio){this.tiposSeleccionadosPrevio = tiposSeleccionadosPrevio;}
    public void setEventosEnFiltrosCombinados(ArrayList<Event> eventosEnFiltrosCombinados){this.eventosEnFiltrosCombinados = eventosEnFiltrosCombinados;}

    public void setDiaInicio(int diaInicio){this.diaInicio = diaInicio;}
    public void setMesInicio(int mesInicio){this.mesInicio = mesInicio;}
    public void setAnhoInicio(int anhoInicio){this.anhoInicio = anhoInicio;}

    public void setDiaFin(int diaFin){this.diaFin = diaFin;}
    public void setMesFin(int mesFin){this.mesFin = mesFin;}
    public void setAnhoFin(int anhoFin){this.anhoFin = anhoFin;}

    public void setDiaInicioPrevio(int diaInicioPrevio){this.diaInicioPrevio = diaInicioPrevio;}
    public void setMesInicioPrevio(int mesInicioPrevio){this.mesInicioPrevio = mesInicioPrevio;}
    public void setAnhoInicioPrevio(int anhoInicioPrevio){this.anhoInicioPrevio = anhoInicioPrevio;}

    public void setDiaFinPrevio(int diaFinPrevio){this.diaFinPrevio = diaFinPrevio;}
    public void setMesFinPrevio(int mesFinPrevio){this.mesFinPrevio = mesFinPrevio;}
    public void setAnhoFinPrevio(int anhoFinPrevio){this.anhoFinPrevio = anhoFinPrevio;}

    public void setFechaInicio(LocalDate fechaInicio){this.fechaIni = fechaInicio;}
    public void setFechaFin(LocalDate fechaFin){this.fechaFin = fechaFin;}

    public void setTextoFechaInicio(TextView textoFechaInicio){this.textoFechaInicio = textoFechaInicio;}
    public void setTextoFechaFin(TextView textoFechaFin){this.textoFechaFin = textoFechaFin;}
}
