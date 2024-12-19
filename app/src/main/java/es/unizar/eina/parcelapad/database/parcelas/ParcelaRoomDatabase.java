package es.unizar.eina.parcelapad.database.parcelas;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Parcela.class}, version = 1, exportSchema = false)
public abstract class ParcelaRoomDatabase extends RoomDatabase {

    public abstract ParcelaDao parcelaDao();

    private static volatile ParcelaRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ParcelaRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ParcelaRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ParcelaRoomDatabase.class, "parcela_database")
                            .fallbackToDestructiveMigration() // Permite la migración de la base de datos
                            .addCallback(sRoomDatabaseCallback)
                            .build();
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
                ParcelaDao dao = INSTANCE.parcelaDao();
                dao.deleteAll();

                Parcela parcela1 = new Parcela("Parcela 1 nombre", "Esto es una parcela de ejemplo 1",10,125.5);
                dao.insert(parcela1);
                Parcela parcela2 = new Parcela("Parcela 2 nombre", "Esto es una parcela de ejemlo 2", 12, 134.31);
                dao.insert(parcela2);
                Parcela parcela3 = new Parcela("Parcela 3 nombre", "Esto es una parcela de ejemlo 3", 33, 1200.56);
                dao.insert(parcela3);
            });
        }
    };

}
