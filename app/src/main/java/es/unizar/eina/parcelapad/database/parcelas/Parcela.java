package es.unizar.eina.parcelapad.database.parcelas;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/** Clase anotada como entidad que representa una parcela, que consta de un nombre, una
 * descripcion, un numero maximo de ocupantes, y el precio que cuesta reservar esa parcela.
 **/
@Entity(tableName = "parcela")
public class Parcela implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "desc")
    private String desc;

    @NonNull
    @ColumnInfo(name = "maxOcupantes")
    private int maxOcupantes;

    @NonNull
    @ColumnInfo(name = "precioParcela")
    private double precioParcela;

    public Parcela(@NonNull String nombre, String desc, @NonNull int maxOcupantes, @NonNull double precioParcela) {
        this.nombre = nombre;
        this.desc = desc;
        this.maxOcupantes = maxOcupantes;
        this.precioParcela = precioParcela;
    }

    /** Devuelve el nombre de la parcela */
    public @NonNull String getNombre() {
        return this.nombre;
    }

    /** Permite actualizar el nombre de la parcela */
    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    /** Devuelve la descripción de la parcela */
    public String getDesc() {
        return this.desc;
    }

    /** Permite actualizar la descripción de la parcela */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /** Devuelve el número máximo de ocupantes */
    public int getMaxOcupantes() {
        return this.maxOcupantes;
    }

    /** Permite actualizar el número máximo de ocupantes */
    public void setMaxOcupantes(int maxOcupantes) {
        this.maxOcupantes = maxOcupantes;
    }

    /** Devuelve el precio de la parcela */
    public double getPrecioParcela() {
        return this.precioParcela;
    }

    /** Permite actualizar el precio de la parcela */
    public void setPrecioParcela(double precioParcela) {
        this.precioParcela = precioParcela;
    }
}
