package es.unizar.eina.reservapad.database.reservas;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/** Definición de un Data Access Object para las reservas */
@Dao
public interface ReservaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Reserva reserva);

    @Update
    int update(Reserva reserva);

    @Delete
    int delete(Reserva reserva);

    @Query("SELECT * FROM reserva")
    LiveData<List<Reserva>> getAllReservas();

    @Query("DELETE FROM reserva")
    void deleteAll();

    @Query("SELECT * FROM reserva ORDER BY nombreCliente ASC")
    LiveData<List<Reserva>> getOrderedReservasByNombreCLiente();

    @Query("SELECT * FROM reserva ORDER BY tlfCliente ASC")
    LiveData<List<Reserva>> getOrderedReservasByTlfCLiente();

    @Query("SELECT * FROM reserva ORDER BY fechaEntrada ASC")
    LiveData<List<Reserva>> getOrderedReservasByFEntrada();
}
