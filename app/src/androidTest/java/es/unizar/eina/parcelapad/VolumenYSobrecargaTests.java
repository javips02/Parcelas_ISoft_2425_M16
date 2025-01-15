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
import es.unizar.eina.reservapad.database.reservas.Reserva;
import es.unizar.eina.reservapad.database.reservas.ReservaRepository;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class VolumenYSobrecargaTests {

    private ParcelaRepository parcelaRepository;
    private ReservaRepository reservaRepository;
    private ParcelaRoomDatabase db;

    @Before
    public void setUp() {
        db = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ParcelaRoomDatabase.class
        ).allowMainThreadQueries().build();

        parcelaRepository = new ParcelaRepository(ApplicationProvider.getApplicationContext());
        reservaRepository = new ReservaRepository(ApplicationProvider.getApplicationContext());
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

    /* Test de prueba de volumen que inserta 100 parcelas */
    @Test
    public void testInsertParcelas_Volume() {
        for (int i = 0; i < 100; i++) {
            Parcela parcela = new Parcela("Parcela" + i, "Descripción", 5, 100.00);
            long result = parcelaRepository.insert(parcela);
            assertTrue(result > 0);
        }
    }

    /* Test de prueba de volumen que inserta 10000 reservas */
    @Test
    public void testInsertReservas_Volume() {
        for (int i = 0; i < 10000; i++) {
            Reserva reserva = new Reserva("Reserva" + i, 777777777, "2025-02-01", "2025-02-02");
            long result = reservaRepository.insert(reserva);
            assertTrue(result > 0);
        }
    }

    /* Test de prueba de sobrecarga que prueba a insertar parcelas con una descripcion que aumenta en 5 caracteres cada vez,
    hasta que falle la insercion por demasiado tamaño de descripcion. Se registra a traves de util.log el tamaño de la descripcion
    que se intenta insertar, para ver el registro en LogCat y ver cuando falla. */
    @Test
    public void testInsertParcelas_Overload() {
        for (int i = 0; i < 100000; i++) {

            Parcela parcela = new Parcela("Parcela" + i, "a".repeat(i*1000), 5, 100.00);
            long result = parcelaRepository.insert(parcela);

            if (result == -1) {
                android.util.Log.d("testInsertParcelas_Overload", "Failed to insert parcela with description of size " + i*1000);
                break;
            } else {
                android.util.Log.d("testInsertParcelas_Overload", "Inserted parcela with description of size " + i*1000);
            }
        }
    }
}
