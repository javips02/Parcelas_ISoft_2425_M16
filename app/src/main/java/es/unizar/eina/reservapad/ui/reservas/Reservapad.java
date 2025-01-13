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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import es.unizar.eina.parcelapad.R;
import es.unizar.eina.reservapad.database.reservas.ParcelaEnReserva;
import es.unizar.eina.reservapad.database.reservas.Reserva;
import es.unizar.eina.send.SendAbstractionImpl;

/** Pantalla principal de la aplicación Reservapad */
public class Reservapad extends AppCompatActivity {
    private ReservaViewModel mReservaViewModel;
    private ParcelaEnReservaViewModel mParcelaEnReservaViewModel;
    public static final int EDIT_ID = Menu.FIRST;
    public static final int DELETE_ID = Menu.FIRST+1;

    RecyclerView mRecyclerView;
    ReservaListAdapter mAdapter;
    FloatingActionButton mFab;

    SendAbstractionImpl senderWS;
    SendAbstractionImpl senderSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservapad);

        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new ReservaListAdapter(new ReservaListAdapter.ReservaDiff());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mReservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
        mParcelaEnReservaViewModel = new ViewModelProvider(this).get(ParcelaEnReservaViewModel.class);

        // Creo la instancia para los Send
        senderSMS = new SendAbstractionImpl(this, "SMS");
        senderWS = new SendAbstractionImpl(this, "WS");

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
        if (itemId == R.id.filter_nombreCliente) {
            Toast.makeText(this, "Filtrando por nombre del CLiente...", Toast.LENGTH_SHORT).show();
            // Lógica para filtrar por nombre del cliente
            mReservaViewModel.getAllReservasByNombreCliente().observe(this, reservas -> {
                mAdapter.submitList(reservas);
            });
            return true;
        } else if (itemId == R.id.filter_tlfCliente) {
            Toast.makeText(this, "Filtrando por tlf cliente...", Toast.LENGTH_SHORT).show();
            // Lógica para filtrar por telefono del cliente
            mReservaViewModel.getAllReservasByTlfCliente().observe(this, reservas -> {
                mAdapter.submitList(reservas);
            });
            return true;
        } else if (itemId == R.id.filter_fentrada) {
            Toast.makeText(this, "Filtrando por fecha de entrada (ascendente)...", Toast.LENGTH_SHORT).show();
            // Lógica para filtrar por fecha de entrada ascendente
            mReservaViewModel.getAllReservasByFEntrada().observe(this, reservas -> {
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
            inflater.inflate(R.menu.context_menu_reservapad, menu); // Menú contextual para editar, eliminar y notificar
        }
    }

    // Manejar las opciones del menú contextual
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Reserva current = mAdapter.getCurrent();
        int itemId = item.getItemId();
        if (itemId == R.id.edit_reserva) {
            editReserva(current);
            return true;
        } else if (itemId == R.id.delete_reserva) {
            Toast.makeText(
                    getApplicationContext(),
                    "Eliminando reserva con ID " + current.getID(),
                    Toast.LENGTH_LONG).show();
            mReservaViewModel.delete(current);
            return true;
        } else if (itemId == R.id.notificar_reserva_SMS) {
            // Recuperar datos de la reserva
            String phone = String.valueOf(current.getTlfCliente());
            String message = "Hola " + current.getNombreCliente() +
                    ", tu reserva desde " + current.getFechaEntrada() +
                    " hasta " + current.getFechaSalida() + " ha sido confirmada.";

            // Enviar notificación
            senderSMS.send(phone, message);

            Toast.makeText(
                    getApplicationContext(),
                    "Notificación enviada a " + current.getNombreCliente() + " por SMS",
                    Toast.LENGTH_LONG).show();
            return true;
        } else if (itemId == R.id.notificar_reserva_WS) {
            // Recuperar datos de la reserva
            String phone = String.valueOf(current.getTlfCliente());
            String message = "Hola " + current.getNombreCliente() +
                    ", tu reserva desde " + current.getFechaEntrada() +
                    " hasta " + current.getFechaSalida() + " ha sido confirmada.";

            // Enviar notificación
            senderWS.send(phone, message);

            Toast.makeText(
                    getApplicationContext(),
                    "Notificación enviada a " + current.getNombreCliente() + " por WhatsApp",
                    Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createReserva() {
        mStartCreateReserva.launch(new Intent(this, ReservaEdit.class));
    }

    private void editReserva(Reserva current) { //TODO: FALTA LA LISTA DE PARCELAS
        Intent intent = new Intent(this, ReservaEdit.class);
        intent.putExtra(ReservaEdit.RESERVA_ID, current.getID());
        intent.putExtra(ReservaEdit.NOMBRE_CLIENTE, current.getNombreCliente());
        intent.putExtra(String.valueOf(ReservaEdit.TLF_CLIENTE), current.getTlfCliente());
        intent.putExtra(ReservaEdit.FECHA_ENTRADA, current.getFechaEntrada());
        intent.putExtra(ReservaEdit.FECHA_SALIDA, current.getFechaSalida());
        mStartUpdateReserva.launch(intent);
    }

    ActivityResultLauncher<Intent> mStartCreateReserva = newActivityResultLauncher(new ExecuteActivityResult() {
        @Override
        public void process(Bundle extras, Reserva reserva) {
            long reservaId = mReservaViewModel.insertAndGetId(reserva);
            ArrayList<ParcelaEnReserva> parcelasAInsertar =
                    (ArrayList<ParcelaEnReserva>) extras.getSerializable("parcelasAInsertar");
            ArrayList<ParcelaEnReserva> parcelasAEliminar =
                    (ArrayList<ParcelaEnReserva>) extras.getSerializable("parcelasAEliminar");
            //mReservaViewModel.insert(reserva);
            // Asignar el ID de la reserva a las parcelas
            if (parcelasAInsertar != null) {
                for (ParcelaEnReserva parcela : parcelasAInsertar) {
                    parcela.setReservaID((int) reservaId); // Asegúrate de que el ID de la reserva esté asignado
                }
            }
            manejarParcelas(parcelasAInsertar, parcelasAEliminar);

            Toast.makeText(Reservapad.this, "Reserva creada correctamente", Toast.LENGTH_SHORT).show();
        }
    });

    ActivityResultLauncher<Intent> mStartUpdateReserva = newActivityResultLauncher(new ExecuteActivityResult() {
        @Override
        public void process(Bundle extras, Reserva reserva) {
            ArrayList<ParcelaEnReserva> parcelasAInsertar =
                    (ArrayList<ParcelaEnReserva>) extras.getSerializable("parcelasAInsertar");
            ArrayList<ParcelaEnReserva> parcelasAEliminar =
                    (ArrayList<ParcelaEnReserva>) extras.getSerializable("parcelasAEliminar");
            Reserva current = mAdapter.getCurrent();
            int id = current.getID();
            Toast.makeText(getApplicationContext(), "Intento actualizar reserva con ID:" + id, Toast.LENGTH_LONG).show();
            reserva.setID(id);
            Toast.makeText(getApplicationContext(), "Entro a hacer UPDATE", Toast.LENGTH_LONG).show();
            mReservaViewModel.update(reserva);
            manejarParcelas(parcelasAInsertar, parcelasAEliminar);
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
                        Reserva reserva = new Reserva(Objects.requireNonNull(extras.getString(ReservaEdit.NOMBRE_CLIENTE)),
                                extras.getInt(String.valueOf(ReservaEdit.TLF_CLIENTE)),
                                Objects.requireNonNull(extras.getString(ReservaEdit.FECHA_ENTRADA)),
                                Objects.requireNonNull(extras.getString(ReservaEdit.FECHA_SALIDA)));
                        executable.process(extras, reserva);
                    }
                });
    }

    private void manejarParcelas(List<ParcelaEnReserva> parcelasAInsertar,
                                 List<ParcelaEnReserva> parcelasAEliminar) {
        if (parcelasAInsertar != null) {
            for (ParcelaEnReserva parcela : parcelasAInsertar) {
                mParcelaEnReservaViewModel.insert(parcela);
            }
        }

        if (parcelasAEliminar != null) {
            for (ParcelaEnReserva parcela : parcelasAEliminar) {
                mParcelaEnReservaViewModel.delete(parcela);
            }
        }
    }

}

interface ExecuteActivityResult {
    void process(Bundle extras, Reserva reserva);
}
