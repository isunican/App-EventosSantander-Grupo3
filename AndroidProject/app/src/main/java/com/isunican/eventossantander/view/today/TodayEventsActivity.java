package com.isunican.eventossantander.view.today;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.presenter.today.TodayEventsPresenter;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
import com.isunican.eventossantander.view.info.InfoActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TodayEventsActivity extends AppCompatActivity implements ITodayEventsContract.View, View.OnClickListener {

    private ITodayEventsContract.Presenter presenter;

    private int posi;

    private static final String APLICAR = "Aplicar";
    private static final String CANCELAR = "Cancelar";

    private List<String> tiposSeleccionados;
    private List<String> tiposSeleccionadosPrevio;

    // Variables para filtrar por fecha
    private int diaInicio;
    private int mesInicio;
    private int anhoInicio;

    // Variables para guardar las fechas seleccionadas
    private LocalDate fechaIni;
    private LocalDate fechaFin;
    private int diaFin;
    private int mesFin;
    private int anhoFin;

    private int diaInicioPrevio;
    private int mesInicioPrevio;
    private int anhoInicioPrevio;

    private int diaFinPrevio;
    private int mesFinPrevio;
    private int anhoFinPrevio;

    private TextView textoFechaInicio;
    private TextView textoFechaFin;

    private static void onClick(DialogInterface dialog, int id) {
        // TODO document why this method is empty
    }


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

        presenter = new TodayEventsPresenter(this);


        // Se intenta recargar las variables de filtrar entre dos fechas
        onReloadFilteredDates();
    }

    @Override
    public void onEventsLoaded(List<Event> events) {
        TodayEventArrayAdapter adapter = new TodayEventArrayAdapter(TodayEventsActivity.this, 0, events);
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
                diaInicioPrevio = -1; mesInicioPrevio = -1; anhoInicioPrevio = -1;
                diaFinPrevio = -1; mesFinPrevio = -1; anhoFinPrevio = -1;

                tiposSeleccionadosPrevio.clear();
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
            AlertDialog ado = onFilterAlertDialogOrdenar();
            ado.show();
        }
    }


    public AlertDialog onFilterAlertDialog(){
        //Creamos dos listas donde tenemos los tipos de evento, y los tipos marcados para filtrar
        List<String> tipostotales = new ArrayList<>();
        anhadirTiposeventos(tipostotales);
        tipostotales.toArray(new String[0]);

        tiposSeleccionados = new ArrayList<>();

        //Creamos una AlertDialog
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
            for(String p : tiposSeleccionadosPrevio){
                if(s.equals(p)){
                    checked[posChecked]=true;
                    tiposSeleccionados.add(s);
                }
            }
            posChecked++;
        }


        //Creamos los elementos de la seleccion de tipo multiple
        builder.setMultiChoiceItems(tipostotales.toArray(new String[0]), checked, (dialog, which, estaMarcado) -> {
            if (estaMarcado) {
                // If the user checked the item, add it to the selected items
                tiposSeleccionados.add(tipostotales.get(which));
                tiposSeleccionadosPrevio=tiposSeleccionados;
            } else if (tiposSeleccionados.contains(tipostotales.get(which))) {
                // Else, if the item is already in the array, remove it
                tiposSeleccionados.remove(tipostotales.get(which));
                tiposSeleccionadosPrevio=tiposSeleccionados;
            }
        });

        //Creamos el boton de aplicar
        builder.setPositiveButton(APLICAR, (dialogInterface, i) -> presenter.onFiltrarClicked(tiposSeleccionados));
        builder.setNegativeButton(CANCELAR, (dialogInterface, i) -> {
            //si se cancela no se hace nada
        });

        return builder.create();
    }


    public AlertDialog onFilterAlertDialogOrdenar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] array = {"Ascendente(A-Z)", "Descendente(Z-A)"};
        builder.setTitle("Ordenar");

        builder.setSingleChoiceItems(array, 0, (dialogInterface, i) -> posi = i);
        // Set the action buttons
        builder.setPositiveButton(APLICAR, (dialog, id) -> {
            presenter.onOrdenarCategoriaClicked(posi);
            // User clicked OK, so save the selectedItems results somewhere
            // or return them to the component that opened the dialog
        });
        builder.setNegativeButton(CANCELAR, TodayEventsActivity::onClick);
        return builder.create();
    }

    public void anhadirTiposeventos(List<String> tipostotales){
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

    /**
     * Crea un alertDialog personalizado que muestra la posibilidad de introducir 2 fechas
     * de inicio y final para filtrar la lista de eventos acorde a dichas fechas
     * En el caso de que no se introduzcan ambas fechas o que la de fin sea anterior a la de inicio
     * se le notifica al usuario y no se realiza ningun cambio
     */
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onDateFilterAlertDialog() {

        diaInicio = diaInicioPrevio;
        mesInicio = mesInicioPrevio;
        anhoInicio = anhoInicioPrevio;
        diaFin = diaFinPrevio;
        mesFin = mesFinPrevio;
        anhoFin = anhoFinPrevio;

        SharedPreferences sharpref = getPreferences(MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(
                R.layout.alert_dialog_filtrar_fecha,
                (ConstraintLayout) findViewById(R.id.layout_dialog_container)
        );

        // Texto de fecha de inicio y de final a mostrar en el AlertDialog de filtrar por fecha
        textoFechaInicio = (TextView) view.findViewById(R.id.filtrar_fecha_inicio_texto);
        textoFechaFin = (TextView) view.findViewById(R.id.filtrar_fecha_fin_texto);

        // Si habia fechas introducidas anteriormente se muestran
        if (diaInicioPrevio!=-1) {
            textoFechaFin.setText(diaFinPrevio+"/"+(mesFinPrevio+1)+"/"+anhoFinPrevio);
            textoFechaInicio.setText(diaInicioPrevio+"/"+(mesInicioPrevio+1)+"/"+anhoInicioPrevio);
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
            if (diaFin == -1 || diaInicio == -1) {
                Toast.makeText(getBaseContext(), "Ambas fechas deben estar seleccionadas", Toast.LENGTH_SHORT).show();
            } else {
                // Si la fecha de inicio es posterior ala de fin se notifica al usuario
                if (!onCheckDateOrder()) {
                    Toast.makeText(getBaseContext(), "Fecha de fin debe ser posterior a fecha de inicio", Toast.LENGTH_SHORT).show();
                } else {
                    diaInicioPrevio = diaInicio;
                    mesInicioPrevio = mesInicio;
                    anhoInicioPrevio = anhoInicio;
                    diaFinPrevio = diaFin;
                    mesFinPrevio = mesFin;
                    anhoFinPrevio = anhoFin;
                    fechaIni = LocalDate.of(anhoInicio, mesInicio+1, diaInicio);
                    fechaFin = LocalDate.of(anhoFin, mesFin+1, diaFin);
                    presenter.onFiltrarDate(fechaIni,fechaFin);

                    SharedPreferences.Editor editor = sharpref.edit();
                    editor.putInt("diaInicioPrevioGuardado", diaInicioPrevio);
                    editor.putInt("mesInicioPrevioGuardado", mesInicioPrevio);
                    editor.putInt("anhoInicioPrevioGuardado", anhoInicioPrevio);
                    editor.putInt("diaFinPrevioGuardado", diaFinPrevio);
                    editor.putInt("mesFinPrevioGuardado", mesFinPrevio);
                    editor.putInt("anhoFinPrevioGuardado", anhoFinPrevio);
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
        diaInicio= c.get(Calendar.DAY_OF_MONTH);
        mesInicio= c.get(Calendar.MONTH);
        anhoInicio= c.get(Calendar.YEAR);

        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, anho, mes, dia) -> {
            diaInicio = dia;
            mesInicio = mes;
            anhoInicio = anho;
            textoFechaInicio.setText(diaInicio+"/"+(mesInicio+1)+"/"+anhoInicio);
        }
                ,diaInicio,mesInicio,anhoInicio);
        datePickerDialog.show();
    }

    /**
     * Selecciona una fecha mediante el standar de Android para la fecha de fin
     */
    private void onSelectFinishDate() {
        final Calendar c = Calendar.getInstance();
        diaFin= c.get(Calendar.DAY_OF_MONTH);
        mesFin= c.get(Calendar.MONTH);
        anhoFin= c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this::onDateSet
                ,diaFin,mesFin,anhoFin);
        datePickerDialog.show();
    }

    /**
     * Comprueba que la fecha de fin es menor que la fecha de inicio
     * @return true si es verdad, false en caso contrario
     */
    private boolean onCheckDateOrder() {

        if (anhoInicio < anhoFin) {
            return true;
        } else if (anhoInicio == anhoFin) {
            if(mesInicio < mesFin) {
                return true;
            }else return mesInicio == mesFin && diaInicio <= diaFin;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onReloadFilteredDates() {

        SharedPreferences sharpref = getPreferences(MODE_PRIVATE);
        diaInicioPrevio = sharpref.getInt("diaInicioPrevioGuardado", -1);
        mesInicioPrevio = sharpref.getInt("mesInicioPrevioGuardado", -1);
        anhoInicioPrevio = sharpref.getInt("anhoInicioPrevioGuardado", -1);
        diaFinPrevio = sharpref.getInt("diaFinPrevioGuardado", -1);
        mesFinPrevio = sharpref.getInt("mesFinPrevioGuardado", -1);
        anhoFinPrevio = sharpref.getInt("anhoFinPrevioGuardado", -1);
    }

    @SuppressLint("SetTextI18n")
    private void onDateSet(DatePicker datePicker, int anho, int mes, int dia) {
        diaFin = dia;
        mesFin = mes;
        anhoFin = anho;
        textoFechaFin.setText(diaFin + "/" + (mesFin + 1) + "/" + anhoFin);
    }
}