package de.iu.boardgame;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.iu.boardgame.feature_termine.Meeting;
import de.iu.boardgame.feature_termine.MeetingDao;

@Database(entities = {Meeting.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
   public abstract MeetingDao meetingDao();

   // Signalton Logik
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    // Execute fürs Repository
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //Methode mit der das Repository die DB bekommt
    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    //Erstellen/Öffnen der DB
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "meeting_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
