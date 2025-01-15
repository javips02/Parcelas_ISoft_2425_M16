package es.unizar.eina.reservapad.database.reservas;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import es.unizar.eina.welcome.db.UnifiedRoomDatabase;

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
            UnifiedRoomDatabase db = UnifiedRoomDatabase.getDatabase(application);
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

    /** Inserta una reserva nueva en la base de datos.
     * Los datos vienen comprobados y depurados desde la lógica de la aplicación, esta función no
     * comprueba la corrección de estos datos en cuanto a los requisitos del usuario.
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

        // Comprobaciones previas a la inserción de la reserva
        if(reserva.getNombreCliente() == null){
            Log.d("ReservaRepository", "El nombre de una reserva nod ebe ser nulo");
            return -1;
        } else if (reserva.getNombreCliente().isEmpty()){
            Log.d("ReservaRepository", "El nombre de una reserva nod ebe ser vacío");
            return -1;
        } else if (reserva.getFechaEntrada() == null || reserva.getFechaSalida() == null){
            Log.d("ReservaRepository", "Las fechas de una reserva no deben ser nulas");
            return -1;
        } else if (reserva.getFechaEntrada().isEmpty() || reserva.getFechaSalida().isEmpty()){
            Log.d("ReservaRepository", "Las fechas de una reserva no deben ser vacías");
            return -1;
        }

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

        // Comprobaciones previas a la actualización de la reserva
        if(reserva.getNombreCliente() == null){
            Log.d("ReservaRepository", "El nombre de una reserva nod ebe ser nulo");
            return -1;
        } else if (reserva.getNombreCliente().isEmpty()){
            Log.d("ReservaRepository", "El nombre de una reserva nod ebe ser vacío");
            return -1;
        } else if (reserva.getFechaEntrada() == null || reserva.getFechaSalida() == null){
            Log.d("ReservaRepository", "Las fechas de una reserva no deben ser nulas");
            return -1;
        } else if (reserva.getFechaEntrada().isEmpty() || reserva.getFechaSalida().isEmpty()){
            Log.d("ReservaRepository", "Las fechas de una reserva no deben ser vacías");
            return -1;
        } else if(reserva.getTlfCliente() == null) {
            Log.d("ReservaRepository", "El tlf de una reserva no puede ser nulo");
            return -1;
        } else if(reserva.getTlfCliente() / 1000000000 > 0 || reserva.getTlfCliente() /100000000 <=0) {
            Log.d("ReservaRepository", "El tlf de una reserva debe tener exactamente 9 dígitos");
            return -1;
        }

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
        // Comprobaciones previas al borrado de una reserva
        if(reserva.getNombreCliente() == null){
            Log.d("ReservaRepository", "El nombre de una reserva nod ebe ser nulo");
            return -1;
        } else if (reserva.getNombreCliente().isEmpty()){
            Log.d("ReservaRepository", "El nombre de una reserva nod ebe ser vacío");
            return -1;
        }

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

    /** Devuelve la reserva con el ID dado
     * @param ID El ID de la reserva
     * @return La reserva con el ID dado
     */
    public Reserva getReservaByID(int ID) {
        return mReservaDao.getReservaByID(ID);
    }
}
