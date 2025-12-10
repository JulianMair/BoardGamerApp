package de.iu.boardgame.feature_termine;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.iu.boardgame.AppDatabase;

public class MeetingRepository {

    private final MeetingDao meetingDao;
    // Erstellt einen Thread pool zur Ausführung der db im Hintergrund
    private final ExecutorService databaseWriteExecutor;

    public MeetingRepository(Application application){
        //Erstellen der DB
        AppDatabase db = Room.databaseBuilder(application,AppDatabase.class, "boardgame_database").build();
        meetingDao = db.meetingDao();
        databaseWriteExecutor = Executors.newFixedThreadPool(4);
    }

    public void insert(Meeting meeting){
        databaseWriteExecutor.execute(() -> {
            // Insert wird im Hintergrund ausgeführt
            meetingDao.create(meeting);

            List<Meeting> testList = meetingDao.getAll();
            String test = testList.get(0).getLocation();
        });

    }
}
