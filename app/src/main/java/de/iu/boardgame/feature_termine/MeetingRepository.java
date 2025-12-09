package de.iu.boardgame.feature_termine;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.AppDatabase;

public class MeetingRepository {
    AppDatabase meetingDatabase;

    public MeetingRepository(){

    }

    public void insert(Meeting meeting){
        meetingDatabase.meetingDao().create(meeting);
        List<Meeting> testList = new ArrayList<Meeting>();
        testList =  meetingDatabase.meetingDao().getAll();
        meetingDatabase.meetingDao().getAll();
        String test = testList.get(0).getLocation();
        Log.d("HHHHHHHHHHHHHHHHHHHH", test );
    }
}
