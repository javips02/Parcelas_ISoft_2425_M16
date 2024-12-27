package es.unizar.eina.reservapad.ui.reservas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import es.unizar.eina.notepad.R;


/** Pantalla utilizada para la creación o edición de una nota */
public class ReservaEdit extends AppCompatActivity {

    public static final String NOMBRE_CLIENTE = "nombreCliente";
    public static final String TLF_CLIENTE = "tlfCliente";
    public static final String FECHA_ENTRADA = "fecha_entrada";
    public static final String FECHA_SALIDA = "fecha_salida";

    private EditText mNombreCliente;
    private EditText mTlfCliente;
    private EditText mFEntrada;
    private EditText mFSalida;

    Button mSaveReservaButton;
    Button mAddParcelaButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservaedit);

        mNombreCliente = findViewById(R.id.nombreCliente);
        mTlfCliente = findViewById(R.id.tlfCliente);
        mFEntrada = findViewById(R.id.fecha_entrada);
        mFSalida = findViewById(R.id.fecha_salida);

        mSaveReservaButton = findViewById(R.id.button_save_reserva);
        mSaveReservaButton.setOnClickListener(view -> {
            // Validar campos antes de guardar
            if (validateFields()) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra(ReservaEdit.NOMBRE_CLIENTE, mNombreCliente.getText().toString());
                replyIntent.putExtra(ReservaEdit.TLF_CLIENTE, Integer.parseInt(mTlfCliente.getText().toString()));
                replyIntent.putExtra(ReservaEdit.FECHA_ENTRADA, mFEntrada.getText().toString());
                replyIntent.putExtra(ReservaEdit.FECHA_SALIDA, mFSalida.getText().toString());

                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

        //Creo listener para el botón de añadir parcela para que me lleve a la actividad de añadir parcela
        mAddParcelaButton = findViewById(R.id.button_add_parcela);
        mAddParcelaButton.setOnClickListener(view -> {
            Intent intent = new Intent(ReservaEdit.this, ParcelaEnReservaEdit.class);
            startActivity(intent);
        });

        populateFields();
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
