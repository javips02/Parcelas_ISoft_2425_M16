package es.unizar.eina.reservapad.database.reservas;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.unizar.eina.parcelapad.database.parcelas.Parcela;

@Dao
public interface ParcelaEnReservaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(ParcelaEnReserva pr);

    @Update
    int update(ParcelaEnReserva pr);

    @Delete
    int delete(ParcelaEnReserva pr);

    @Query("DELETE FROM parcela_en_reserva")
    void deleteAll();

    @Query("SELECT * FROM parcela_en_reserva ORDER BY reservaID ASC")
    LiveData<List<ParcelaEnReserva>> getPR();

    @Query("SELECT * FROM parcela_en_reserva WHERE reservaID= :nombreReserva")
    ParcelaEnReserva findByReserva(String nombreReserva);

    @Query("SELECT * FROM parcela_en_reserva WHERE parcelaNombre= :nombreParcela")
    ParcelaEnReserva fundByParcela(String nombreParcela);


}
