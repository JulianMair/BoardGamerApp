package de.iu.boardgame.feature_spiele;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.iu.boardgame.logging.AppLog;

@Database(entities = {Game.class, de.iu.boardgame.feature_abstimmung.Vote.class}, version = 2, exportSchema = false)
public abstract class GamesDatabase extends RoomDatabase {

    private static volatile GamesDatabase INSTANCE;
    private static final ExecutorService DB_EXECUTOR = Executors.newSingleThreadExecutor();

    public abstract GameDao gameDao();
    public abstract de.iu.boardgame.feature_abstimmung.VoteDao voteDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS votes (" +
                            "meeting_id INTEGER NOT NULL, " +
                            "user_id INTEGER NOT NULL, " +
                            "game_id INTEGER NOT NULL, " +
                            "PRIMARY KEY(meeting_id, user_id, game_id))"
            );
            db.execSQL("CREATE INDEX IF NOT EXISTS index_votes_meeting_id ON votes(meeting_id)");
            db.execSQL("CREATE INDEX IF NOT EXISTS index_votes_meeting_id_user_id ON votes(meeting_id, user_id)");
        }
    };

    public static GamesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (GamesDatabase.class) {
                if (INSTANCE == null) {
                    AppLog.d("BGA-DB", "Creating Room instance...");

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), GamesDatabase.class, "games_db")
                            .addCallback(dbCallback)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public static void runDb(Runnable task) {
        DB_EXECUTOR.execute(task);
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

