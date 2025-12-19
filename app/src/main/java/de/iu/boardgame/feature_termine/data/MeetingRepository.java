package de.iu.boardgame.feature_termine.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import java.util.List;

public class MeetingRepository {

    private static volatile MeetingRepository INSTANCE;
    //################
    private final MeetingDao meetingDao;
    // Erstellt einen Thread pool zur Ausführung der db im Hintergrund
    //private final ExecutorService databaseWriteExecutor;
    private LiveData<List<Meeting>> allMeetings;

    //Verhindert neue Instanzen von außen
    private MeetingRepository(Application application){
        //Erstellen der DB
        //AppDatabase db = Room.databaseBuilder(application,AppDatabase.class, "boardgame_database").build();
        AppDatabase db = AppDatabase.getDatabase(application);

        // ########
        meetingDao = db.meetingDao();

        allMeetings = meetingDao.getAllMeetings();
        //databaseWriteExecutor = Executors.newFixedThreadPool(4);
    }

    // Erstellt eine INSTANCE oder gibt die bestehende zurück
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

    // -------------------- MEETING ----------------------------------------------
    public void insert(Meeting meeting){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                meetingDao.create(meeting);
            } catch (Exception e){
                e.printStackTrace();
            }
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

    //----------------------END MEETING -------------------------------------------
}
