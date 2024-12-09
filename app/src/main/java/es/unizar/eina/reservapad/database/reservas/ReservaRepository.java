package es.unizar.eina.reservapad.database.reservas;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Clase que gestiona el acceso la fuente de datos.
 * Interacciona con la base de datos a través de las clases ReservaRoomDatabase y ReservaDao.
 */
public class ReservaRepository {

        private final ReservaDao mReservaDao;
        private final LiveData<List<Reserva>> mAllReservas;

        private final long TIMEOUT = 15000;

        /**
        * Constructor de ReservaRepository utilizando el contexto de la aplicación para instanciar la base de datos.
        **/

        public ReservaRepository(Application application) {
            ReservaRoomDatabase db = ReservaRoomDatabase.getDatabase(application);
            mReservaDao = db.reservaDao();
            mAllReservas = mReservaDao.getAllReservas();
        }

    /** Devuelve un objeto de tipo LiveData con todas las reservas.
     * Room ejecuta todas las consultas en un hilo separado.
     * El objeto LiveData notifica a los observadores cuando los datos cambian.
     */
    public LiveData<List<Reserva>> getAllReservas() {
        return mAllReservas;
    }

    /** Inserta una reserva nueva en la base de datos
     * @param reserva La reserva consta de: un nombre del cliente (reserva.getNombreCliente()) no nulo (reserva.getNombreCliente()!=null) y no vacío
     *             (reserva.getNombreCliente().length()>0); un telefono del cliente (reserva.getTlfCliente()) no nulo (reserva.getTlfCliente()!= null) y no vacío
     *             (reserva.getTlfCliente().lenght>=0); una fecha de entrada (reserva.getFechaEntrada()) no nulo
     *             (reserva.getFechaEntrada()!=null) y no vacío (reserva.getFechaEntrada().lenght>0); y una fecha de salida (reserva.getFechaSalida()) no nulo
     *             (reserva.getFechaSalida()!=null) y no vacío (reserva.getFechaSalida().lenght>0).
     * @return Si la reserva se ha insertado correctamente, devuelve el nombre del cliente de la reserva que se ha creado. En caso
     *         contrario, devuelve -1 para indicar el fallo.
     */
    public long insert(Reserva reserva) {
        /* Para que la App funcione correctamente y no lance una excepción, la modificación de la
         * base de datos se debe lanzar en un hilo de ejecución separado
         * (databaseWriteExecutor.submit). Para poder sincronizar la recuperación del resultado
         * devuelto por la base de datos, se puede utilizar un Future.
         */
        Future<Long> future = ReservaRoomDatabase.databaseWriteExecutor.submit(() -> {
            return mReservaDao.insert(reserva);
        });

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ReservaRepository", "Error al insertar la reserva", ex);
            return -1;
        }
    }

    /** Actualiza una reserva en la base de datos
     * @param reserva La reserva a actualizar
     * @return Si la reserva se ha actualizado correctamente, devuelve el número de reservas a
     *          ctualizadas. En caso contrario, devuelve -1 para indicar el fallo.
     */
    public int update(Reserva reserva) {
        Future<Integer> future = ReservaRoomDatabase.databaseWriteExecutor.submit(() ->
                mReservaDao.update(reserva)
        );

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ReservaRepository", "Error al actualizar la reserva", ex);
            return -1;
        }
    }

    /** Elimina una reserva de la base de datos
     * @param reserva La reserva a eliminar
     * @return Si la reserva se ha eliminado correctamente, devuelve el número de reservas eliminadas.
     *          En caso contrario, devuelve -1 para indicar el fallo.
     */
    public int delete(Reserva reserva) {
        Future<Integer> future = ReservaRoomDatabase.databaseWriteExecutor.submit(() ->
                mReservaDao.delete(reserva)
        );

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ReservaRepository", "Error al eliminar la reserva", ex);
            return -1;
        }
    }

    /** Devuelve un objeto de tipo LiveData con todas las reservas ordenadas alfabéticamente por
     * nombre del cliente.
     * Room ejecuta todas las consultas en un hilo separado.
     * El objeto LiveData notifica a los observadores cuando los datos cambian.
     */
    public LiveData<List<Reserva>> getAllReservasByNombreCliente() {
        return mReservaDao.getOrderedReservasByNombreCLiente();
    }

    /** Devuelve un objeto de tipo LiveData con todas las reservas ordenadas alfabéticamente por
     * telfono del cliente.
     * Room ejecuta todas las consultas en un hilo separado.
     * El objeto LiveData notifica a los observadores cuando los datos cambian.
     */
    public LiveData<List<Reserva>> getAllReservasByTlfCliente() {
        return mReservaDao.getOrderedReservasByTlfCLiente();
    }

    /** Devuelve un objeto de tipo LiveData con todas las reservas ordenadas cronologicamente por
     * su fecha de entrada.
     * Room ejecuta todas las consultas en un hilo separado.
     * El objeto LiveData notifica a los observadores cuando los datos cambian.
     */
    public LiveData<List<Reserva>> getAllReservasByFEntrada() {
        return mReservaDao.getOrderedReservasByFEntrada();
    }
}
