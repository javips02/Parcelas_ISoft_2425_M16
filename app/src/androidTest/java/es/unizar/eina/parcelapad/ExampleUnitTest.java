package es.unizar.eina.parcelapad;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.unizar.eina.parcelapad.database.parcelas.Parcela;
import es.unizar.eina.parcelapad.database.parcelas.ParcelaRepository;
import es.unizar.eina.parcelapad.database.parcelas.ParcelaRoomDatabase;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ExampleUnitTest {

    private ParcelaRepository parcelaRepository;
    private ParcelaRoomDatabase db;

    @Before
    public void setUp() {
        db = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ParcelaRoomDatabase.class
        ).allowMainThreadQueries().build();

        parcelaRepository = new ParcelaRepository(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void testInsert_ValidParcela() {
        Parcela parcela = new Parcela("Parcela A", "Descripción", 5, 100.00);
        long result = parcelaRepository.insert(parcela);
        assertTrue(result > 0);
    }
}
