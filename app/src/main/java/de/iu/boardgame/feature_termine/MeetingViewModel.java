package de.iu.boardgame.feature_termine;

import android.app.Application;

public class MeetingViewModel {
    MeetingRepository meetingRepos;

    public MeetingViewModel(Application application){
        meetingRepos = new MeetingRepository(application);
    }

    public void insert(Meeting meeting){
        meetingRepos.insert(meeting);
    }
}
