package es.unizar.eina.parcelapad.database.parcelas;


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
 * Interacciona con la base de datos a través de las clases ParcelaRoomDatabase y ParcelaDao.
 */
public class ParcelaRepository {

    private final ParcelaDao mParcelaDao;
    private final LiveData<List<Parcela>> mAllParcelas;

    private final long TIMEOUT = 15000;

    /**
     * Constructor de ParcelaRepository utilizando el contexto de la aplicación para instanciar la base de datos.
     * Alternativamente, se podría estudiar la instanciación del repositorio con una referencia a la base de datos
     * siguiendo el ejemplo de
     * <a href="https://github.com/android/architecture-components-samples/blob/main/BasicSample/app/src/main/java/com/example/android/persistence/DataRepository.java">architecture-components-samples/.../persistence/DataRepository</a>
     */
    public ParcelaRepository(Application application) {
        ParcelaRoomDatabase db = ParcelaRoomDatabase.getDatabase(application);
        mParcelaDao = db.parcelaDao();
        mAllParcelas = mParcelaDao.getParcelas();
    }

    /** Devuelve un objeto de tipo LiveData con todas las parcelas.
     * Room ejecuta todas las consultas en un hilo separado.
     * El objeto LiveData notifica a los observadores cuando los datos cambian.
     */
    public LiveData<List<Parcela>> getAllParcelas() {
        return mAllParcelas;
    }


    /** Devuelve un objeto de tipo LiveData con todas las parcelas ordenadas alfabéticamente por nembre.
     * Room ejecuta todas las consultas en un hilo separado.
     * El objeto LiveData notifica a los observadores cuando los datos cambian.
     */
    public LiveData<List<Parcela>> getAllParcelasByNombre() {
        return mParcelaDao.getOrderedParcelasByName();
    }

    /** Devuelve un objeto de tipo LiveData con todas las parcelas ordenadas ascendentemente por númeor de ocupantes.
     * Room ejecuta todas las consultas en un hilo separado.
     * El objeto LiveData notifica a los observadores cuando los datos cambian.
     */
    public LiveData<List<Parcela>> getAllParcelasByOcupantes() {
        return mParcelaDao.getOrderedParcelasByOcupantes();
    }

    /** Devuelve un objeto de tipo LiveData con todas las parcelas ordenadas ascendentemente por precio.
     * Room ejecuta todas las consultas en un hilo separado.
     * El objeto LiveData notifica a los observadores cuando los datos cambian.
     */
    public LiveData<List<Parcela>> getAllParcelasByPrecio() {
        return mParcelaDao.getAllParcelasByPrecio();
    }

    /** Inserta una nota nueva en la base de datos
     * @param parcela La parcela consta de: un nombre (parcela.getNombre()) no nulo (parcela.getNombre()!=null) y no vacío
     *             (parcela.getNombre().length()>0); una descripción (parcela.getDesc()) que puede ser nula; un máximo numero de
     *             ocupantes (pacela.getMaxOcupantes()) no nulo (parcela.getMaxOcupantes!= null) y no vacío
     *             (parcela.getMaxOcupantes().lenght>=0); y un precio (parcela.getPrecioParcela()) no nulo
     *             (parcela.getPrecioParcela()!=null) y no vacío (parcela.getPrecioParcela().lenght>0).
     * @return Si la parcela se ha insertado correctamente, devuelve el nombre de la parcela que se ha creado. En caso
     *         contrario, devuelve -1 para indicar el fallo.
     */
    public long insert(Parcela parcela) {
        /* Para que la App funcione correctamente y no lance una excepción, la modificación de la
         * base de datos se debe lanzar en un hilo de ejecución separado
         * (databaseWriteExecutor.submit). Para poder sincronizar la recuperación del resultado
         * devuelto por la base de datos, se puede utilizar un Future.
         */
        Future<Long> future = ParcelaRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.insert(parcela));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    /** Actualiza una parcela en la base de datos
     * @param parcela La parcela que se desea actualizar y que consta de: un nombre (parcela.getNombre()) no nulo (parcela.getNombre()!=null) y no vacío
     *                   (parcela.getNombre().length()>0); una descripción (parcela.getDesc()) que puede ser nula; un máximo numero de
     *                   ocupantes (pacela.getMaxOcupantes()) no nulo (parcela.getMaxOcupantes!= null) y no vacío
     *                   (parcela.getMaxOcupantes().lenght>=0); y un precio (parcela.getPrecioParcela()) no nulo
     *                   (parcela.getPrecioParcela()!=null) y no vacío (parcela.getPrecioParcela().lenght>0).
     * @return Un valor entero con el número de filas modificadas: 1 si el identificador se corresponde con una parcela
     *         previamente insertada; 0 si no existe previamente una parcela con ese identificador, o hay algún problema
     *         con los atributos.
     */
    public int update(Parcela parcela) {
        Future<Integer> future = ParcelaRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.update(parcela));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }


    /** Elimina una nota en la base de datos.
     * @param parcela Objeto nota cuyo atributo identificador (parcela.getNombre()) contiene la clave primaria de la parcela que se
     *             va a eliminar de la base de datos. Se debe cumplir: parcela.getNombre() > 0.
     * @return Un valor entero con el número de filas eliminadas: 1 si el identificador se corresponde con una parcela
     *         previamente insertada; 0 si no existe previamente una parcela con ese identificador o el identificador no es
     *         un valor aceptable.
     */
    public int delete(Parcela parcela) {
        Future<Integer> future = ParcelaRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.delete(parcela));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }
}
