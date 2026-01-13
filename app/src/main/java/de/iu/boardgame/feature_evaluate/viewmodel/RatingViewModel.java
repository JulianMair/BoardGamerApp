package de.iu.boardgame.feature_evaluate.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_evaluate.data.MeetingRating;
import de.iu.boardgame.feature_evaluate.data.RatingRepository;

public class RatingViewModel extends AndroidViewModel {

    private final RatingRepository repository;
    private final LiveData<List<MeetingRating>> allRatings;

    public RatingViewModel(@NonNull Application application) {
        super(application);
        repository = new RatingRepository(application);
        allRatings = repository.getAllRatings();
    }

    public LiveData<List<MeetingRating>> getAllRatings() {
        return allRatings;
    }


    public LiveData<List<MeetingRating>> getRatingsForMeeting(int meetingId) {
        return repository.getRatingsForMeeting(meetingId);
    }

    public void insert(MeetingRating rating) {
        repository.insert(rating);
    }

    public void delete(MeetingRating rating) {
        repository.delete(rating);
    }
}
