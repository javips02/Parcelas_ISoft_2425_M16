package es.unizar.eina.parcelapad;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.parcelapad.database.parcelas.Parcela;
import es.unizar.eina.parcelapad.database.parcelas.ParcelaDao;
import es.unizar.eina.parcelapad.database.parcelas.ParcelaRepository;

public class ParcelasInstrumentedTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ParcelaRepository repository;
    private ParcelaDao mockParcelaDao;

    @Before
    public void setUp() {
        Application mockApplication = mock(Application.class);
        mockParcelaDao = mock(ParcelaDao.class);

        repository = new ParcelaRepository(mockApplication) {
            @Override
            protected ParcelaDao getDao() {
                return mockParcelaDao;
            }
        };
    }

    @Test
    public void testInsertParcelaValid() throws Exception {
        Parcela validParcela = new Parcela("Parcela1", "Descripción", 4, 100.0);

        when(mockParcelaDao.insert(validParcela)).thenReturn(1L);

        long result = repository.insert(validParcela);

        assertEquals(1L, result);
        verify(mockParcelaDao).insert(validParcela);
    }

    @Test
    public void testInsertParcelaInvalidName() throws Exception {
        Parcela invalidParcela = new Parcela("", "Descripción", 4, 100.0);

        long result = repository.insert(invalidParcela);

        assertEquals(-1, result);
        verify(mockParcelaDao, never()).insert(any());
    }

    @Test
    public void testUpdateParcelaValid() throws Exception {
        Parcela parcela = new Parcela("Parcela1", "Descripción actualizada", 4, 120.0);

        when(mockParcelaDao.update(parcela)).thenReturn(1);

        int result = repository.update(parcela);

        assertEquals(1, result);
        verify(mockParcelaDao).update(parcela);
    }

    @Test
    public void testUpdateParcelaNotFound() throws Exception {
        Parcela parcela = new Parcela("ParcelaInexistente", "Descripción", 4, 120.0);

        when(mockParcelaDao.update(parcela)).thenReturn(0);

        int result = repository.update(parcela);

        assertEquals(0, result);
        verify(mockParcelaDao).update(parcela);
    }

    @Test
    public void testDeleteParcelaValid() throws Exception {
        Parcela parcela = new Parcela("Parcela1", "Descripción", 4, 100.0);

        when(mockParcelaDao.delete(parcela)).thenReturn(1);

        int result = repository.delete(parcela);

        assertEquals(1, result);
        verify(mockParcelaDao).delete(parcela);
    }

    @Test
    public void testDeleteParcelaNotFound() throws Exception {
        Parcela parcela = new Parcela("ParcelaInexistente", "Descripción", 4, 100.0);

        when(mockParcelaDao.delete(parcela)).thenReturn(0);

        int result = repository.delete(parcela);

        assertEquals(0, result);
        verify(mockParcelaDao).delete(parcela);
    }

    @Test
    public void testGetAllParcelas() {
        List<Parcela> parcelasList = new ArrayList<>();
        parcelasList.add(new Parcela("Parcela1", "Descripción", 4, 100.0));
        parcelasList.add(new Parcela("Parcela2", "Descripción 2", 2, 50.0));

        LiveData<List<Parcela>> liveData = mock(LiveData.class);
        when(mockParcelaDao.getParcelas()).thenReturn(liveData);

        LiveData<List<Parcela>> result = repository.getAllParcelas();

        assertEquals(liveData, result);
        verify(mockParcelaDao).getParcelas();
    }
}
