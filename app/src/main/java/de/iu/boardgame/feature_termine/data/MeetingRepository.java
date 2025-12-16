package de.iu.boardgame.feature_termine.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeetingRepository {

    private final MeetingDao meetingDao;
    // Erstellt einen Thread pool zur Ausführung der db im Hintergrund
    private final ExecutorService databaseWriteExecutor;
    private LiveData<List<Meeting>> allMeetings;

    public MeetingRepository(Application application){
        //Erstellen der DB
        AppDatabase db = Room.databaseBuilder(application,AppDatabase.class, "boardgame_database").build();
        meetingDao = db.meetingDao();
        allMeetings = meetingDao.getAllMeetings();
        databaseWriteExecutor = Executors.newFixedThreadPool(4);
    }

    public void insert(Meeting meeting){
        databaseWriteExecutor.execute(() -> {
            // Insert wird im Hintergrund ausgeführt
            meetingDao.create(meeting);
        });
    }

    public LiveData<List<Meeting>> getAllMeetings() {
        return allMeetings;
    }
}
