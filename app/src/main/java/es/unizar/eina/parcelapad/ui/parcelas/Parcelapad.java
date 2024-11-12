package es.unizar.eina.parcelapad.ui.parcelas;

import static android.os.Build.VERSION_CODES.R;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.unizar.eina.parcelapad.database.parcelas.Parcela;

import static androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;

/** Pantalla principal de la aplicación Parcelapad */
public class Parcelapad extends AppCompatActivity {
    private ParcelaViewModel mParcelaViewModel;

    static final int INSERT_ID = Menu.FIRST;
    static final int DELETE_ID = Menu.FIRST + 1;
    static final int EDIT_ID = Menu.FIRST + 2;

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
            // Update the cached copy of the parcelas in the adapter.
            mAdapter.submitList(parcelas);
        });

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(view -> createParcela());

        // It doesn't affect if we comment the following instruction
        registerForContextMenu(mRecyclerView);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.add_parcela);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createParcela();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

     public boolean onContextItemSelected(MenuItem item) {
        Parcela current = mAdapter.getCurrent();
        switch (item.getItemId()) {
            case DELETE_ID:
                Toast.makeText(
                        getApplicationContext(),
                        "Deleting " + current.getNombre(),
                        Toast.LENGTH_LONG).show();
                mParcelaViewModel.delete(current);
                return true;
            case EDIT_ID:
                editParcela(current);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createParcela() {
        mStartCreateParcela.launch(new Intent(this, ParcelaEdit.class));
    }

    ActivityResultLauncher<Intent> mStartCreateParcela = newActivityResultLauncher(new ExecuteActivityResult() {
        @Override
        public void process(Bundle extras, Parcela parcela) {
            mParcelaViewModel.insert(parcela);
        }
    });

    ActivityResultLauncher<Intent> newActivityResultLauncher(ExecuteActivityResult executable) {
        return registerForActivityResult(
                new StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        Parcela parcela = new Parcela(extras.getString(ParcelaEdit.NOMBRE_PARCELA),
                                extras.getString(ParcelaEdit.DESC_PARCELA),
                                extras.getInt(String.valueOf(ParcelaEdit.MAX_OCUPANTES)),
                                extras.getDouble(String.valueOf(ParcelaEdit.PRECIO_PARCELA)));
                        executable.process(extras, parcela);
                    }
                });
    }

    private void editParcela(Parcela current) { //TODO: revisar que estamos pasando los tipos de datos correctos a la BD cuando depuremos
        Intent intent = new Intent(this, ParcelaEdit.class);
        intent.putExtra(ParcelaEdit.NOMBRE_PARCELA, current.getNombre());
        intent.putExtra(ParcelaEdit.DESC_PARCELA, current.getDesc());
        intent.putExtra(String.valueOf(ParcelaEdit.MAX_OCUPANTES), current.getMaxOcupantes());
        intent.putExtra(String.valueOf(ParcelaEdit.PRECIO_PARCELA), current.getPrecioParcela());
        mStartUpdateParcela.launch(intent);
    }

    ActivityResultLauncher<Intent> mStartUpdateParcela = newActivityResultLauncher(new ExecuteActivityResult() {
        @Override
        public void process(Bundle extras, Parcela parcela) {
            String id = extras.getString(ParcelaEdit.NOMBRE_PARCELA);
            assert id != null;
            parcela.setNombre(id);
            mParcelaViewModel.update(parcela);
        }
    });

}

interface ExecuteActivityResult {
    void process(Bundle extras, Parcela parcela);
}