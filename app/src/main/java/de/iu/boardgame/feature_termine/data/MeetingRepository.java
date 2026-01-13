package de.iu.boardgame.feature_termine.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;

/** Verwaltet den Datenzugriff (Repository Pattern) und abstrahiert die Datenbanklogik. */
public class MeetingRepository {

    // Volatile für Thread-Safety (Atomic Updates)
    private static volatile MeetingRepository INSTANCE;

    // Das DAO ist das Werkzeug, um die SQL-Befehle auszuführen
    private final MeetingDao meetingDao;

    // LiveData: Ein Container, der beobachtet werden kann.
    // Wenn sich die DB ändert, wird diese Liste automatisch aktualisiert.
    private LiveData<List<Meeting>> allMeetings;

    /**
     * Privater Konstruktor: Verhindert, dass von außen einfach 'new MeetingRepository()'
     * aufgerufen werden kann. Nutzung nur über getInstance().
     */
    private MeetingRepository(Application application){
        // Erstellen / Holen der Datenbank-Instanz
        AppDatabase db = AppDatabase.getDatabase(application);

        // Wir initialisieren das DAO über die Datenbank
        meetingDao = db.meetingDao();

        // Wir laden direkt die LiveData-Referenz. Das löst noch keine Query aus,
        // sondern bereitet die Beobachtung vor.
        allMeetings = meetingDao.getAllMeetings();

    }

    /**
     * Singleton-Pattern Implementierung:
     * Stellt sicher, dass es in der gesamten App nur genau EIN Repository gibt.
     * @param application Der App-Context wird für die Datenbank benötigt.
     * @return Die einzige Instanz des Repositories.
     */
    public static MeetingRepository getInstance(Application application) {
        if(INSTANCE == null){
            // synchronized verhindert, dass zwei Threads gleichzeitig eine Instanz erstellen
            synchronized (MeetingRepository.class){
                if(INSTANCE == null){
                    INSTANCE = new MeetingRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    // -------------------- MEETING ----------------------------------------------

    // Ausführung im Background-Thread, um UI-Blockaden zu vermeiden.
    public void insert(Meeting meeting){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                meetingDao.create(meeting);
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    /**
     * Gibt die LiveData-Liste zurück.
     * Das ViewModel beobachtet diese Methode. Sobald ein Insert/Delete passiert,
     * liefert Room automatisch die neuen Daten hierhin.
     */
    public LiveData<List<Meeting>> getAllMeetings() {
        return allMeetings;
    }

    /**
     * Löscht ein Meeting anhand der ID im Hintergrund.
     */
    public void deleteById(int id) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            meetingDao.deleteById(id);
        });
    }

    /**
     * Holt ein einzelnes Meeting für die Detailansicht.
     * Rückgabewert ist LiveData, damit auch Änderungen im Detail-Screen sofort sichtbar werden.
     */
    public LiveData<Meeting> getCurrentMeeting(int meetingId) {
        return meetingDao.getById(meetingId);
    }

    /**
     * Für die Anzeige in der Liste (Gefiltert)
     */
    public LiveData<List<Meeting>> getDisplayMeetings() {
        return meetingDao.getRelevantMeetings(getHistoryLimit());
    }

    public void update(Meeting meeting) {
        meetingDao.update(meeting);
    }

    // Zeitgrenze berechnen (3 Tage)
    private long getHistoryLimit() {
        long now = System.currentTimeMillis();
        long threeDaysInMillis = 3L * 24 * 60 * 60 * 1000;
        return now - threeDaysInMillis;
    }
    private long getHistoryLimit1() {
        long now = System.currentTimeMillis();
        // WICHTIG: Das 'L' hinter der 24 sorgt dafür, dass Java als Long rechnet
        // und keinen Überlauf produziert (obwohl 1 Tag noch in Integer passt).
        long oneDayInMillis = 1L * 24 * 60 * 60 * 1000;

        return now - oneDayInMillis;
    }
}
