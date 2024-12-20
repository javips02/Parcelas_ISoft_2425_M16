package es.unizar.eina.parcelapad.database.parcelas;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/** Definición de un Data Access Object para las parcelas */
@Dao
public interface ParcelaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Parcela parcela);

    @Update
    int update(Parcela parcela);

    @Query("UPDATE parcela SET nombre = :nombre, desc = :desc, maxOcupantes = :maxOcupantes, precioParcela = :precio WHERE nombre = :originalNombre")
    int updateWithOriginalName(String originalNombre, String nombre, String desc, int maxOcupantes, double precio);


    @Delete
    int delete(Parcela parcela);

    @Query("DELETE FROM parcela")
    void deleteAll();

    @Query("SELECT * FROM parcela")
    LiveData<List<Parcela>> getParcelas();

    @Query("SELECT * FROM parcela ORDER BY nombre ASC")
    LiveData<List<Parcela>> getOrderedParcelasByName();

    @Query("SELECT * FROM parcela ORDER BY maxOcupantes ASC")
    LiveData<List<Parcela>> getOrderedParcelasByOcupantes();

    @Query("SELECT * FROM parcela ORDER BY precioParcela ASC")
    LiveData<List<Parcela>> getAllParcelasByPrecio();

    @Query("SELECT * FROM parcela WHERE nombre = :name LIMIT 1")
    Parcela findByName(String name);

}
