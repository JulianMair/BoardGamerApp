package de.iu.boardgame.feature_termine.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.data.MeetingRepository;

public class MeetingViewModel extends AndroidViewModel {
    private MeetingRepository repository;
    private final LiveData<List<Meeting>> allMeetings;

    public MeetingViewModel(Application application){
        super(application);
        repository = new MeetingRepository(application);

        allMeetings = repository.getAllMeetings();
    }


    public LiveData<List<Meeting>> getAllMeetings() {
        return allMeetings;
    }

    public void insert(Meeting meeting) {
        repository.insert(meeting);
    }
}
