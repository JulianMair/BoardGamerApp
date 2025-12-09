package de.iu.boardgame.feature_termine;

public class MeetingViewModel {
    MeetingRepository meetingRepos;

    public MeetingViewModel(){
        meetingRepos = new MeetingRepository();
    }

    public void insert(Meeting meeting){
        meetingRepos.insert(meeting);
    }
}
