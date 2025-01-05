package es.unizar.eina.welcome.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.unizar.eina.parcelapad.database.parcelas.Parcela;
import es.unizar.eina.parcelapad.database.parcelas.ParcelaDao;
import es.unizar.eina.reservapad.database.reservas.ParcelaEnReserva;
import es.unizar.eina.reservapad.database.reservas.ParcelaEnReservaDao;
import es.unizar.eina.reservapad.database.reservas.Reserva;
import es.unizar.eina.reservapad.database.reservas.ReservaDao;

@Database(entities = {Parcela.class, Reserva.class, ParcelaEnReserva.class}, version = 2, exportSchema = false)
public abstract class UnifiedRoomDatabase extends RoomDatabase {

    public abstract ParcelaDao parcelaDao();
    public abstract ReservaDao reservaDao();
    public abstract ParcelaEnReservaDao parcelaEnReservaDao();

    private static volatile UnifiedRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static UnifiedRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UnifiedRoomDatabase.class) {
                if (INSTANCE == null) {
                    context.deleteDatabase("unified_db");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UnifiedRoomDatabase.class, "unified_db")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {

            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Inserta datos iniciales en todas las tablas
                UnifiedRoomDatabase database = INSTANCE;

                if (database != null) {
                    // Inserta datos de la tabla "Parcelas"
                    ParcelaDao parcelaDao = database.parcelaDao();
                    parcelaDao.deleteAll(); // Limpia tabla
                    parcelaDao.insert(new Parcela("Parcela 1", "Ejemplo 1", 10, 125.5));
                    parcelaDao.insert(new Parcela("Parcela 2", "Ejemplo 2", 12, 134.31));
                    parcelaDao.insert(new Parcela("Parcela 3", "Ejemplo 3", 33, 1200.56));

                    // Inserta datos de la tabla "Reservas"
                    ReservaDao reservaDao = database.reservaDao();
                    reservaDao.deleteAll(); // Limpia tabla
                    reservaDao.insert(new Reserva("Juan Luis Gonzalez", 676686696, "07/11/2023", "08/11/2023"));
                    reservaDao.insert(new Reserva("Maria Perez", 676686697, "09/11/2023", "10/11/2023"));
                    reservaDao.insert(new Reserva("Pedro Martinez", 676686698, "11/11/2023", "12/11/2023"));

                    // Inserta datos de la tabla "ParcelasEnReserva"
                    ParcelaEnReservaDao parcelaEnReservaDao = database.parcelaEnReservaDao();
                    parcelaEnReservaDao.deleteAll(); // Limpia tabla
                }
            });
        }
    };
}
