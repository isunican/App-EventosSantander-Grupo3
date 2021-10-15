package com.isunican.eventossantander.view.events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.isunican.eventossantander.R;
import com.isunican.eventossantander.model.Event;
import com.isunican.eventossantander.presenter.events.EventsPresenter;
import com.isunican.eventossantander.view.eventsdetail.EventsDetailActivity;
import com.isunican.eventossantander.view.info.InfoActivity;
import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity implements IEventsContract.View, View.OnClickListener {

    private IEventsContract.Presenter presenter;
    private EventArrayAdapter adapter;

    // Declaramos campos para enlazar con widgets del layout
    private Button btnOrdenar;
    private  ArrayList selectedItems;
    private  ArrayList selectedItemsFinales;

    private Button btnFiltrar;
    private int posi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // es importante llamar siempre al método de la clase padre, para inicializar
        // correctamente
        super.onCreate(savedInstanceState);
        // instancia la interfaz definida en el Layout activity_main.xml
        setContentView(R.layout.activity_main);

        // Enlazamos con los widgets del layout

        btnOrdenar = findViewById(R.id.btn_ordenar);

        // Asignamos los listeners para los botones
        btnOrdenar.setOnClickListener(this);

        // Creamos objeto presenter para cargar los datos del modelo y mostrarlos en la vista
        //enlazamos con el layout y asignamos listener para el boton de filtrar
        btnFiltrar = findViewById(R.id.btn_filtrar);
        btnFiltrar.setOnClickListener(this);

        presenter = new EventsPresenter(this);
    }


    @Override
    public void onEventsLoaded(List<Event> events) {
        adapter = new EventArrayAdapter(EventsActivity.this, 0, events);
        ListView listView = findViewById(R.id.eventsListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            presenter.onEventClicked(position);
        });
    }

    @Override
    public void onLoadError() {

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

    @Override
    public AlertDialog onFilterAlertDialog(){
        //Creamos una lista donde meter los eventos que cumplan el filtro
        List eventosFiltrados = new ArrayList<Event>();

        //Creamos una AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Le ponemos un titulo
        builder.setTitle("Filtrar");

        //Creamos los elementos de la seleccion de tipo multiple
        //builder.setMultiChoiceItems();

        return builder.create();
    }

    public AlertDialog onFilterAlertDialogOrdenar(){
        //Creamos una lista donde meter los eventos que cumplan el filtro
        List eventosFiltrados = new ArrayList<Event>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] array = {"Ascendente(A-Z)", "Descendente(Z-A)"};
        builder.setTitle("Ordenar");
        //int checkedItem, final DialogInterface.OnClickListener listener
//        builder.setSingleChoiceItems(array, null, new DialogInterface.OnClickListener() {
 //                   @Override
 //                   public void onClick(DialogInterface dialogInterface, int i) {
  //                      posi = i;
   //                 }
    //            });
        builder.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                posi = i;
            }
        });
                // Set the action buttons
        builder.setPositiveButton("Aplicar", (dialog, id) -> {
            // User clicked OK, so save the selectedItems results somewhere
            // or return them to the component that opened the dialog
            presenter.onOrdenarCategoriaClicked(posi);
            selectedItemsFinales = selectedItems;
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //selectedItems.clear();

                    }
                });
        return builder.create();
    }
}