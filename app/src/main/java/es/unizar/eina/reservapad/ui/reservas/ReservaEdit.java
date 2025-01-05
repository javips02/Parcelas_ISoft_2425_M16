package es.unizar.eina.reservapad.ui.reservas;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import es.unizar.eina.notepad.R;
import es.unizar.eina.reservapad.database.reservas.ParcelaEnReserva;
import es.unizar.eina.parcelapad.database.parcelas.Parcela;



/** Pantalla utilizada para la creación o edición de una nota */
public class ReservaEdit extends AppCompatActivity {

    public static final String NOMBRE_CLIENTE = "nombreCliente";
    public static final String TLF_CLIENTE = "tlfCliente";
    public static final String FECHA_ENTRADA = "fecha_entrada";
    public static final String FECHA_SALIDA = "fecha_salida";
    public static final String RESERVA_ID = "reservaId";

    private EditText mNombreCliente;
    private EditText mTlfCliente;
    private EditText mFEntrada;
    private EditText mFSalida;
    private int reservaId;

    Button mSaveReservaButton;
    Button mAddParcelaButton;


    private ParcelaEnReservaViewModel mParcelaEnReservaViewModel;
    private LinearLayout listaParcelasContainer;

    private List<ParcelaEnReserva> parcelasAInsertar = new ArrayList<>();
    private List<ParcelaEnReserva> parcelasAEliminar = new ArrayList<>();

