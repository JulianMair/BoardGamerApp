package de.iu.boardgame.feature_termine.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.iu.boardgame.feature_abstimmung.data.Vote;
import de.iu.boardgame.feature_abstimmung.data.VoteDao;
import de.iu.boardgame.feature_spiele.data.Game;
import de.iu.boardgame.feature_spiele.data.GameDao;
import de.iu.boardgame.feature_user.data.User;
import de.iu.boardgame.feature_user.data.UserDao;

@Database(entities = {Meeting.class, Game.class, Vote.class, User.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MeetingDao meetingDao();

    public abstract GameDao gameDao();

    public abstract VoteDao voteDao();

    public abstract UserDao userDao();

    // Signalton Logik enthält die EInzige Instanz der DB
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    // Execute fürs Repository
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final Callback dbCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                try {
                    if (INSTANCE != null) {
                        GameDao dao = INSTANCE.gameDao();

                        // ---------- Dummy Games ----------
                        GameDao gameDao = INSTANCE.gameDao();
                        if (gameDao.getAll().isEmpty()) {
                            gameDao.insert(new Game("Catan", 90, "Strategie"));
                            gameDao.insert(new Game("Codenames", 30, "Party"));
                            gameDao.insert(new Game("Carcassonne", 45, "Familie"));
                        }

                        // ---------- Dummy Meetings ----------
                        MeetingDao meetingDao = INSTANCE.meetingDao();
                        if (meetingDao.countMeetings() == 0) {

                            long now = System.currentTimeMillis();
                            long oneDay = 24L * 60 * 60 * 1000;

                            meetingDao.create(new Meeting(
                                    "Spieleabend bei Anna",
                                    now + oneDay,
                                    "Bei Anna zuhause",
                                    1L,
                                    "open"
                            ));

                            meetingDao.create(new Meeting(
                                    "Boardgame Night",
                                    now + 3 * oneDay,
                                    "Wohnung Markus",
                                    1L,
                                    "open"
                            ));

                            meetingDao.create(new Meeting(
                                    "Familien-Spieleabend",
                                    now - oneDay,
                                    "Elternhaus",
                                    2L,
                                    "closed"
                            ));
                        }

                        // ---------- Dummy Users ----------
                        try {
                            UserDao userDao = INSTANCE.userDao();
                            if (userDao.getAll().isEmpty()) {
                                userDao.insert(new User("Anna", "anna@example.com", "0171123456", "Musterstrasse 1", true));
                                userDao.insert(new User("Markus", "markus@example.com", "0171987654", "Hauptstrasse 5", false));
                            }
                        } catch (Exception ignored) { }
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
