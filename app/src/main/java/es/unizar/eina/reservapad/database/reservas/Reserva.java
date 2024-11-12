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
    @NonNull
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
    private LocalDate fechaEntrada;

    @NonNull
    @ColumnInfo(name = "fechaSalida")
    private LocalDate fechaSalida;

    public Reserva(@NonNull String nombreCliente, @NonNull Integer tlfCliente,
                   @NonNull LocalDate fechaEntrada, @NonNull LocalDate fechaSalida) {
        this.nombreCliente = nombreCliente;
        this.tlfCliente = tlfCliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
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
    public @NonNull LocalDate getFechaEntrada() {
        return this.fechaEntrada;
    }

    /** Permite actualizar la fecha de entrada de la reserva */
    public void setFechaEntrada(@NonNull LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    /** Devuelve la fecha de salida de la reserva */
    public @NonNull LocalDate getFechaSalida() {
        return this.fechaSalida;
    }

    /** Permite actualizar la fecha de salida de la reserva */
    public void setFechaSalida(@NonNull LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
}
