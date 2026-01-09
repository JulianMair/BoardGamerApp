package de.iu.boardgame.feature_termine.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_termine.data.AppDatabase;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.data.MeetingRepository;

/**
 * Das ViewModel ist das "Gehirn" der UI.
 * Speichert die Daten der Activity auch bei Konfigurationsänderungen (z.B. Bildschirm drehen).
 */
public class MeetingViewModel extends AndroidViewModel {
    // Daten Manager
    private final MeetingRepository repository;

    // Cache für die Liste aller Meetings (LiveData)
    private final LiveData<List<Meeting>> allMeetings;
    private final LiveData<List<Meeting>> displayMeetings;

    /**
     * Konstruktor
     * @param application Wird vom System übergeben (von Factory).
     * Initialisiert das Repository
     */
    public MeetingViewModel(Application application){
        super(application);

        // Verbindung zum Repository herstellen (Singleton)
        repository = MeetingRepository.getInstance(application);

        // Hollt den "Live-Ticker" aller Meetings.
        // Das Repository läd die Daten aus der DB
        allMeetings = repository.getAllMeetings();
        displayMeetings = repository.getDisplayMeetings();
    }

    /**
     * Getter für die UI.
     * Die Activity beobachtet (observe) diese Liste.
     */
    public LiveData<List<Meeting>> getAllMeetings() {
        return allMeetings;
    }

    /**
     * Leitet den Speicher-Befehl an das Repository weiter.
     */
    public void insert(Meeting meeting) {
        repository.insert(meeting);
    }

    /**
     * Löscht ein Meeting anhand der ID.
     */
    public void deleteById(int id) {
        Log.d("ID_TEST", "delete: :" + id);
        repository.deleteById(id);
    }

    /**
     * Holt ein einzelnes Meeting für die Detailansicht.
     */
    public LiveData<Meeting> getcurrentMeeting(int meetingId) {
        return repository.getCurrentMeeting(meetingId);
    }

    public void update(Meeting meeting){
        AppDatabase.databaseWriteExecutor.execute (() -> {
            repository.update(meeting);
        });
    }

    // Getter für die UI
    public LiveData<List<Meeting>> getDisplayMeetings() { return displayMeetings; }
}
