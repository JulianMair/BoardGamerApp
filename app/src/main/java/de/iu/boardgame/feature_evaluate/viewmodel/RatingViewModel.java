package de.iu.boardgame.feature_evaluate.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_evaluate.data.MeetingRating;
import de.iu.boardgame.feature_evaluate.data.RatingRepository;
import de.iu.boardgame.feature_evaluate.data.RatingWithUser;

public class RatingViewModel extends AndroidViewModel {

    private final RatingRepository repository;

    public RatingViewModel(@NonNull Application application) {
        super(application);
        repository = new RatingRepository(application);
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

    public LiveData<List<RatingWithUser>> getRatingsForMeetingWithUser(int meetingId){
        return repository.getRatingsForMeetingWithUser(meetingId);
    }
}
