package de.iu.boardgame.feature_spiele.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_termine.data.AppDatabase;

public class GamesRepository {
    private static volatile GamesRepository INSTANCE;

    private final GameDao gameDao;
    private final LiveData<List<Game>> allGames;

    private GamesRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        gameDao = db.gameDao();
        allGames = gameDao.getAllGames();
    }

    public static GamesRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (GamesRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GamesRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<List<Game>> getAllGames() {
        return allGames;
    }

    public void insert(Game game){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try { gameDao.insert(game); } catch (Exception e) { e.printStackTrace(); }
        });
    }

    public void update(Game game){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try { gameDao.update(game); } catch (Exception e) { e.printStackTrace(); }
        });
    }

    public void deleteById(long id) {
        AppDatabase.databaseWriteExecutor.execute(() -> gameDao.deleteById(id));
    }

    public LiveData<Game> getCurrentGame(long gameId) {
        return gameDao.getById(gameId);
    }
}
