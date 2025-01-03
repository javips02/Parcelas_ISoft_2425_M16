package es.unizar.eina.reservapad.database.reservas;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import es.unizar.eina.welcome.db.UnifiedRoomDatabase;

/**
 * Clase que gestiona el acceso la fuente de datos.
 * Interacciona con la base de datos a través de las clases ParcelaEnReservaRoomDatabase y ParcelaEnReservaDao.
 */
public class ParcelaEnReservaRepository {
    private final ParcelaEnReservaDao mParcelaEnReservaDao;
    private final LiveData<List<ParcelaEnReserva>> mAllParcelasEnReserva;
    private final long TIMEOUT = 15000;

    /**
     * Constructor de ParcelaEnReservaRepository utilizando el contexto de la aplicación para instanciar la base de datos.
     */
    public ParcelaEnReservaRepository(Application application) {
        UnifiedRoomDatabase db = UnifiedRoomDatabase.getDatabase(application);
        mParcelaEnReservaDao = db.parcelaEnReservaDao();
        mAllParcelasEnReserva = mParcelaEnReservaDao.getPR();
    }

    /**
     * Devuelve un objeto de tipo LiveData con todas las parcelas en reserva.
     * Room ejecuta todas las consultas en un hilo separado.
     * El objeto LiveData notifica a los observadores cuando los datos cambian.
     */
    public LiveData<List<ParcelaEnReserva>> getAllParcelasEnReserva() {
        return mAllParcelasEnReserva;
    }

    /**
     * Inserta una parcela en reserva en la base de datos
     * @param parcelaEnReserva La parcela en reserva a insertar
     * @return Si la parcela se ha insertado correctamente, devuelve el id generado. En caso
     *         contrario, devuelve -1 para indicar el fallo.
     */
    public long insert(ParcelaEnReserva parcelaEnReserva) {
        Future<Long> future = ParcelaEnReservaRoomDatabase.databaseWriteExecutor.submit(() ->
                mParcelaEnReservaDao.insert(parcelaEnReserva)
        );

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ParcelaEnReservaRepository", "Error al insertar la parcela en reserva", ex);
            return -1;
        }
    }

    /**
     * Actualiza una parcela en reserva en la base de datos
     * @param parcelaEnReserva La parcela en reserva a actualizar
     * @return Si la parcela se ha actualizado correctamente, devuelve el número de filas actualizadas.
     *         En caso contrario, devuelve -1 para indicar el fallo.
     */
    public long update(ParcelaEnReserva parcelaEnReserva) {
        Future<Integer> future = ParcelaEnReservaRoomDatabase.databaseWriteExecutor.submit(() ->
                mParcelaEnReservaDao.update(parcelaEnReserva)
        );

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ParcelaEnReservaRepository", "Error al actualizar la parcela en reserva", ex);
            return -1;
        }
    }

    /**
     * Elimina una parcela en reserva de la base de datos
     * @param parcelaEnReserva La parcela en reserva a eliminar
     * @return Si la parcela se ha eliminado correctamente, devuelve el número de filas eliminadas.
     *         En caso contrario, devuelve -1 para indicar el fallo.
     */
    public int delete(ParcelaEnReserva parcelaEnReserva) {
        Future<Integer> future = ParcelaEnReservaRoomDatabase.databaseWriteExecutor.submit(() ->
                mParcelaEnReservaDao.delete(parcelaEnReserva)
        );

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ParcelaEnReservaRepository", "Error al eliminar la parcela en reserva", ex);
            return -1;
        }
    }

    /**
     * Elimina todas las parcelas en reserva de la base de datos
     */
    public void deleteAll() {
        ParcelaEnReservaRoomDatabase.databaseWriteExecutor.execute(mParcelaEnReservaDao::deleteAll);
    }

    /**
     * Busca una parcela en reserva por el nombre de la reserva
     * @param nombreReserva El nombre de la reserva a buscar
     * @return La parcela en reserva encontrada o null si no existe
     */
    public ParcelaEnReserva findByReserva(String nombreReserva) {
        Future<ParcelaEnReserva> future = ParcelaEnReservaRoomDatabase.databaseWriteExecutor.submit(() ->
                mParcelaEnReservaDao.findByReserva(nombreReserva)
        );

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ParcelaEnReservaRepository", "Error al buscar por reserva", ex);
            return null;
        }
    }

    /**
     * Busca una parcela en reserva por el nombre de la parcela
     * @param nombreParcela El nombre de la parcela a buscar
     * @return La parcela en reserva encontrada o null si no existe
     */
    public ParcelaEnReserva findByParcela(String nombreParcela) {
        Future<ParcelaEnReserva> future = ParcelaEnReservaRoomDatabase.databaseWriteExecutor.submit(() ->
                mParcelaEnReservaDao.fundByParcela(nombreParcela)
        );

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ParcelaEnReservaRepository", "Error al buscar por parcela", ex);
            return null;
        }
    }

    /**
     * Verifica si una parcela está en una reserva específica.
     * @param parcelaNombre El nombre de la parcela.
     * @param reservaID El ID de la reserva.
     * @return true si la parcela está en la reserva, false en caso contrario.
     */
    public boolean isParcelaInReserva(String parcelaNombre, int reservaID) {
        Future<Boolean> future = ParcelaEnReservaRoomDatabase.databaseWriteExecutor.submit(() ->
                mParcelaEnReservaDao.existsInReserva(parcelaNombre, reservaID)
        );

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ParcelaEnReservaRepository", "Error al verificar la parcela en la reserva", ex);
            return false;
        }
    }

    /**
     * Comprueba si una parcela está en alguna reserva que se solape con las fechas dadas.
     *
     * @param nombreParcela El nombre de la parcela a comprobar.
     * @param fechaEntrada La fecha de entrada de tu reserva.
     * @param fechaSalida La fecha de salida de tu reserva.
     * @return true si la parcela está en una reserva que se solape con tus fechas; false en caso contrario.
     */
    public boolean isParcelaYSolapa(String nombreParcela, String fechaEntrada, String fechaSalida) {
        Future<Boolean> future = ParcelaEnReservaRoomDatabase.databaseWriteExecutor.submit(() ->
                mParcelaEnReservaDao.isParcelaYSolapa(nombreParcela, fechaEntrada, fechaSalida)
        );

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.e("ParcelaEnReservaRepository", "Error al comprobar solapamiento", ex);
            return false;
        }
    }


}
