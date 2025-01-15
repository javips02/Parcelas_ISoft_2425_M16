package es.unizar.eina.parcelapad;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.unizar.eina.parcelapad.database.parcelas.Parcela;
import es.unizar.eina.parcelapad.database.parcelas.ParcelaRepository;
import es.unizar.eina.parcelapad.database.parcelas.ParcelaRoomDatabase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ParcelaRepositoryUnitTest {

    private ParcelaRepository parcelaRepository;
    private ParcelaRoomDatabase db;

    @Before
    public void setUp() {
        // Configuración de base de datos en memoria
        db = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ParcelaRoomDatabase.class
        ).allowMainThreadQueries().build();

        parcelaRepository = new ParcelaRepository(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() {
        // Cierre de la base de datos
        db.close();
    }

    // Caso válido: Parcela con datos correctos
    @Test
    public void testInsert_ValidParcela() {
        Parcela parcela = new Parcela("ParcelaB", "Descripción", 5, 100.00);
        long result = parcelaRepository.insert(parcela);
        assertTrue("Se espera un ID válido", result > 0);
    }

    // Caso inválido: Nombre vacío (Casos 2 y 3)
    @Test
    public void testInsert_EmptyNombre() {
        Parcela parcela = new Parcela("", "Descripción", 5, 100.00);
        long result = parcelaRepository.insert(parcela);
        assertEquals("El nombre vacío debería devolver -1", -1, result);
    }

    // Caso inválido: MaxOcupantes menor que 0 (Caso 4)
    @Test
    public void testInsert_NegativeMaxOcupantes() {
        Parcela parcela = new Parcela("Parcela A", "Descripción", -1, 100.00);
        long result = parcelaRepository.insert(parcela);
        assertEquals("MaxOcupantes negativo debería devolver -1", -1, result);
    }

    // Caso inválido: Precio igual a 0.0
    @Test
    public void testInsert_ZeroPrecio() {
        Parcela parcela = new Parcela("Parcela A", "Descripción", 5, 0.00);
        long result = parcelaRepository.insert(parcela);
        assertEquals("El precio igual a 0 debería devolver -1", -1, result);
    }

    // Caso inválido: Precio negativo
    @Test
    public void testInsert_NegativePrecio() {
        Parcela parcela = new Parcela("Parcela A", "Descripción", 5, -10.00);
        long result = parcelaRepository.insert(parcela);
        assertEquals("El precio negativo debería devolver -1", -1, result);
    }

    /* **************************
     * TESTS DEL METHOD UPDATE *
     * **************************
     */
    // Caso válido: Actualización exitosa
    @Test
    public void testUpdate_ValidParcela() {
        Parcela parcela = new Parcela("Parcela A", "Descripción", 5, 100.00);
        parcelaRepository.insert(parcela);

        Parcela updatedParcela = new Parcela("Parcela A", "Nueva descripción", 10, 150.00);
        int result = parcelaRepository.update(updatedParcela);
        assertEquals("Se espera una actualización exitosa", 1, result);
    }
    // Caso inválido: Nombre nulo
    @Test
    public void testUpdate_NullNombre() {
        Parcela parcela = new Parcela("Parcela A", "Descripción", 5, 100.00);
        parcelaRepository.insert(parcela);

        Parcela updatedParcela = new Parcela(null, "Nueva descripción", 10, 150.00);
        int result = parcelaRepository.update(updatedParcela);
        assertEquals("Un nombre nulo debería devolver -1", -1, result);
    }

    // Caso inválido: Nombre vacío
    @Test
    public void testUpdate_EmptyNombre() {
        Parcela parcela = new Parcela("Parcela A", "Descripción", 5, 100.00);
        parcelaRepository.insert(parcela);

        Parcela updatedParcela = new Parcela("", "Nueva descripción", 10, 150.00);
        int result = parcelaRepository.update(updatedParcela);
        assertEquals("Un nombre vacío debería devolver -1", -1, result);
    }

    // Caso inválido: MaxOcupantes menor que 0
    @Test
    public void testUpdate_NegativeMaxOcupantes() {
        Parcela parcela = new Parcela("Parcela A", "Descripción", 5, 100.00);
        parcelaRepository.insert(parcela);

        Parcela updatedParcela = new Parcela("Parcela A", "Nueva descripción", -1, 150.00);
        int result = parcelaRepository.update(updatedParcela);
        assertEquals("MaxOcupantes negativo debería devolver -1", -1, result);
    }

    // Caso inválido: Precio igual a 0
    @Test
    public void testUpdate_ZeroPrecio() {
        Parcela parcela = new Parcela("Parcela A", "Descripción", 5, 100.00);
        parcelaRepository.insert(parcela);

        Parcela updatedParcela = new Parcela("Parcela A", "Nueva descripción", 10, 0.00);
        int result = parcelaRepository.update(updatedParcela);
        assertEquals("Precio igual a 0 debería devolver -1", -1, result);
    }

    // Caso inválido: Precio negativo
    @Test
    public void testUpdate_NegativePrecio() {
        Parcela parcela = new Parcela("Parcela A", "Descripción", 5, 100.00);
        parcelaRepository.insert(parcela);

        Parcela updatedParcela = new Parcela("Parcela A", "Nueva descripción", 10, -10.00);
        int result = parcelaRepository.update(updatedParcela);
        assertEquals("Precio negativo debería devolver -1", -1, result);
    }
}
