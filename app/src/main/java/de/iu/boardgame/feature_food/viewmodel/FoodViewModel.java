package de.iu.boardgame.feature_food.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_food.data.FoodRepository;
import de.iu.boardgame.feature_food.data.FoodVote;
import de.iu.boardgame.feature_food.data.FoodVoteResult;
import de.iu.boardgame.feature_user.data.UserDao;
import de.iu.boardgame.feature_termine.data.AppDatabase;

public class FoodViewModel extends AndroidViewModel {
    private final FoodRepository repository;
    private final UserDao userDao;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        repository = new FoodRepository(application);
        userDao = AppDatabase.getDatabase(application).userDao();
    }

    public void vote(int meetingId, long userId, String foodType) {
        repository.vote(new FoodVote(meetingId, userId, foodType));
    }

    public LiveData<FoodVote> getVoteByUser(int meetingId, long userId) {
        return repository.getVoteByUser(meetingId, userId);
    }

    public LiveData<List<FoodVoteResult>> getResults(int meetingId) {
        return repository.getResults(meetingId);
    }

    public LiveData<Integer> getVotedUserCount(int meetingId) {
        return repository.getVotedUserCount(meetingId);
    }

    public LiveData<Integer> getTotalUserCount() {
        return userDao.getUserCountLive();
    }
}
