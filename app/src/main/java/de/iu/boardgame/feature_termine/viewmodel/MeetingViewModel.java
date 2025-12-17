package de.iu.boardgame.feature_termine.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.data.MeetingRepository;

public class MeetingViewModel extends AndroidViewModel {
    private final MeetingRepository repository;
    private final LiveData<List<Meeting>> allMeetings;

    public MeetingViewModel(Application application){
        super(application);
        repository = MeetingRepository.getInstance(application);

        allMeetings = repository.getAllMeetings();
    }


    public LiveData<List<Meeting>> getAllMeetings() {
        return allMeetings;
    }

    public void insert(Meeting meeting) {
        int id = meeting.getMeeting_id();
        Log.d("ID_TEST", "create: " + id);
        repository.insert(meeting);
    }

    public void deleteById(int id) {
        Log.d("ID_TEST", "delete: :" + id);
        repository.deleteById(id);
    }

    public LiveData<Meeting> getcurrentMeeting(int meetingId) {
        return repository.getCurrentMeeting(meetingId);
    }
}
