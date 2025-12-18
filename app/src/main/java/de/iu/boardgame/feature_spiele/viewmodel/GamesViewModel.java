package de.iu.boardgame.feature_spiele.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_spiele.data.Game;
import de.iu.boardgame.feature_spiele.data.GamesRepository;

public class GamesViewModel extends AndroidViewModel {
    private final GamesRepository repository;
    private final LiveData<List<Game>> allGames;

    public GamesViewModel(Application application) {
        super(application);
        repository = GamesRepository.getInstance(application);

        allGames = repository.getAllGames();
    }


    public LiveData<List<Game>> getAllGames() {
        return allGames;
    }

    public void insert(Game game) {
        repository.insert(game);
    }

    public void update(Game game) {
        repository.update(game);
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }

    public LiveData<Game> getcurrentGame(long gameId) {
        return repository.getCurrentGame(gameId);
    }
}

