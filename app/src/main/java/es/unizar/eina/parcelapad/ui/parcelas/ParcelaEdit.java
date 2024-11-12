package es.unizar.eina.parcelapad.ui.parcelas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.unizar.eina.parcelapad.R;

/** Pantalla utilizada para la creación o edición de una nota */
public class ParcelaEdit extends AppCompatActivity {

    public static final String NOMBRE_PARCELA = "nombre";
    public static final String DESC_PARCELA = "desc";
    public static final int MAX_OCUPANTES = "maxOcupantes";
    public static final double PRECIO_PARCELA = "precio";

    private EditText mNombreText;

    private EditText mDescText;

    private EditText mMaxOcupantesText;

    private EditText mPrecioText;

    private Integer mRowId; //TODO: revisar esta variable, quizás sea "nombre" lo que queremos coger aquí

    Button mSaveParcelaButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcelaedit);

        mNombreText = findViewById(R.id.nombre);
        mDescText = findViewById(R.id.desc);
        mMaxOcupantesText = findViewById(R.id.maxOcupantes);
        mPrecioText = findViewById(R.id.precio);

        mSaveParcelaButtonButton = findViewById(R.id.button_save);
        mSaveParcelaButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mNombreText.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
                Toast.makeText(getApplicationContext(), R.string.empty_not_saved_parcela, Toast.LENGTH_LONG).show();
            } else {
                replyIntent.putExtra(ParcelaEdit.NOMBRE_PARCELA, mNombreText.getText().toString());
                replyIntent.putExtra(ParcelaEdit.DESC_PARCELA, mDescText.getText().toString());
                replyIntent.putExtra(String.valueOf(ParcelaEdit.MAX_OCUPANTES), Integer.parseInt(mMaxOcupantesText.getText().toString()));
                replyIntent.putExtra(String.valueOf(ParcelaEdit.PRECIO_PARCELA), Float.parseFloat(mPrecioText.getText().toString()));
                if (mRowId!=null) {
                    replyIntent.putExtra(ParcelaEdit.NOMBRE_PARCELA, mRowId.intValue());
                }
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });

        populateFields();

    }

    private void populateFields () {
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            mNombreText.setText(extras.getString(ParcelaEdit.NOMBRE_PARCELA));
            mDescText.setText(extras.getString(ParcelaEdit.DESC_PARCELA));
            mMaxOcupantesText.setText(extras.getString(String.valueOf(ParcelaEdit.MAX_OCUPANTES)));
            mPrecioText.setText(extras.getString(String.valueOf(ParcelaEdit.PRECIO_PARCELA)));
        }
    }

}
