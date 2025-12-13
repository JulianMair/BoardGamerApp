package de.iu.boardgame.feature_spiele;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.iu.boardgame.logging.AppLog;

@Database(entities = {Game.class}, version = 1, exportSchema = false)
public abstract class GamesDatabase extends RoomDatabase {

    private static volatile GamesDatabase INSTANCE;
    private static final ExecutorService DB_EXECUTOR = Executors.newSingleThreadExecutor();

    public abstract GameDao gameDao();

    public static GamesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (GamesDatabase.class) {
                if (INSTANCE == null) {
                    AppLog.d("BGA-DB", "Creating Room instance...");

                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    GamesDatabase.class,
                                    "games_db"
                            )
                            .addCallback(dbCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback dbCallback = new Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AppLog.i("DB", "onCreate() -> DB FILE WAS CREATED, inserting dummy games...");

            DB_EXECUTOR.execute(() -> {
                try {
                    GameDao dao = INSTANCE.gameDao();

                    dao.insert(new Game("Catan", 90, "Strategie"));
                    dao.insert(new Game("Codenames", 30, "Party"));
                    dao.insert(new Game("Carcassonne", 45, "Familie"));

                    int count = dao.getAll().size();
                    AppLog.i("BGA-DB", "Dummy games inserted. Games in DB now: " + count);
                } catch (Exception e) {
                    AppLog.e("DB", "Dummy insert failed: " + e.getMessage(), e);
                }
            });
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            AppLog.i("DB", "onOpen() -> DB opened (exists already or just created).");
        }
    };
}

