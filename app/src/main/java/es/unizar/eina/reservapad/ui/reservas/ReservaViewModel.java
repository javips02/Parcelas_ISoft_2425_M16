package es.unizar.eina.reservapad.ui.reservas;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.parcelapad.database.parcelas.Parcela;
import es.unizar.eina.parcelapad.database.parcelas.ParcelaRepository;
import es.unizar.eina.reservapad.database.reservas.Reserva;
import es.unizar.eina.reservapad.database.reservas.ReservaRepository;

public class ReservaViewModel extends AndroidViewModel {

    private ReservaRepository mRepository;

    private final LiveData<List<Reserva>> mAllReservas;

    public ReservaViewModel(Application application) {
        super(application);
        mRepository = new ReservaRepository(application);
        mAllReservas = mRepository.getAllReservas();
    }

    LiveData<List<Reserva>> getAllReservas() { return mAllReservas; }
    LiveData<List<Reserva>> getAllReservasByNombreCliente() {
        return mRepository.getAllReservasByNombreCliente();
    }

    LiveData<List<Reserva>> getAllReservasByTlfCliente() {
        return mRepository.getAllReservasByTlfCliente();
    }

    LiveData<List<Reserva>> getAllReservasByFEntrada() {
        return mRepository.getAllReservasByFEntrada();
    }

    public void insert(Reserva reserva) { mRepository.insert(reserva); }

    public long insertAndGetId(Reserva reserva) {
        return mRepository.insert(reserva);
    }


    public void update(Reserva reserva) { mRepository.update(reserva); }
    public void delete(Reserva reserva) { mRepository.delete(reserva); }

    public Reserva getReservaByID(int ID) {
        return mRepository.getReservaByID(ID);
    }
}
