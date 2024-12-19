package es.unizar.eina.reservapad.database.reservas;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.time.LocalDate;

@Database(entities = {Reserva.class}, version = 2, exportSchema = false)
public abstract class ReservaRoomDatabase extends RoomDatabase {

    public abstract ReservaDao reservaDao();

    private static volatile ReservaRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ReservaRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ReservaRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ReservaRoomDatabase.class, "parcela_database")
                            .fallbackToDestructiveMigration() // Permite la migración de la base de datos
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more notes, just add them.
                ReservaDao dao = INSTANCE.reservaDao();
                dao.deleteAll();

                Reserva reserva1 = new Reserva("Juan Luis Gonzalez", 676686696,"7/11/2023", "8/11/2023");
                dao.insert(reserva1);
                Reserva reserva2 = new Reserva("Maria Perez", 676686697, "9/11/2023" , "10/11/2023");
                dao.insert(reserva2);
                Reserva reserva3 = new Reserva("Pedro Martinez", 676686698, "11/11/2023" , "12/11/2023");
                dao.insert(reserva3);
            });
        }
    };

}
