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
}
