package es.unizar.eina.reservapad.ui.reservas;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import es.unizar.eina.notepad.R;
import es.unizar.eina.parcelapad.database.parcelas.Parcela;
import es.unizar.eina.parcelapad.ui.parcelas.ParcelaEdit;
import es.unizar.eina.parcelapad.ui.parcelas.ParcelaListAdapter;
import es.unizar.eina.parcelapad.ui.parcelas.ParcelaViewModel;
import es.unizar.eina.reservapad.database.reservas.Reserva;

/** Pantalla principal de la aplicación Parcelapad */
public class Reservapad extends AppCompatActivity {
    private ReservaViewModel mReservaViewModel;
    public static final int EDIT_ID = Menu.FIRST;
    public static final int DELETE_ID = Menu.FIRST+1;

    RecyclerView mRecyclerView;
    ParcelaListAdapter mAdapter;
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservapad);

        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new ReservaListAdapter(new ReservaListAdapter.ReservaDiff());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mReservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);

        mReservaViewModel.getAllReservas().observe(this, reservas -> {
            mAdapter.submitList(reservas);
        });

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(view -> createReserva());

        // Registrar menú contextual para los elementos de la lista
        registerForContextMenu(mRecyclerView);
    }

    // Inflar el menú de la barra superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reservapad, menu); //nombre del archivo xml en res/menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.filter_name) {
            Toast.makeText(this, "Filtrando por nombre...", Toast.LENGTH_SHORT).show();
            // Lógica para filtrar por nombre
            mReservaViewModel.getAllReservasByName().observe(this, reservas -> {
                mAdapter.submitList(reservas);
            });
            return true;
        } else if (itemId == R.id.filter_occupants) {
            Toast.makeText(this, "Filtrando por número de ocupantes...", Toast.LENGTH_SHORT).show();
            // Lógica para filtrar por número de ocupantes
            mReservaViewModel.getAllReservasByOcupantes().observe(this, reservas -> {
                mAdapter.submitList(reservas);
            });
            return true;
        } else if (itemId == R.id.filter_price) {
            Toast.makeText(this, "Filtrando por precio (ascendente)...", Toast.LENGTH_SHORT).show();
            // Lógica para filtrar por precio ascendente
            mReservaViewModel.getAllReservasByPrecio().observe(this, reservas -> {
                mAdapter.submitList(reservas);
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Crear el menú contextual para el RecyclerView
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.recyclerview) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu_reservapad, menu); // Menú contextual para editar y eliminar
        }
    }

    // Manejar las opciones del menú contextual
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Parcela current = mAdapter.getCurrent();
        int itemId = item.getItemId();
        if (itemId == R.id.edit_reserva) {
            editParcela(current);
            return true;
        } else if (itemId == R.id.delete_reserva) {
            Toast.makeText(
                    getApplicationContext(),
                    "Eliminando " + current.getNombre(),
                    Toast.LENGTH_LONG).show();
            mReservaViewModel.delete(current);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createParcela() {
        mStartCreateReserva.launch(new Intent(this, ReservaEdit.class));
    }

    private void editReserva(Reserva current) { //TODO: FALTA LA LISTA DE PARCELAS
        Intent intent = new Intent(this, ReservaEdit.class);
        intent.putExtra(ReservaEdit.NOMBRE_CLIENTE, current.getNombreCliente());
        intent.putExtra(ReservaEdit.TLF_CLIENTE, current.getTlfCliente());
        intent.putExtra(ReservaEdit.FECHA_ENTRADA, current.getFechaEntrada());
        intent.putExtra(ReservaEdit.FECHA_SALIDA, current.getFechaSalida());
        intent.putExtra(String.valueOf(ReservaEdit.RESERVA_ID), current.getID());
        mStartUpdateReserva.launch(intent);
    }

    ActivityResultLauncher<Intent> mStartCreateReserva = newActivityResultLauncher(new ExecuteActivityResult() {
        @Override
        public void process(Bundle extras, Parcela parcela) {
            mReservaViewModel.insert(reserva);
        }
    });

    ActivityResultLauncher<Intent> mStartUpdateReserva = newActivityResultLauncher(new ExecuteActivityResult() {
        @Override
        public void process(Bundle extras, Reserva reserva) {
            Integer id = ReservaEdit.RESERVA_ID;
            reserva.setID(id);
            mReservaViewModel.update(reserva);
        }
    });

    private ActivityResultLauncher<Intent> newActivityResultLauncher(ExecuteActivityResult executable) {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
                        Bundle extras = result.getData().getExtras();
                        assert extras != null;
                        Reserva reserva = new Reserva( //TODO: agnadir lista parcelas
                                Objects.requireNonNull(extras.getString(ReservaEdit.NOMBRE_CLIENTE)),
                                Objects.requireNonNull(extras.getString(ReservaEdit.TLF_CLIENTE)),
                                Objects.requireNonNull(extras.getString(ReservaEdit.FECHA_ENTRADA)),
                                extras.getString(ReservaEdit.FECHA_SALIDA),
                        executable.process(extras, reserva);
                    }
                });
    }
}

interface ExecuteActivityResult {
    void process(Bundle extras, Reserva reserva);
}
