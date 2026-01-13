package de.iu.boardgame.feature_spiele.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_termine.data.AppDatabase;

/**
 * Repository für den Zugriff auf Game-Daten.
 *
 * Kapselt den Zugriff auf Room (GameDao) und stellt eine saubere API
 * für das GameViewModel bereit.
 *
 * - Verwendet das Singleton-Pattern
 * - Trennt Datenquelle (Room) von der UI
 * - Führt Schreiboperationen asynchron aus
 */
public class GamesRepository {

    // Singleton-Instanz (thread-sicher)
    private static volatile GamesRepository INSTANCE;

    // DAO für Game-Entitäten
    private final GameDao gameDao;

    // LiveData mit allen gespeicherten Games
    private final LiveData<List<Game>> allGames;

    /**
     * Privater Konstruktor – erzwingt Nutzung von getInstance().
     * Initialisiert Datenbank und DAO.
     */
    private GamesRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        gameDao = db.gameDao();
        allGames = gameDao.getAllGames();
    }

    /**
     * Liefert die Singleton-Instanz des Repositories.
     * Double-Checked Locking für Thread-Sicherheit.
     */
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

    /**
     * Gibt alle Games als LiveData zurück.
     * Änderungen in der DB werden automatisch an die UI weitergeleitet.
     */
    public LiveData<List<Game>> getAllGames() {
        return allGames;
    }

    /**
     * Fügt ein neues Game in die Datenbank ein.
     * Wird asynchron ausgeführt (kein Blockieren des UI-Threads).
     */
    public void insert(Game game){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                gameDao.insert(game);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Aktualisiert ein bestehendes Game.
     */
    public void update(Game game){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                gameDao.update(game);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Löscht ein Game anhand seiner ID.
     */
    public void deleteById(long id) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                gameDao.deleteById(id)
        );
    }

    /**
     * Liefert ein einzelnes Game als LiveData.
     * Nützlich für Detail- oder Edit-Screens.
     */
    public LiveData<Game> getCurrentGame(long gameId) {
        return gameDao.getById(gameId);
    }
}
