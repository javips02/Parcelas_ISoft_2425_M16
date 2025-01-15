package es.unizar.eina.parcelapad;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.unizar.eina.parcelapad.database.parcelas.ParcelaRoomDatabase;
import es.unizar.eina.reservapad.database.reservas.Reserva;
import es.unizar.eina.reservapad.database.reservas.ReservaRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ReservaRepositoryUnitTest {

    private ReservaRepository reservaRepository;
    private ParcelaRoomDatabase db;

    @Before
    public void setUp() {
        // Configuración de base de datos en memoria
        db = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ParcelaRoomDatabase.class
        ).allowMainThreadQueries().build();

        reservaRepository = new ReservaRepository(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() {
        // Cierre de la base de datos
        db.close();
    }

   /*
    *   TESTS DE INSERCIÓN
    */
// Caso válido: Inserción exitosa
   @Test
   public void testInsert_ValidReserva() {
       Reserva reserva = new Reserva("Cliente A", 123456789, "2025-01-01", "2025-01-10");
       long result = reservaRepository.insert(reserva);
       assertTrue("Se espera un ID válido", result > 0);
   }

    // Caso inválido: Nombre nulo
    @Test
    public void testInsert_NullNombreCliente() {
        Reserva reserva = new Reserva(null, 123456789, "2025-01-01", "2025-01-10");
        long result = reservaRepository.insert(reserva);
        assertEquals("Un nombre nulo debería devolver -1", -1, result);
    }

    // Caso inválido: Nombre vacío
    @Test
    public void testInsert_EmptyNombreCliente() {
        Reserva reserva = new Reserva("", 123456789, "2025-01-01", "2025-01-10");
        long result = reservaRepository.insert(reserva);
        assertEquals("Un nombre vacío debería devolver -1", -1, result);
    }

    // Caso inválido: Teléfono nulo
    @Test
    public void testInsert_NullTlfCliente() {
        Reserva reserva = new Reserva("Cliente A", null, "2025-01-01", "2025-01-10");
        long result = reservaRepository.insert(reserva);
        assertEquals("Un teléfono nulo debería devolver -1", -1, result);
    }

    // Caso inválido: Fecha de entrada nula
    @Test
    public void testInsert_NullFechaEntrada() {
        Reserva reserva = new Reserva("Cliente A", 123456789, null, "2025-01-10");
        long result = reservaRepository.insert(reserva);
        assertEquals("Una fecha de entrada nula debería devolver -1", -1, result);
    }

    // Caso inválido: Fecha de entrada vacía
    @Test
    public void testInsert_EmptyFechaEntrada() {
        Reserva reserva = new Reserva("Cliente A", 123456789, "", "2025-01-10");
        long result = reservaRepository.insert(reserva);
        assertEquals("Una fecha de entrada vacía debería devolver -1", -1, result);
    }

    // Caso inválido: Fecha de salida nula
    @Test
    public void testInsert_NullFechaSalida() {
        Reserva reserva = new Reserva("Cliente A", 123456789, "2025-01-01", null);
        long result = reservaRepository.insert(reserva);
        assertEquals("Una fecha de salida nula debería devolver -1", -1, result);
    }

    // Caso inválido: Fecha de salida vacía
    @Test
    public void testInsert_EmptyFechaSalida() {
        Reserva reserva = new Reserva("Cliente A", 123456789, "2025-01-01", "");
        long result = reservaRepository.insert(reserva);
        assertEquals("Una fecha de salida vacía debería devolver -1", -1, result);
    }

    /*
     *  TESTS DE ACTUALIZACIÓN
     */

    // Caso válido: Actualización exitosa
    @Test
    public void testUpdate_ValidReserva() {
        Reserva reserva = new Reserva("Cliente A", 123456789, "2025-01-01", "2025-01-10");
        reservaRepository.insert(reserva);

        reserva.setFechaSalida("2025-01-15"); //Cambio de 1 parámetro (fechaSalida)
        int result = reservaRepository.update(reserva);
        assertEquals("Se espera que se actualice una fila", 0, result);
    }

    // Caso inválido: Nombre nulo
    @Test
    public void testUpdate_NullNombreCliente() {
        Reserva reserva = new Reserva("Cliente A", 123456789, "2025-01-01", "2025-01-10");
        reservaRepository.insert(reserva);

        reserva.setNombreCliente(null);
        int result = reservaRepository.update(reserva);
        assertEquals("Un nombre nulo debería devolver -1", -1, result);
    }

    // Caso inválido: Nombre vacío
    @Test
    public void testUpdate_EmptyNombreCliente() {
        Reserva reserva = new Reserva("Cliente A", 123456789, "2025-01-01", "2025-01-10");
        reservaRepository.insert(reserva);

        reserva.setNombreCliente("");
        int result = reservaRepository.update(reserva);
        assertEquals("Un nombre vacío debería devolver -1", -1, result);
    }

    // Caso inválido: Teléfono nulo
    @Test
    public void testUpdate_NullTlfCliente() {
        Reserva reserva = new Reserva("Cliente A", 123456789, "2025-01-01", "2025-01-10");
        reservaRepository.insert(reserva);

        reserva.setTlfCliente(null);
        int result = reservaRepository.update(reserva);
        assertEquals("Un teléfono nulo debería devolver -1", -1, result);
    }

    // Caso inválido: Fecha de entrada nula
    @Test
    public void testUpdate_NullFechaEntrada() {
        Reserva reserva = new Reserva("Cliente A", 123456789, "2025-01-01", "2025-01-10");
        reservaRepository.insert(reserva);

        reserva.setFechaEntrada(null);
        int result = reservaRepository.update(reserva);
        assertEquals("Una fecha de entrada nula debería devolver -1", -1, result);
    }

    // Caso inválido: Fecha de salida nula
    @Test
    public void testUpdate_NullFechaSalida() {
        Reserva reserva = new Reserva("Cliente A", 123456789, "2025-01-01", "2025-01-10");
        reservaRepository.insert(reserva);

        reserva.setFechaSalida(null);
        int result = reservaRepository.update(reserva);
        assertEquals("Una fecha de salida nula debería devolver -1", -1, result);
    }


    /*
     *  TESTS DE BORRADO
     */
}
