package de.iu.boardgame.feature_termine.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Zentrale Datenbank-Klasse (Room Database).
 * Hier werden alle Tabellen (Entities) registriert und die Datenbank-Instanz verwaltet.
 * * ACHTUNG TEAM: Wenn ihr neue Tabellen (z.B. Player) hinzufügt, müsst ihr sie
 * oben in der @Database Annotation bei "entities" ergänzen!
 */
@Database(entities = {Meeting.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Zugriffspunkt auf das DAO (Data Access Object) für Meetings.
     * Room generiert den Code für diese Methode automatisch im Hintergrund.
     * Für neue Tabellen (z.B. PlayerDao) hier einfach eine neue abstrakte Methode hinzufügen.
     */
    public abstract MeetingDao meetingDao();

    // SINGLETON-PATTERN:
    // Wir wollen nur EINE offene Datenbank-Verbindung für die gesamte App (Performance).
    // 'volatile' stellt sicher, dass alle Threads sofort sehen, wenn die Instanz erstellt wurde.
    private static volatile AppDatabase INSTANCE;

    // Anzahl der Threads für Hintergrundoperationen
    private static final int NUMBER_OF_THREADS = 4;

    /**
     * ExecutorService für Datenbank-Schreibvorgänge.
     * WICHTIG: Datenbankzugriffe dürfen NIEMALS im Main-Thread (UI) laufen,
     * sonst friert die App ein. Das Repository nutzt diesen Executor.
     */
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Holt die Datenbank-Instanz. Wenn noch keine existiert, wird eine erstellt.
     * @param context Der App-Kontext (meist ApplicationContext)
     * @return Die Singleton-Instanz der Datenbank
     */
    public static AppDatabase getDatabase(final Context context) {
        // Erste Prüfung: Gibt es schon eine Instanz?
        if (INSTANCE == null) {
            // synchronized verhindert, dass zwei Threads gleichzeitig eine DB erstellen (Race Condition)
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "boardgame_database")
                                    // WICHTIG: Bei Änderungen an der Tabellenstruktur (Version-Erhöhung)
                                    // wird hier die alte DB gelöscht und neu erstellt.
                                    // TODO: Für die Produktion später Migrations-Skripte schreiben!
                                    .fallbackToDestructiveMigration()
                                    .build();
                }
            }
        }
        return INSTANCE;
    }


}
