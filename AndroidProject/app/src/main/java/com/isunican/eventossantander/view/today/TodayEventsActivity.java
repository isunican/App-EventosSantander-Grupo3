package com.isunican.eventossantander.view.today;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.presenter.today.TodayEventsPresenter;
import com.isunican.eventossantander.view.common.CommonAtributes;
import com.isunican.eventossantander.view.common.CommonEventsActivity;
import com.isunican.eventossantander.view.events.EventArrayAdapter;
import com.isunican.eventossantander.view.events.IEventsContract;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
import com.isunican.eventossantander.view.info.InfoActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TodayEventsActivity extends AppCompatActivity implements IEventsContract.View, View.OnClickListener {

    private ITodayEventsContract.Presenter presenter;
    private CommonAtributes commonAtributes;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // es importante llamar siempre al método de la clase padre, para inicializar
        // correctamente
        super.onCreate(savedInstanceState);
        // instancia la interfaz definida en el Layout activity_main.xml
        setContentView(R.layout.activity_events_today);

        // Enlazamos con los widgets del layout

        Button btnOrdenar = findViewById(R.id.btn_ordenar);

        // Asignamos los listeners para los botones
        btnOrdenar.setOnClickListener(this);

        // Creamos objeto presenter para cargar los datos del modelo y mostrarlos en la vista
        //enlazamos con el layout y asignamos listener para el boton de filtrar
        Button btnFiltrar = findViewById(R.id.btn_filtrar);
        btnFiltrar.setOnClickListener(this);

        presenter = new TodayEventsPresenter( this);
        commonAtributes.setTiposSeleccionadosPrevio(new ArrayList<>());
        commonAtributes.setEventosEnFiltrosCombinados(new ArrayList<>());

        // Se intenta recargar las variables de filtrar entre dos fechas
        onReloadFilteredDates();
    }

    @Override
    public void onEventsLoaded(List<Event> events) {
        EventArrayAdapter adapter = new EventArrayAdapter(TodayEventsActivity.this, 0, events);
        ListView listView = findViewById(R.id.eventsListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> presenter.onEventClicked(position));
    }

    @Override
    public void onLoadError() {
        //Error de carga
    }

    @Override
    public void onLoadSuccess(int elementsLoaded) {
        @SuppressLint("DefaultLocale") String text = String.format("Loaded %d events", elementsLoaded);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadNoEventsInDate() {
        Toast.makeText(this, "No hay eventos en esas fechas", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openEventDetails(Event event) {
        Intent intent = new Intent(this, EventsDetailActivity.class);
        intent.putExtra(EventsDetailActivity.INTENT_EVENT, event);
        startActivity(intent);
    }

    @Override
    public void openInfoView() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("DIAINICIO",commonAtributes.getDiaInicio());
        outState.putInt("MESINICIO",commonAtributes.getMesInicio());
        outState.putInt("ANHOINICIO",commonAtributes.getAnhoInicio());

        outState.putInt("DIAFIN",commonAtributes.getDiaFin());
        outState.putInt("MESFIN",commonAtributes.getMesFin());
        outState.putInt("ANHOFIN",commonAtributes.getAnhoFin());

        outState.putInt("DIAINICIOPREVIO",commonAtributes.getDiaInicioPrevio());
        outState.putInt("MESINICIOPREVIO",commonAtributes.getMesInicioPrevio());
        outState.putInt("ANHOINICIOPREVIO",commonAtributes.getAnhoInicioPrevio());

        outState.putInt("DIAFINPREVIO",commonAtributes.getDiaFinPrevio());
        outState.putInt("MESFINPREVIO",commonAtributes.getMesFinPrevio());
        outState.putInt("ANHOFINPREVIO",commonAtributes.getAnhoFinPrevio());

        outState.putStringArrayList("TIPOSSLECCIONADOS", commonAtributes.getTiposSeleccionados());
        outState.putStringArrayList("TIPOSSLECCIONADOSPREVIO", commonAtributes.getTiposSeleccionadosPrevio());

        commonAtributes.setEventosEnFiltrosCombinados((ArrayList<Event>) presenter.getCachedEventsOrdenados());
        //outState.putParcelableArrayList("FILTEREDEVENTS", eventosEnFiltrosCombinados);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

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
        presenter.setCachedEventsOrdenados(commonAtributes.getEventosEnFiltrosCombinados());
    }

    /*
    Menu Handling
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menuHoy) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_hoy, menuHoy);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                // Se reinician las variables del filtro de fecha
                commonAtributes.setDiaInicioPrevio(-1); commonAtributes.setMesInicioPrevio(-1); commonAtributes.setAnhoInicioPrevio(-1);
                commonAtributes.setDiaFinPrevio(-1); commonAtributes.setMesFinPrevio(-1); commonAtributes.setAnhoFinPrevio(-1);

                commonAtributes.getTiposSeleccionadosPrevio().clear();
                presenter.onReloadClicked();
                return true;
            case R.id.menu_filter_date:
                onDateFilterAlertDialog();
                return true;
            case R.id.menu_info:
                presenter.onInfoClicked();
                return true;
            case R.id.menu_volver:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_filtrar) {
            AlertDialog ad = onFilterAlertDialog();
            ad.show();
        } else if (view.getId() == R.id.btn_ordenar) {
            onOrdenarAlertDialog();
        }
    }


    public AlertDialog onFilterAlertDialog(){
        //Creamos dos listas donde tenemos los tipos de evento, y los tipos marcados para filtrar
        ArrayList<String> tipostotales = new ArrayList<>();
        CommonEventsActivity.anhadirTiposeventos(tipostotales);
        tipostotales.toArray(new String[0]);

        commonAtributes.setTiposSeleccionados(new ArrayList<>());

        //Creamos una AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Le ponemos un titulo
        builder.setTitle("Filtrar");

        //Comprobamos los elementos seleccionados previamente
        // Declaramos campos para enlazar con widgets del layout
        boolean[] checked = new boolean[tipostotales.size()];
        int posChecked = 0;
        Arrays.fill(checked, false);
        for(String s : tipostotales){
            for(String p : commonAtributes.getTiposSeleccionadosPrevio()){
                if(s.equals(p)){
                    checked[posChecked]=true;
                    commonAtributes.getTiposSeleccionados().add(s);
                }
            }
            posChecked++;
        }


        //Creamos los elementos de la seleccion de tipo multiple
        builder.setMultiChoiceItems(tipostotales.toArray(new String[0]), checked, (dialog, which, estaMarcado) -> {
            if (estaMarcado) {
                // If the user checked the item, add it to the selected items
                commonAtributes.getTiposSeleccionados().add(tipostotales.get(which));
                commonAtributes.setTiposSeleccionadosPrevio(commonAtributes.getTiposSeleccionados());
            } else if (commonAtributes.getTiposSeleccionados().contains(tipostotales.get(which))) {
                // Else, if the item is already in the array, remove it
                commonAtributes.getTiposSeleccionados().remove(tipostotales.get(which));
                commonAtributes.setTiposSeleccionadosPrevio(commonAtributes.getTiposSeleccionados());
            }
        });

        //Creamos el boton de aplicar
        builder.setPositiveButton(commonAtributes.getAplicar(), (dialogInterface, i) -> presenter.onFiltrarClicked(commonAtributes.getTiposSeleccionados()));
        builder.setNegativeButton(commonAtributes.getCancelar(), (dialogInterface, i) -> {
            //si se cancela no se hace nada
        });
        return builder.create();
    }


    public void onOrdenarAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(
                R.layout.alert_dialog_ordenar,
                (ConstraintLayout) findViewById(R.id.layout_dialog_container)
        );

        RadioButton btnTipoAscendente = (RadioButton) view.findViewById(R.id.btn_ordenar_ascendente);
        RadioButton btnTipoDescendente = (RadioButton) view.findViewById(R.id.btn_ordenar_descendente);
        RadioButton btnHoraMasProxima = (RadioButton) view.findViewById(R.id.btn_mas_proximas_primero);
        RadioButton btnHoraMenosProxima = (RadioButton) view.findViewById(R.id.btn_menos_proximas_primero);

        builder.setView(view);
        final AlertDialog ad = builder.create();

        view.findViewById(R.id.btn_ordenar_ascendente).setOnClickListener(view0 -> {
            commonAtributes.setPosi(0);
            btnTipoDescendente.setChecked(false);
            btnHoraMasProxima.setChecked(false);
            btnHoraMenosProxima.setChecked(false);
        });

        view.findViewById(R.id.btn_ordenar_descendente).setOnClickListener(view1 -> {
            commonAtributes.setPosi(1);
            btnTipoAscendente.setChecked(false);
            btnHoraMasProxima.setChecked(false);
            btnHoraMenosProxima.setChecked(false);
        });

        view.findViewById(R.id.btn_mas_proximas_primero).setOnClickListener(view2 -> {
            commonAtributes.setPosi(2);;
            btnTipoAscendente.setChecked(false);
            btnTipoDescendente.setChecked(false);
            btnHoraMenosProxima.setChecked(false);
        });

        view.findViewById(R.id.btn_menos_proximas_primero).setOnClickListener(view3 -> {
            commonAtributes.setPosi(3);
            btnTipoAscendente.setChecked(false);
            btnTipoDescendente.setChecked(false);
            btnHoraMasProxima.setChecked(false);
        });

        // Caso en el que se pulsa el boton de cancelar
        view.findViewById(R.id.ordenar_cancelar).setOnClickListener(view4 -> ad.dismiss());

        // Caso en el que se pulsa el boton de aceptar
        view.findViewById(R.id.ordenar_aplicar);
        view.setOnClickListener(view5 -> {
            presenter.onOrdenarClicked(commonAtributes.getPosi());
            // Se cierra el Alert Dialog
            ad.dismiss();
        });
        ad.show();
        btnTipoAscendente.setChecked(true);
    }

    /**
     * Crea un alertDialog personalizado que muestra la posibilidad de introducir 2 fechas
     * de inicio y final para filtrar la lista de eventos acorde a dichas fechas
     * En el caso de que no se introduzcan ambas fechas o que la de fin sea anterior a la de inicio
     * se le notifica al usuario y no se realiza ningun cambio
     */
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onDateFilterAlertDialog() {

        commonAtributes.setDiaInicio(commonAtributes.getDiaInicioPrevio());
        commonAtributes.setMesInicio(commonAtributes.getMesInicioPrevio());
        commonAtributes.setAnhoInicio(commonAtributes.getAnhoInicioPrevio());
        commonAtributes.setDiaFin(commonAtributes.getDiaFinPrevio());
        commonAtributes.setMesFin(commonAtributes.getMesFinPrevio());
        commonAtributes.setAnhoFin(commonAtributes.getAnhoFinPrevio());

        SharedPreferences sharpref = getPreferences(Context.MODE_PRIVATE); // Sensitive

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(
                R.layout.alert_dialog_filtrar_fecha,
                (ConstraintLayout) findViewById(R.id.layout_dialog_container)
        );

        // Texto de fecha de inicio y de final a mostrar en el AlertDialog de filtrar por fecha
        commonAtributes.setTextoFechaInicio(view.findViewById(R.id.filtrar_fecha_inicio_texto));
        commonAtributes.setTextoFechaFin(view.findViewById(R.id.filtrar_fecha_fin_texto));

        // Si habia fechas introducidas anteriormente se muestran
        if (commonAtributes.getDiaInicioPrevio()!=-1) {
            commonAtributes.getTextoFechaFin().setText(commonAtributes.getDiaFinPrevio()+"/"+(commonAtributes.getMesFinPrevio()+1)+"/"+commonAtributes.getAnhoFinPrevio());
            commonAtributes.getTextoFechaInicio().setText(commonAtributes.getDiaInicioPrevio()+"/"+(commonAtributes.getMesInicioPrevio()+1)+"/"+commonAtributes.getAnhoInicioPrevio());
        }

        builder.setView(view);
        final AlertDialog adff = builder.create();

        // Caso en el que se pulse la fecha de inicio (mediante la pulsacion del titulo)
        view.findViewById(R.id.filtrar_fecha_inicio_titulo).setOnClickListener(view16 -> onSelectStartDate());
        // Caso en el que se pulse la fecha de inicio (mediante la pulsacion del texto)
        view.findViewById(R.id.filtrar_fecha_inicio_texto).setOnClickListener(view15 -> onSelectStartDate());

        // Caso en el que se pulse la fecha de fin (mediante la pulsacion del titulo)
        view.findViewById(R.id.filtrar_fecha_fin_titulo).setOnClickListener(view14 -> onSelectFinishDate());
        // Caso en el que se pulse la fecha de fin (mediante la pulsacion del texto)
        view.findViewById(R.id.filtrar_fecha_fin_texto).setOnClickListener(view13 -> onSelectFinishDate());

        view.findViewById(R.id.filtrar_fecha_cancelar).setOnClickListener(view12 -> adff.dismiss());
        // Caso en el que se pulsa el boton de aceptar
        view.findViewById(R.id.filtrar_fecha_aceptar);
        view.setOnClickListener(view1 -> {

            // Si falta una fecha muestra un mensaje de error
            if (commonAtributes.getDiaFin() == -1 || commonAtributes.getDiaInicio() == -1) {
                Toast.makeText(getBaseContext(), "Ambas fechas deben estar seleccionadas", Toast.LENGTH_SHORT).show();
            } else {
                // Si la fecha de inicio es posterior ala de fin se notifica al usuario
                if (!onCheckDateOrder()) {
                    Toast.makeText(getBaseContext(), "Fecha de fin debe ser posterior a fecha de inicio", Toast.LENGTH_SHORT).show();
                } else {
                    commonAtributes.setDiaInicioPrevio(commonAtributes.getDiaInicio());
                    commonAtributes.setMesInicioPrevio(commonAtributes.getMesInicio());
                    commonAtributes.setAnhoInicioPrevio(commonAtributes.getAnhoInicio());
                    commonAtributes.setDiaFinPrevio(commonAtributes.getDiaFin());
                    commonAtributes.setMesFinPrevio(commonAtributes.getMesFin());
                    commonAtributes.setAnhoFinPrevio(commonAtributes.getAnhoFin());
                    commonAtributes.setFechaInicio(LocalDate.of(commonAtributes.getAnhoInicio(), commonAtributes.getMesInicio()+1, commonAtributes.getDiaInicio()));
                    commonAtributes.setFechaFin(LocalDate.of(commonAtributes.getAnhoFin(), commonAtributes.getMesFin()+1, commonAtributes.getDiaFin()));
                    presenter.onFiltrarDate(commonAtributes.getFechaInicio(),commonAtributes.getFechaFin());

                    SharedPreferences.Editor editor = sharpref.edit();
                    editor.putInt("diaInicioPrevioGuardado", commonAtributes.getDiaInicioPrevio());
                    editor.putInt("mesInicioPrevioGuardado", commonAtributes.getMesInicioPrevio());
                    editor.putInt("anhoInicioPrevioGuardado",  commonAtributes.getAnhoInicioPrevio());
                    editor.putInt("diaFinPrevioGuardado", commonAtributes.getDiaFinPrevio());
                    editor.putInt("mesFinPrevioGuardado", commonAtributes.getMesFinPrevio());
                    editor.putInt("anhoFinPrevioGuardado", commonAtributes.getAnhoFinPrevio());
                    editor.apply();

                    // Se cierra el Alert Dialog
                    adff.dismiss();
                }
            }
        });

        if (adff.getWindow() != null) {
            adff.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        adff.show();
    }

    /**
     * Selecciona una fecha mediante el standar de Android para la fecha de inicio
     */
    private void onSelectStartDate() {
        final Calendar c = Calendar.getInstance();
        commonAtributes.setDiaInicio(c.get(Calendar.DAY_OF_MONTH));
        commonAtributes.setMesInicio(c.get(Calendar.MONTH));
        commonAtributes.setAnhoInicio(c.get(Calendar.YEAR));

        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, anho, mes, dia) -> {
            commonAtributes.setDiaInicio(dia);
            commonAtributes.setMesInicio(mes);
            commonAtributes.setAnhoInicio(anho);
            commonAtributes.getTextoFechaInicio().setText(commonAtributes.getDiaInicio()+"/"+(commonAtributes.getMesInicio()+1)+"/"+commonAtributes.getAnhoInicio());
        }
                ,commonAtributes.getDiaInicio(),commonAtributes.getMesInicio(),commonAtributes.getAnhoInicio());
        datePickerDialog.show();
    }

    /**
     * Selecciona una fecha mediante el standar de Android para la fecha de fin
     */
    private void onSelectFinishDate() {
        final Calendar c = Calendar.getInstance();

        commonAtributes.setDiaFin(c.get(Calendar.DAY_OF_MONTH));
        commonAtributes.setMesFin(c.get(Calendar.MONTH));
        commonAtributes.setAnhoFin(c.get(Calendar.YEAR));

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this::onDateSet
                ,commonAtributes.getDiaFin(),commonAtributes.getMesFin(),commonAtributes.getAnhoFin());
        datePickerDialog.show();
    }

    /**
     * Comprueba que la fecha de fin es menor que la fecha de inicio
     * @return true si es verdad, false en caso contrario
     */
    private boolean onCheckDateOrder() {

        if (commonAtributes.getAnhoInicio() < commonAtributes.getAnhoFin()) {
            return true;
        } else if (commonAtributes.getAnhoInicio() == commonAtributes.getAnhoFin()) {
            if(commonAtributes.getMesInicio() < commonAtributes.getMesFin()) {
                return true;
            }else return commonAtributes.getMesInicio() == commonAtributes.getMesFin() &&
                    commonAtributes.getDiaInicio() <= commonAtributes.getDiaFin();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onReloadFilteredDates() {
        SharedPreferences sharpref = getPreferences(Context.MODE_PRIVATE); // Sensitive
        commonAtributes.setDiaInicioPrevio(sharpref.getInt("diaInicioPrevioGuardado", -1));
        commonAtributes.setMesInicioPrevio(sharpref.getInt("mesInicioPrevioGuardado", -1));
        commonAtributes.setAnhoInicioPrevio(sharpref.getInt("anhoInicioPrevioGuardado", -1));
        commonAtributes.setDiaFinPrevio(sharpref.getInt("diaFinPrevioGuardado", -1));
        commonAtributes.setMesFinPrevio(sharpref.getInt("mesFinPrevioGuardado", -1));
        commonAtributes.setAnhoFinPrevio(sharpref.getInt("anhoFinPrevioGuardado", -1));
    }

    @SuppressLint("SetTextI18n")
    private void onDateSet(DatePicker datePicker, int anho, int mes, int dia) {
        commonAtributes.setDiaFin(dia);
        commonAtributes.setMesFin(mes);
        commonAtributes.setAnhoFin(anho);
        commonAtributes.getTextoFechaFin().setText(commonAtributes.getDiaFin() + "/" + (commonAtributes.getMesFin() + 1) + "/" + commonAtributes.getAnhoFin());
    }
}