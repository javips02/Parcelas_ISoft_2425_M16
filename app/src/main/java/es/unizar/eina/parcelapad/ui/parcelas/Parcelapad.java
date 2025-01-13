package es.unizar.eina.parcelapad.ui.parcelas;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import es.unizar.eina.parcelapad.R;
import es.unizar.eina.parcelapad.database.parcelas.Parcela;

import java.util.Objects;

/** Pantalla principal de la aplicación Parcelapad */
public class Parcelapad extends AppCompatActivity {
    private ParcelaViewModel mParcelaViewModel;
    public static final int EDIT_ID = Menu.FIRST;
    public static final int DELETE_ID = Menu.FIRST+1;

    RecyclerView mRecyclerView;
    ParcelaListAdapter mAdapter;
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcelapad);

        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new ParcelaListAdapter(new ParcelaListAdapter.ParcelaDiff());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mParcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);

        mParcelaViewModel.getAllParcelas().observe(this, parcelas -> {
            mAdapter.submitList(parcelas);
        });

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(view -> createParcela());

        // Registrar menú contextual para los elementos de la lista
        registerForContextMenu(mRecyclerView);
    }

    // Inflar el menú de la barra superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_parcelapad, menu); //nombre del archivo xml en res/menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.filter_name) {
            Toast.makeText(this, "Filtrando por nombre...", Toast.LENGTH_SHORT).show();
            // Lógica para filtrar por nombre
            mParcelaViewModel.getAllParcelasByName().observe(this, parcelas -> {
                mAdapter.submitList(parcelas);
            });
            return true;
        } else if (itemId == R.id.filter_occupants) {
            Toast.makeText(this, "Filtrando por número de ocupantes...", Toast.LENGTH_SHORT).show();
            // Lógica para filtrar por número de ocupantes
            mParcelaViewModel.getAllParcelasByOcupantes().observe(this, parcelas -> {
                mAdapter.submitList(parcelas);
            });
            return true;
        } else if (itemId == R.id.filter_price) {
            Toast.makeText(this, "Filtrando por precio (ascendente)...", Toast.LENGTH_SHORT).show();
            // Lógica para filtrar por precio ascendente
            mParcelaViewModel.getAllParcelasByPrecio().observe(this, parcelas -> {
                mAdapter.submitList(parcelas);
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
            inflater.inflate(R.menu.context_menu_parcelapad, menu); // Menú contextual para editar y eliminar
        }
    }

    // Manejar las opciones del menú contextual
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Parcela current = mAdapter.getCurrent();
        int itemId = item.getItemId();
        if (itemId == R.id.edit_parcela) {
            editParcela(current);
            return true;
        } else if (itemId == R.id.delete_parcela) {
            Toast.makeText(
                    getApplicationContext(),
                    "Eliminando " + current.getNombre(),
                    Toast.LENGTH_LONG).show();
            mParcelaViewModel.delete(current);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createParcela() {
        mStartCreateParcela.launch(new Intent(this, ParcelaEdit.class));
    }

    private void editParcela(Parcela current) {
        Intent intent = new Intent(this, ParcelaEdit.class);
        intent.putExtra(ParcelaEdit.NOMBRE_PARCELA, current.getNombre());
        intent.putExtra(ParcelaEdit.DESC_PARCELA, current.getDesc());
        intent.putExtra(String.valueOf(ParcelaEdit.MAX_OCUPANTES), current.getMaxOcupantes());
        intent.putExtra(String.valueOf(ParcelaEdit.PRECIO_PARCELA), current.getPrecioParcela());
        mStartUpdateParcela.launch(intent);
    }

    ActivityResultLauncher<Intent> mStartCreateParcela = newActivityResultLauncher(new ExecuteActivityResult() {
        @Override
        public void process(Bundle extras, Parcela parcela) {
            mParcelaViewModel.insert(parcela);
        }
    });

    ActivityResultLauncher<Intent> mStartUpdateParcela = newActivityResultLauncher(new ExecuteActivityResult() {
        @Override
        public void process(Bundle extras, Parcela parcela) {
            String originalNombre = extras.getString("original_nombre"); // Recuperar el nombre original
            if (originalNombre != null && !originalNombre.equals(parcela.getNombre())) {
                // Actualización del nombre, manejar el caso en el ViewModel
                Toast.makeText(getApplicationContext(),
                        "Actualizando parcela (Nombre original: " + originalNombre +
                                ", Nuevo nombre: " + parcela.getNombre() + ")",
                        Toast.LENGTH_LONG).show();
            }
            mParcelaViewModel.updateWithOriginalName(parcela, originalNombre);
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
                        Parcela parcela = new Parcela(Objects.requireNonNull(extras.getString(ParcelaEdit.NOMBRE_PARCELA)),
                                extras.getString(ParcelaEdit.DESC_PARCELA),
                                extras.getInt(String.valueOf(ParcelaEdit.MAX_OCUPANTES)),
                                extras.getDouble(String.valueOf(ParcelaEdit.PRECIO_PARCELA)));
                        executable.process(extras, parcela);
                    }
                });
    }
}

interface ExecuteActivityResult {
    void process(Bundle extras, Parcela parcela);
}
