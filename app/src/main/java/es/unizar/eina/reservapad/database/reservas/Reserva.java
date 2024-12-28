package es.unizar.eina.reservapad.database.reservas;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

/** Clase anotada como entidad que representa una reserva, y consta de un nombre del cliente, un
 * telefono del cliente, una fecha de entrada y una fecha de salida.
 **/
@Entity(tableName = "reserva")
public class Reserva {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private Integer ID;

    @NonNull
    @ColumnInfo(name = "nombreCliente")
    private String nombreCliente;

    @NonNull
    @ColumnInfo(name = "tlfCliente")
    private Integer tlfCliente;

    @NonNull
    @ColumnInfo(name = "fechaEntrada")
    private String fechaEntrada;

    @NonNull
    @ColumnInfo(name = "fechaSalida")
    private String fechaSalida;

    public Reserva(@NonNull String nombreCliente, @NonNull Integer tlfCliente,
                   @NonNull String fechaEntrada, @NonNull String fechaSalida) {
        this.nombreCliente = nombreCliente;
        this.tlfCliente = tlfCliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    /** Devuelve el ID de la reserva */
    public @NonNull Integer getID() {
        return this.ID;
    }

    /** Permite actualizar el ID de la reserva */
    public void setID(@NonNull Integer ID) {
        this.ID = ID;
    }

    /** Devuelve el nombre del Cliente de la reserva */
    public @NonNull String getNombreCliente() {
        return this.nombreCliente;
    }

    /** Permite actualizar el nombre del Cliente de la reserva */
    public void setNombreCliente(@NonNull String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /** Devuelve el telefono del Cliente de la reserva */
    public @NonNull Integer getTlfCliente() {
        return this.tlfCliente;
    }

    /** Permite actualizar el telefono del Cliente de la reserva */
    public void setTlfCliente(@NonNull Integer tlfCliente) {
        this.tlfCliente = tlfCliente;
    }

    /** Devuelve la fecha de entrada de la reserva */
    public @NonNull String getFechaEntrada() {
        return this.fechaEntrada;
    }

    /** Permite actualizar la fecha de entrada de la reserva */
    public void setFechaEntrada(@NonNull String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    /** Devuelve la fecha de salida de la reserva */
    public @NonNull String getFechaSalida() {
        return this.fechaSalida;
    }

    /** Permite actualizar la fecha de salida de la reserva */
    public void setFechaSalida(@NonNull String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
}
