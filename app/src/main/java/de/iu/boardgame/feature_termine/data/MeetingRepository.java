package de.iu.boardgame.feature_termine.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;

public class MeetingRepository {

    private static volatile MeetingRepository INSTANCE;
    private final MeetingDao meetingDao;
    // Erstellt einen Thread pool zur Ausführung der db im Hintergrund
    private final ExecutorService databaseWriteExecutor;
    private LiveData<List<Meeting>> allMeetings;

    //Verhindert neue Instanzen von außen
    private MeetingRepository(Application application){
        //Erstellen der DB
        AppDatabase db = Room.databaseBuilder(application,AppDatabase.class, "boardgame_database").build();
        meetingDao = db.meetingDao();
        allMeetings = meetingDao.getAllMeetings();
        databaseWriteExecutor = Executors.newFixedThreadPool(4);
    }

    public static MeetingRepository getInstance(Application application) {
        if(INSTANCE == null){
            synchronized (MeetingRepository.class){
                if(INSTANCE == null){
                    INSTANCE = new MeetingRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    public void insert(Meeting meeting){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Insert wird im Hintergrund ausgeführt
            meetingDao.create(meeting);
        });
    }

    public LiveData<List<Meeting>> getAllMeetings() {
        return allMeetings;
    }

    public void deleteById(int id) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            meetingDao.deleteById(id);
        });
    }

    public LiveData<Meeting> getCurrentMeeting(int meetingId) {
        return meetingDao.getById(meetingId);
    }
}
