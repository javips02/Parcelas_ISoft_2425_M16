package es.unizar.eina.reservapad.ui.reservas;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.reservapad.database.reservas.ParcelaEnReserva;
import es.unizar.eina.reservapad.database.reservas.ParcelaEnReservaRepository;

public class ParcelaEnReservaViewModel extends AndroidViewModel {

    private ParcelaEnReservaRepository mRepository;

    private final LiveData<List<ParcelaEnReserva>> mAllParcelasEnReserva;

    public ParcelaEnReservaViewModel(Application application) {
        super(application);
        mRepository = new ParcelaEnReservaRepository(application);
        mAllParcelasEnReserva = mRepository.getAllParcelasEnReserva();
    }

    LiveData<List<ParcelaEnReserva>> getAllParcelasEnReserva() { return mAllParcelasEnReserva; }

    public void insert(ParcelaEnReserva parcelaEnReserva) { mRepository.insert(parcelaEnReserva); }

    public void update(ParcelaEnReserva parcelaEnReserva) { mRepository.update(parcelaEnReserva); }

    public void delete(ParcelaEnReserva parcelaEnReserva) { mRepository.delete(parcelaEnReserva); }

    /**
     * Verifica si una parcela está en una reserva específica.
     * @param parcelaNombre El nombre de la parcela.
     * @param reservaID El ID de la reserva.
     * @return true si la parcela está en la reserva, false en caso contrario.
     */
    public boolean isParcelaInReserva(String parcelaNombre, int reservaID) {
        return mRepository.isParcelaInReserva(parcelaNombre, reservaID);
    }

    /**
     * Comprueba si una parcela está en alguna reserva que se solape con las fechas dadas.
     *
     * @param nombreParcela El nombre de la parcela a comprobar.
     * @param fechaEntrada La fecha de entrada de tu reserva.
     * @param fechaSalida La fecha de salida de tu reserva.
     * @return true si la parcela está en una reserva que se solape con tus fechas; false en caso contrario.
     */
    public boolean isParcelaYSolapa(String nombreParcela, String fechaEntrada, String fechaSalida) {
        return mRepository.isParcelaYSolapa(nombreParcela, fechaEntrada, fechaSalida);
    }

}
