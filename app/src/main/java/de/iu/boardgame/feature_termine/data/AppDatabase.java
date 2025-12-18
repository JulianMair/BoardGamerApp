package de.iu.boardgame.feature_termine.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.iu.boardgame.feature_abstimmung.Vote;
import de.iu.boardgame.feature_abstimmung.VoteDao;
import de.iu.boardgame.feature_spiele.data.Game;
import de.iu.boardgame.feature_spiele.data.GameDao;

@Database(entities = {Meeting.class, Game.class, Vote.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MeetingDao meetingDao();

    public abstract GameDao gameDao();

    public abstract VoteDao voteDao();

    // Signalton Logik enthält die EInzige Instanz der DB
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    // Execute fürs Repository
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static void runDb(Runnable task) {
        databaseWriteExecutor.execute(task);
    }

    private static final Callback dbCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Prepopulate im Hintergrund
            databaseWriteExecutor.execute(() -> {
                try {
                    if (INSTANCE != null) {
                        GameDao dao = INSTANCE.gameDao();
                        dao.insert(new Game("Catan", 90, "Strategie"));
                        dao.insert(new Game("Codenames", 30, "Party"));
                        dao.insert(new Game("Carcassonne", 45, "Familie"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    };

    //Methode mit der das Repository die DB bekommt
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "boardgame_database"
                            )
                            .addCallback(dbCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
