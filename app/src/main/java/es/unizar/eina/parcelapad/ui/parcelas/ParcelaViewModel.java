package es.unizar.eina.parcelapad.ui.parcelas;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.parcelapad.database.parcelas.Parcela;
import es.unizar.eina.parcelapad.database.parcelas.ParcelaRepository;

public class ParcelaViewModel extends AndroidViewModel {

    private ParcelaRepository mRepository;

    private final LiveData<List<Parcela>> mAllParcelas;

    public ParcelaViewModel(Application application) {
        super(application);
        mRepository = new ParcelaRepository(application);
        mAllParcelas = mRepository.getAllParcelas();
    }

    public LiveData<List<Parcela>> getAllParcelas() { return mAllParcelas; }
    LiveData<List<Parcela>> getAllParcelasByName() {
        return mRepository.getAllParcelasByNombre();
    }

    LiveData<List<Parcela>> getAllParcelasByOcupantes() {
        return mRepository.getAllParcelasByOcupantes();
    }

    LiveData<List<Parcela>> getAllParcelasByPrecio() {
        return mRepository.getAllParcelasByPrecio();
    }



    public void insert(Parcela parcela) { mRepository.insert(parcela); }

    public void update(Parcela parcela) { mRepository.update(parcela); }
    public void delete(Parcela parcela) { mRepository.delete(parcela); }

    public void updateWithOriginalName(Parcela parcela, String originalNombre) {
        mRepository.updateWithOriginalName(parcela, originalNombre);
    }

}