    private ActivityResultLauncher<Intent> parcelaResultLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservaedit);

        mNombreCliente = findViewById(R.id.nombreCliente);
        mTlfCliente = findViewById(R.id.tlfCliente);
        mFEntrada = findViewById(R.id.fecha_entrada);
        mFSalida = findViewById(R.id.fecha_salida);
        listaParcelasContainer = findViewById(R.id.listaParcelasContainer);
        reservaId = getIntent().getIntExtra(RESERVA_ID, -1); // Recupera correctamente

        // Inicializar ViewModel
        mParcelaEnReservaViewModel = new ViewModelProvider(this).get(ParcelaEnReservaViewModel.class);

        // Observar la lista de parcelas en reserva
        mParcelaEnReservaViewModel.getParcelasByReserva(reservaId).observe(this, this::actualizarListaParcelas);


        mSaveReservaButton = findViewById(R.id.button_save_reserva);
        mSaveReservaButton.setOnClickListener(view -> {
            // Validar campos antes de guardar
            if (validateFields()) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra(ReservaEdit.NOMBRE_CLIENTE, mNombreCliente.getText().toString());
                replyIntent.putExtra(ReservaEdit.TLF_CLIENTE, Integer.parseInt(mTlfCliente.getText().toString()));
                replyIntent.putExtra(ReservaEdit.FECHA_ENTRADA, mFEntrada.getText().toString());
                replyIntent.putExtra(ReservaEdit.FECHA_SALIDA, mFSalida.getText().toString());
                replyIntent.putExtra("parcelasAInsertar", new ArrayList<>(parcelasAInsertar));
                replyIntent.putExtra("parcelasAEliminar", new ArrayList<>(parcelasAEliminar));

                setResult(RESULT_OK, replyIntent);
                finish();
            }

        });

        // Inicializar el launcher para manejar resultados
        parcelaResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // Recuperar listas desde el resultado
                            List<Parcela> nuevasParcelasAInsertar =
                                    (List<Parcela>) data.getSerializableExtra("parcelasAInsertar");
                            List<Parcela> nuevasParcelasAEliminar =
                                    (List<Parcela>) data.getSerializableExtra("parcelasAEliminar");

                            if (nuevasParcelasAInsertar != null) {
                                for (Parcela parcela : nuevasParcelasAInsertar) {
                                    ParcelaEnReserva parcelaEnReserva = new ParcelaEnReserva(
                                            parcela.getNombre(),
                                            reservaId,
                                            parcela.getMaxOcupantes(),
                                            parcela.getPrecioParcela()
                                    );
                                    parcelasAInsertar.add(parcelaEnReserva);
                                }
                            }

                            if (nuevasParcelasAEliminar != null) {
                                for (Parcela parcela : nuevasParcelasAEliminar) {
                                    ParcelaEnReserva parcelaEnReserva = new ParcelaEnReserva(
                                            parcela.getNombre(),
                                            reservaId,
                                            parcela.getMaxOcupantes(),
                                            parcela.getPrecioParcela()
                                    );
                                    parcelasAEliminar.add(parcelaEnReserva);
                                }
                            }

                            mParcelaEnReservaViewModel.getParcelasByReserva(reservaId).observe(this, this::actualizarListaParcelas);
                            // Actualiza la vista de parcelas si es necesario
                            actualizarListaParcelasInsertar(parcelasAInsertar);
                        }
                    }
                }
        );

        //Creo listener para el botón de añadir parcela para que me lleve a la actividad de añadir parcela
        mAddParcelaButton = findViewById(R.id.button_add_parcela);
        mAddParcelaButton.setOnClickListener(view -> {
            Intent intent = new Intent(ReservaEdit.this, ParcelaEnReservaEdit.class);
            intent.putExtra("reservaId", reservaId);
            parcelaResultLauncher.launch(intent);
        });

        populateFields();
    }

    private void actualizarListaParcelas(List<ParcelaEnReserva> parcelas) {
        //listaParcelasContainer.removeAllViews(); // Limpiar el contenedor antes de agregar nuevos elementos

        LayoutInflater inflater = LayoutInflater.from(this);
        for (ParcelaEnReserva parcela : parcelas) {
            // Verificar si la parcela está en la lista de eliminaciones
            if (!parcelasAEliminar.contains(parcela)) {
                // Inflar la vista solo si no está marcada para eliminación
                View parcelaView = inflater.inflate(R.layout.parcela_de_reserva, listaParcelasContainer, false);

                // Configurar los datos de la parcela
                TextView nombreParcela = parcelaView.findViewById(R.id.nombreParcela);
                TextView numeroOcupantes = parcelaView.findViewById(R.id.numeroOcupantes);
                TextView precioParcela = parcelaView.findViewById(R.id.precioParcela);

                nombreParcela.setText(parcela.getParcelaNombre());
                numeroOcupantes.setText(String.valueOf(parcela.getOcupantes()));
                precioParcela.setText(String.format("€%.2f", parcela.getPrecio()));

                // Agregar la vista inflada al contenedor
                listaParcelasContainer.addView(parcelaView);
            }
        }
    }


    private void actualizarListaParcelasInsertar(List<ParcelaEnReserva> parcelasAInsertar) {
        Toast.makeText(this, "Actualizando lista de parcelas...", Toast.LENGTH_SHORT).show();
        listaParcelasContainer.removeAllViews();
        List<ParcelaEnReserva> parcelas = mParcelaEnReservaViewModel.getParcelasByReserva(reservaId).getValue();
        LayoutInflater inflater = LayoutInflater.from(this);
        // Mostrar también las parcelas en la lista de inserciones
        for (ParcelaEnReserva parcela : parcelasAInsertar) {
            View parcelaView = inflater.inflate(R.layout.parcela_de_reserva, listaParcelasContainer, false);

            TextView nombreParcela = parcelaView.findViewById(R.id.nombreParcela);
            TextView numeroOcupantes = parcelaView.findViewById(R.id.numeroOcupantes);
            TextView precioParcela = parcelaView.findViewById(R.id.precioParcela);

            nombreParcela.setText(parcela.getParcelaNombre());
            numeroOcupantes.setText(String.valueOf(parcela.getOcupantes()));
            precioParcela.setText(String.format("€%.2f", parcela.getPrecio()));

            // Mostrar solo el nombre de la parcela si está en la lista de inserciones
            listaParcelasContainer.addView(parcelaView);
        }

    }


    private void populateFields () {
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            mNombreCliente.setText(extras.getString(ReservaEdit.NOMBRE_CLIENTE));
            mTlfCliente.setText(String.valueOf(extras.getInt(ReservaEdit.TLF_CLIENTE, 000000000)));
            mFEntrada.setText(String.valueOf(extras.getString(ReservaEdit.FECHA_ENTRADA, "01/01/2000"))); //REVISAR
            mFSalida.setText(String.valueOf(extras.getString(ReservaEdit.FECHA_SALIDA, "02/01/2000"))); //REVISAR
        }
    }

    private boolean validateFields() {
        // Validar que el nombre no esté vacío
        if (TextUtils.isEmpty(mNombreCliente.getText())) {
            Toast.makeText(this, "El nombre del cliente no puede estar vacío.", Toast.LENGTH_LONG).show();
            return false;
        }

        // Validar que el teléfono sea numérico y tenga una longitud válida
        String telefono = mTlfCliente.getText().toString();
        if (TextUtils.isEmpty(telefono) || !telefono.matches("\\d{9}")) {
            Toast.makeText(this, "Introduce un número de teléfono válido (9 dígitos).", Toast.LENGTH_LONG).show();
            return false;
        }

        // Validar que las fechas no estén vacías y tengan un formato correcto
        String fechaEntrada = mFEntrada.getText().toString();
        String fechaSalida = mFSalida.getText().toString();
        if (TextUtils.isEmpty(fechaEntrada) || !fechaEntrada.matches("\\d{2}/\\d{2}/\\d{4}")) {
            Toast.makeText(this, "Introduce una fecha de entrada válida (dd/MM/yyyy).", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(fechaSalida) || !fechaSalida.matches("\\d{2}/\\d{2}/\\d{4}")) {
            Toast.makeText(this, "Introduce una fecha de salida válida (dd/MM/yyyy).", Toast.LENGTH_LONG).show();
            return false;
        }

        // Validar que la fecha de salida sea posterior a la de entrada
        if (!isDateValid(fechaEntrada, fechaSalida)) {
            Toast.makeText(this, "La fecha de salida debe ser posterior a la fecha de entrada.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean isDateValid(String fechaEntrada, String fechaSalida) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date entrada = dateFormat.parse(fechaEntrada);
            Date salida = dateFormat.parse(fechaSalida);
            return entrada != null && salida != null && salida.after(entrada);
        } catch (ParseException e) {
            return false;
        }
    }


}
