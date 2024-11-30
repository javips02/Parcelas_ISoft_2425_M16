package es.unizar.eina.reservapad.ui.reservas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.unizar.eina.notepad.R;
import es.unizar.eina.reservapad.database.reservas.Reserva;


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservaedit);

        mNombreCliente = findViewById(R.id.nombreCliente);
        mTlfCliente = findViewById(R.id.tlfCliente);
        mFEntrada = findViewById(R.id.fecha_entrada);
        mFSalida = findViewById(R.id.fecha_salida);

        mSaveReservaButton = findViewById(R.id.button_save);
        mSaveReservaButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mNombreCliente.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
                Toast.makeText(getApplicationContext(), R.string.empty_not_saved_parcela, Toast.LENGTH_LONG).show();
            } else {
                replyIntent.putExtra(ReservaEdit.NOMBRE_CLIENTE, mNombreCliente.getText().toString());
                replyIntent.putExtra(ReservaEdit.TLF_CLIENTE, mTlfCliente.getText().toString()); //REVISAR
                replyIntent.putExtra(ReservaEdit.FECHA_ENTRADA, Integer.parseInt(mFEntrada.getText().toString())); //REVISAR
                replyIntent.putExtra(ReservaEdit.FECHA_SALIDA, Double.parseDouble(mFSalida.getText().toString())); //REVISAR
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
        populateFields();
    }

    private void populateFields () {
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            mNombreCliente.setText(extras.getString(ReservaEdit.NOMBRE_CLIENTE));
            mTlfCliente.setText(extras.getString(ReservaEdit.TLF_CLIENTE));
            mFEntrada.setText(String.valueOf(extras.getInt(ReservaEdit.FECHA_ENTRADA, 01/01/2000))); //REVISAR
            mFSalida.setText(String.valueOf(extras.getDouble(ReservaEdit.FECHA_SALIDA, 02/01/2000))); //REVISAR
        }
    }

}
