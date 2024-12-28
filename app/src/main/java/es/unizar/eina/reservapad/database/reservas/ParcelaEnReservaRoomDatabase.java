package es.unizar.eina.reservapad.database.reservas;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.unizar.eina.parcelapad.database.parcelas.Parcela;

@Database(entities = {ParcelaEnReserva.class, Parcela.class, Reserva.class}, version = 2, exportSchema = false)
public abstract class ParcelaEnReservaRoomDatabase extends RoomDatabase{
    public abstract ParcelaEnReservaDao parcelaEnReservaDao();

    private static volatile ParcelaEnReservaRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ParcelaEnReservaRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ParcelaEnReservaRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ParcelaEnReservaRoomDatabase.class, "unified_db")
                            .fallbackToDestructiveMigration() // Permite la migración de la base de datos
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                } else { //lenar ocn datos de prueba simplemente
                    ParcelaEnReservaDao dao = INSTANCE.parcelaEnReservaDao();
                    dao.deleteAll();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more notes, just add them.
                ParcelaEnReservaDao dao = INSTANCE.parcelaEnReservaDao();
                dao.deleteAll();
            });
        }
    };
}
