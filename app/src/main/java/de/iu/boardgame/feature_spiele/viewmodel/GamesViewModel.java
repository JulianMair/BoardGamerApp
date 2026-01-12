package de.iu.boardgame.feature_spiele.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_spiele.data.Game;
import de.iu.boardgame.feature_spiele.data.GamesRepository;

/**
 * ViewModel für Spiele (MVVM).
 *
 * Aufgabe:
 * - Liefert LiveData an die UI (z.B. Liste aller Spiele)
 * - Delegiert Schreiboperationen (insert/update/delete) an das Repository
 */
public class GamesViewModel extends AndroidViewModel {

    /**
     * Repository kapselt Zugriff auf Room/DAO und Threading (Executor).
     */
    private final GamesRepository repository;

    /**
     * Beobachtbare Liste aller Spiele.
     * Aktualisiert sich automatisch, sobald sich die DB ändert.
     */
    private final LiveData<List<Game>> allGames;

    public GamesViewModel(@NonNull Application application) {
        super(application);
        repository = GamesRepository.getInstance(application);
        allGames = repository.getAllGames();
    }

    /**
     * Liefert alle Spiele als LiveData.
     */
    public LiveData<List<Game>> getAllGames() {
        return allGames;
    }

    /**
     * Speichert ein neues Spiel.
     */
    public void insert(@NonNull Game game) {
        repository.insert(game);
    }

    /**
     * Aktualisiert ein bestehendes Spiel über Game.id.
     */
    public void update(@NonNull Game game) {
        repository.update(game);
    }

    /**
     * Löscht ein Spiel anhand der ID.
     */
    public void deleteById(long gameId) {
        repository.deleteById(gameId);
    }

    /**
     * Liefert ein einzelnes Spiel als LiveData (für Detail-/Edit-Screen).
     */
    public LiveData<Game> getCurrentGame(long gameId) {
        return repository.getCurrentGame(gameId);
    }
}
