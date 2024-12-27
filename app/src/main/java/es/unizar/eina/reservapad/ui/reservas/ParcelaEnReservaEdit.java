package es.unizar.eina.reservapad.ui.reservas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import es.unizar.eina.notepad.R;
import es.unizar.eina.reservapad.database.reservas.ParcelaEnReserva;
import es.unizar.eina.reservapad.ui.reservas.ParcelaEnReservaViewModel;

public class ParcelaEnReservaEdit extends AppCompatActivity {

    private ParcelaEnReservaViewModel parcelaViewModel;
    private LinearLayout listaParcelasContainer;
    private Integer reservaId; // ID de la reserva actual
    private String fechaEntrada; // Fecha de entrada de la reserva
    private String fechaSalida; // Fecha de salida de la reserva

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcela_add_reserva);

        listaParcelasContainer = findViewById(R.id.listaParcelasContainer);
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaEnReservaViewModel.class);

        // Obtén los datos de la reserva desde el Intent o ViewModel
        reservaId = getIntent().getIntExtra("reservaId", -1);
        fechaEntrada = getIntent().getStringExtra("fechaEntrada");
        fechaSalida = getIntent().getStringExtra("fechaSalida");

        loadParcelas();
    }

    private void loadParcelas() {
        // Obtén las parcelas asociadas y las disponibles
        parcelaViewModel.getAllParcelasEnReserva().observe(this, parcelas -> {
            listaParcelasContainer.removeAllViews();
            for (ParcelaEnReserva parcela : parcelas) {
                boolean isAsignada = parcela.getReservaID() == reservaId;
                boolean isDisponible = isParcelaDisponible(parcela, fechaEntrada, fechaSalida);

                View parcelaView = getLayoutInflater().inflate(R.layout.item_lista_parcelas_reserva, listaParcelasContainer, false);

                TextView nombreParcela = parcelaView.findViewById(R.id.nombreParcela);
                TextView capacidadParcela = parcelaView.findViewById(R.id.capacidadParcela);
                TextView precioParcela = parcelaView.findViewById(R.id.precioParcela);
                Button buttonRojo = parcelaView.findViewById(R.id.button_rojo);
                Button buttonVerde = parcelaView.findViewById(R.id.button_verde);

                nombreParcela.setText(parcela.getParcelaNombre());
                capacidadParcela.setText(String.valueOf(parcela.getOcupantes()));
                precioParcela.setText(String.format("€%.2f", parcela.getPrecio()));

                if (isAsignada) {
                    buttonRojo.setVisibility(View.VISIBLE);
                    buttonVerde.setVisibility(View.GONE);
                    buttonRojo.setOnClickListener(v -> removeParcela(parcela));
                } else if (isDisponible) {
                    buttonRojo.setVisibility(View.GONE);
                    buttonVerde.setVisibility(View.VISIBLE);
                    buttonVerde.setOnClickListener(v -> addParcela(parcela));
                }

                listaParcelasContainer.addView(parcelaView);
            }
        });
    }

    private boolean isParcelaDisponible(ParcelaEnReserva parcela, String fechaEntrada, String fechaSalida) {
        // Lógica para comprobar si la parcela está disponible en las fechas indicadas
        return parcela.getReservaID() == null; //|| !parcela.overlapsWith(fechaEntrada, fechaSalida);
    }

    private void addParcela(ParcelaEnReserva parcela) {
        parcela.setReservaID(reservaId);
        parcelaViewModel.update(parcela);
    }

    private void removeParcela(ParcelaEnReserva parcela) {
        parcela.setReservaID(null);
        parcelaViewModel.update(parcela);
    }
}
