package es.unizar.eina.reservapad.ui.reservas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.List;
import es.unizar.eina.parcelapad.R;
import es.unizar.eina.parcelapad.ui.parcelas.ParcelaViewModel;
import es.unizar.eina.parcelapad.database.parcelas.Parcela;

public class ParcelaEnReservaEdit extends AppCompatActivity {

    private ParcelaEnReservaViewModel parcelaEnReservaViewModel;
    private ParcelaViewModel parcelaViewModel;
    private LinearLayout listaParcelasContainer;
    private Integer reservaId; // ID de la reserva actual
    private String fechaEntrada; // Fecha de entrada de la reserva
    private String fechaSalida; // Fecha de salida de la reserva
    private List<Parcela> parcelasAInsertar = new ArrayList<>();
    private List<Parcela> parcelasAEliminar = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcela_add_reserva);

        listaParcelasContainer = findViewById(R.id.listaParcelasContainer);
        parcelaEnReservaViewModel = new ViewModelProvider(this).get(ParcelaEnReservaViewModel.class);
        parcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);

        // Obtén los datos de la reserva desde el Intent o ViewModel
        reservaId = getIntent().getIntExtra("reservaId", -1);
        fechaEntrada = getIntent().getStringExtra("fecha_entrada");
        fechaSalida = getIntent().getStringExtra("fecha_salida");

        // Obtén todas las parcelas y aplica el filtro
        parcelaViewModel.getAllParcelas().observe(this, parcelas -> {
            listaParcelasContainer.removeAllViews(); // Limpia el contenedor antes de agregar nuevas vistas
            for (Parcela parcela : parcelas) {
                if (parcelaEnReservaViewModel.isParcelaInReserva(parcela.getNombre(), reservaId)) {
                    addParcelaView(parcela, true, false); // Ya está en mi reserva
                } else if (!parcelaEnReservaViewModel.isParcelaYSolapa(parcela.getNombre(), fechaEntrada, fechaSalida)) {
                    addParcelaView(parcela, false, true); // Disponible
                }
                // Las parcelas no disponibles ni en mi reserva se omiten automáticamente
            }
        });

        Button finalizarButton = findViewById(R.id.button_save_add_parcela);
        finalizarButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("parcelasAInsertar", new ArrayList<>(parcelasAInsertar));
            resultIntent.putExtra("parcelasAEliminar", new ArrayList<>(parcelasAEliminar));
            setResult(RESULT_OK, resultIntent);
            finish();
        });

    }

    @SuppressLint({"InflateParams", "DefaultLocale"})
    private void addParcelaView(Parcela parcela, boolean showButtonRojo, boolean showButtonVerde) {
        // Infla el layout del ítem
        View itemView = getLayoutInflater().inflate(R.layout.item_lista_parcelas_reserva, null);

        // Configura los datos del ítem
        TextView nombreParcela = itemView.findViewById(R.id.nombreParcela);
        TextView capacidadParcela = itemView.findViewById(R.id.capacidadParcela);
        TextView precioParcela = itemView.findViewById(R.id.precioParcela);
        Button buttonVerde = itemView.findViewById(R.id.button_verde);
        Button buttonRojo = itemView.findViewById(R.id.button_rojo);

        nombreParcela.setText(parcela.getNombre());
        capacidadParcela.setText(String.valueOf(parcela.getMaxOcupantes()));
        precioParcela.setText(String.format("%.2f€", parcela.getPrecioParcela()));

        // Configura la visibilidad de los botones
        buttonVerde.setVisibility(showButtonVerde ? View.VISIBLE : View.GONE);
        buttonRojo.setVisibility(showButtonRojo ? View.VISIBLE : View.GONE);

        // Configura las acciones de los botones
        buttonVerde.setOnClickListener(v -> addParcela(parcela));
        buttonRojo.setOnClickListener(v -> removeParcela(parcela));

        // Agrega la vista al contenedor
        listaParcelasContainer.addView(itemView);
    }


    private void addParcela(Parcela parcela) {
        parcelasAInsertar.add(parcela);

        parcelasAEliminar.remove(parcela);

        // Actualiza la lista tras la operación
        actualizarListaParcelas();
    }

    private void removeParcela(Parcela parcela) {
        parcelasAEliminar.add(parcela);

        parcelasAInsertar.remove(parcela);

        // Actualiza la lista tras la operación
        actualizarListaParcelas();
    }

    private void actualizarListaParcelas() {
        parcelaViewModel.getAllParcelas().observe(this, parcelas -> {
            listaParcelasContainer.removeAllViews(); // Limpia el contenedor antes de agregar nuevas vistas
            for (Parcela parcela : parcelas) {
                if (parcelasAInsertar.contains(parcela)) {
                    addParcelaView(parcela, true, false); // Ya está en mi reserva
                } else if(parcelasAEliminar.contains(parcela)){
                    addParcelaView(parcela, false, true); // Disponible
                } else if(parcelaEnReservaViewModel.isParcelaInReserva(parcela.getNombre(), reservaId)){
                    addParcelaView(parcela, true, false); // Ya está en mi reserva
                } else if (!parcelaEnReservaViewModel.isParcelaYSolapa(parcela.getNombre(), fechaEntrada, fechaSalida)) {
                    addParcelaView(parcela, false, true); // Disponible
                }
            }
        });
    }


}
