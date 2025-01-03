package es.unizar.eina.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import es.unizar.eina.notepad.R;
import es.unizar.eina.parcelapad.ui.parcelas.Parcelapad;
import es.unizar.eina.reservapad.ui.reservas.Reservapad;
import es.unizar.eina.welcome.db.UnifiedRoomDatabase;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // Hace referencia al layout XML que creaste

        UnifiedRoomDatabase database = UnifiedRoomDatabase.getDatabase(getApplicationContext());

        // Obtén el botón de Parcelas
        Button parcelasButton = findViewById(R.id.button_parcelas);

        // Configura el listener para el botón
        parcelasButton.setOnClickListener(view -> {
            // Crea un Intent para lanzar la actividad Parcelapad
            Intent intent = new Intent(WelcomeActivity.this, Parcelapad.class);
            startActivity(intent);
        });

        Button reservasButton = findViewById(R.id.button_reservas);
        reservasButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeActivity.this, Reservapad.class);
            startActivity(intent);
        });
    }
}
