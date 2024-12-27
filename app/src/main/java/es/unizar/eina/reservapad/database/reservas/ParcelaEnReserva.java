package es.unizar.eina.reservapad.database.reservas;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

/**
 * Clase anotada como entidad que representa una relación entre una parcela y una reserva.
 * Incluye los ocupantes de la reserva en esa parcela.
 */
@Entity(
        tableName = "parcela_en_reserva",
        primaryKeys = {"parcelaNombre", "reservaID"},
        foreignKeys = {
                @ForeignKey(
                        entity = es.unizar.eina.parcelapad.database.parcelas.Parcela.class,
                        parentColumns = "nombre",
                        childColumns = "parcelaNombre",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = es.unizar.eina.reservapad.database.reservas.Reserva.class,
                        parentColumns = "ID",
                        childColumns = "reservaID",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("parcelaNombre"),
                @Index("reservaID")
        }
)
public class ParcelaEnReserva {

    @NonNull
    @ColumnInfo(name = "parcelaNombre")
    private String parcelaNombre;

    @NonNull
    @ColumnInfo(name = "reservaID")
    private Integer reservaID;

    @NonNull
    @ColumnInfo(name = "ocupantes")
    private int ocupantes;

    @NonNull
    @ColumnInfo(name = "precio")
    private double precio;

    public ParcelaEnReserva(@NonNull String parcelaNombre, @NonNull Integer reservaID, @NonNull int ocupantes, @NonNull double precio) {
        this.parcelaNombre = parcelaNombre;
        this.reservaID = reservaID;
        this.ocupantes = ocupantes;
        this.precio = precio;
    }

    /** Devuelve el nombre de la parcela asociada */
    public @NonNull String getParcelaNombre() {
        return parcelaNombre;
    }

    /** Permite actualizar el nombre de la parcela asociada */
    public void setParcelaNombre(@NonNull String parcelaNombre) {
        this.parcelaNombre = parcelaNombre;
    }

    /** Devuelve el ID de la reserva asociada */
    public @NonNull Integer getReservaID() {
        return reservaID;
    }

    /** Permite actualizar el ID de la reserva asociada */
    public void setReservaID(@NonNull Integer reservaID) {
        this.reservaID = reservaID;
    }

    /** Devuelve el número de ocupantes */
    public int getOcupantes() {
        return ocupantes;
    }

    /** Permite actualizar el número de ocupantes */
    public void setOcupantes(int ocupantes) {
        this.ocupantes = ocupantes;
    }

    /** Devuelve el precio de la parcela */
    public double getPrecio() {
        return precio;
    }

    /** Permite actualizar el precio de la parcela */
    public void setPrecio(double precio) {
        this.precio = precio;
    }


}

