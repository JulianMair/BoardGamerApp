package de.iu.boardgame.feature_food.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_termine.data.AppDatabase;

public class FoodRepository {
    private final FoodVoteDao foodVoteDao;

    public FoodRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        foodVoteDao = db.foodVoteDao();
    }

    public void vote(FoodVote vote) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            foodVoteDao.insertOrUpdate(vote);
        });
    }

    public LiveData<FoodVote> getVoteByUser(int meetingId, long userId) {
        return foodVoteDao.getVoteByUser(meetingId, userId);
    }

    public LiveData<List<FoodVoteResult>> getResults(int meetingId) {
        return foodVoteDao.getResults(meetingId);
    }
}
