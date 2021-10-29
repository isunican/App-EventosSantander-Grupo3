package com.isunican.eventossantander.view.events;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.presenter.events.EventsPresenter;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
import com.isunican.eventossantander.view.info.InfoActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventsActivity extends AppCompatActivity implements IEventsContract.View, View.OnClickListener {

    private IEventsContract.Presenter presenter;

    // Declaramos campos para enlazar con widgets del layout
    private  ArrayList<String> selectedItems;
    private  ArrayList<String> selectedItemsFinales;
    private  boolean[] checked;

    private int posi;
    private int posChecked;

    private final String APLICAR = "Aplicar";
    private final String CANCELAR = "Cancelar";


    private List<String> tipostotales;
    private List<String> tiposSeleccionados;
    private List<String> tiposSeleccionadosPrevio;

    // Declaramos los campos para la filtrar por fecha.
    private int dia,mes,ano;
    // Variables para guardar las fechas seleccionadas
    private String fechaIni;
    private String fechaFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // es importante llamar siempre al método de la clase padre, para inicializar
        // correctamente
        super.onCreate(savedInstanceState);
        // instancia la interfaz definida en el Layout activity_main.xml
        setContentView(R.layout.activity_main);

        // Enlazamos con los widgets del layout

        Button btnOrdenar = findViewById(R.id.btn_ordenar);

        // Asignamos los listeners para los botones
        btnOrdenar.setOnClickListener(this);

        // Creamos objeto presenter para cargar los datos del modelo y mostrarlos en la vista
        //enlazamos con el layout y asignamos listener para el boton de filtrar
        Button btnFiltrar = findViewById(R.id.btn_filtrar);
        btnFiltrar.setOnClickListener(this);

        presenter = new EventsPresenter(this);
        tiposSeleccionadosPrevio= new ArrayList<>();
    }


    @Override
    public void onEventsLoaded(List<Event> events) {
        EventArrayAdapter adapter = new EventArrayAdapter(EventsActivity.this, 0, events);
        ListView listView = findViewById(R.id.eventsListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            presenter.onEventClicked(position);
        });
    }

    @Override
    public void onLoadError() {
        //Error de carga
    }

    @Override
    public void onLoadSuccess(int elementsLoaded) {
        String text = String.format("Loaded %d events", elementsLoaded);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
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

    public IEventsContract.Presenter getPresenter() {
        return presenter;
    }

    /*
    Menu Handling
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                tiposSeleccionadosPrevio.clear();
                presenter.onReloadClicked();
                return true;
            case R.id.menu_info:
                presenter.onInfoClicked();
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
        tipostotales = new ArrayList<>();
        anhadirTiposeventos(tipostotales);
        tipostotales.toArray(new String[tipostotales.size()]);

        tiposSeleccionados = new ArrayList<>();

        //Creamos una AlertDialog
        //Creamos una AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Le ponemos un titulo
        builder.setTitle("Filtrar");

        //Comprobamos los elementos seleccionados previamente
            checked= new boolean[tipostotales.size()];
            posChecked=0;
            for(int i=0 ; i<checked.length;i++){
                checked[i]=false;
            }
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
        builder.setMultiChoiceItems(tipostotales.toArray(new String[tipostotales.size()]), checked, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which,
                                boolean estaMarcado) {
                if (estaMarcado) {
                    // If the user checked the item, add it to the selected items
                    tiposSeleccionados.add(tipostotales.get(which));
                    tiposSeleccionadosPrevio=tiposSeleccionados;
                } else if (tiposSeleccionados.contains(tipostotales.get(which))) {
                    // Else, if the item is already in the array, remove it
                    tiposSeleccionados.remove(tipostotales.get(which));
                    tiposSeleccionadosPrevio=tiposSeleccionados;
                }
            }
        });

        //Creamos el boton de aplicar
        builder.setPositiveButton(APLICAR, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.onFiltrarClicked(tiposSeleccionados);
            }
        });
        builder.setNegativeButton(CANCELAR,  new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //si se cancela no se hace nada
            }
        });

        return builder.create();
    }


    public AlertDialog onFilterAlertDialogOrdenar(){
        //Creamos una lista donde meter los eventos que cumplan el filtro
        List eventosFiltrados = new ArrayList<Event>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] array = {"Ascendente(A-Z)", "Descendente(Z-A)"};
        builder.setTitle("Ordenar");

        builder.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                posi = i;
            }
        });
                // Set the action buttons
        builder.setPositiveButton(APLICAR, (dialog, id) -> {
            // User clicked OK, so save the selectedItems results somewhere
            // or return them to the component that opened the dialog
            presenter.onOrdenarCategoriaClicked(posi);
            selectedItemsFinales = selectedItems;
        });
        builder.setNegativeButton(CANCELAR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //selectedItems.clear();

                    }
                });
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
    //TODO
    public AlertDialog onDateFilterAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Duaración del viaje");
        builder.setMessage("Fecha comienzo:");
        builder.setMessage("Fecha finalización:");
        //Botones para introducir fechas
        builder.setNeutralButton("Fecha comienzo", new DialogInterface.OnClickListener() {
            //TODO
            //no se si esto esta bien
            private Context DatePickerDialog;

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                ano = c.get(Calendar.YEAR);
                //TODO
                //En vez de DatePickerDialog en el ejemplo pone This
                DatePickerDialog fechaIniDialog = new DatePickerDialog(DatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        fechaIni = (dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }
                        , dia, mes, ano);
                fechaIniDialog.show();
            }
        });
        builder.setNeutralButton("Fecha finalización:", new DialogInterface.OnClickListener() {
            //TODO
            //no se si esto esta bien
            private Context DatePickerDialog;
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                ano = c.get(Calendar.YEAR);
                //TODO
                //En vez de DatePickerDialog en el ejemplo pone This
                DatePickerDialog fechaFinDialog = new DatePickerDialog(DatePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        fechaFin = (dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }
                        , dia, mes, ano);
                fechaFinDialog.show();
            }
        });


        builder.setPositiveButton(APLICAR, (dialog, id) -> {
                    // User clicked OK, so save the selectedItems results somewhere
                    // or return them to the component that opened the dialog

                });
        builder.setNegativeButton(CANCELAR, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //selectedItems.clear();

            }
        });
        return builder.create();
    }
}