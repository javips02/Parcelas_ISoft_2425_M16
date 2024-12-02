package es.unizar.eina.welcome;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import es.unizar.eina.notepad.R;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // Hace referencia al layout XML que creaste
    }
}

