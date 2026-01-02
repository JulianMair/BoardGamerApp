package de.iu.boardgame.feature_termine.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Das DAO (Data Access Object) ist der "Übersetzer".
 * Es wandelt unsere Java-Methodenaufrufe in SQL-Befehle um.
 * Hier wird KEINE Logik geschrieben, nur definiert.
 */
@Dao
public interface MeetingDao {

    // CRUD
    //CREATE
    @Insert
    void create(Meeting ... meeting);

    //READ
    @Query("SELECT * FROM meeting_table ORDER BY timestamp")
    List<Meeting> getAll();

    @Query("SELECT * FROM meeting_table WHERE meeting_id = :id")
    LiveData<Meeting> getById(int id);
    //Gibt für die Anzeige keine Meetings zurück die drei oder mehr Tage in der Vergangenheit liegen
    @Query("SELECT * FROM meeting_table WHERE status != 'closed' OR timestamp > :historyLimit ORDER BY timestamp")
    LiveData<List<Meeting>> getRelevantMeetings(long historyLimit);

    // --- UPDATE ---
    /**
     * Aktualisiert ein existierendes Meeting.
     * Room sucht anhand der ID (Primary Key) den richtigen Eintrag und überschreibt ihn.
     */
    @Update
    void update(Meeting meeting);

    // --- DELETE ---
    /**
     * Löscht das übergebene Meeting-Objekt aus der Datenbank.
     */
    @Delete
    void delete(Meeting meeting);

    /**
     * Holt ein spezifisches Meeting anhand der ID.
     * Auch hier LiveData, damit wir Änderungen im Detail-Screen sofort sehen.
     */
    @Query("DELETE FROM meeting_table WHERE meeting_id = :id")
    void deleteById(int id);

    // --- READ ---
    /**
     * Diese Methode gibt 'LiveData' zurück. Das ist wie ein Livestream.
     * Wenn sich in der Datenbank etwas ändert, wird die UI automatisch aktualisiert.
     * Das ist die Methode, die wir im ViewModel nutzen sollten!
     */
    @Query("SELECT * FROM meeting_table ORDER BY timestamp")
    LiveData<List<Meeting>> getAllMeetings();

    @Query("SELECT COUNT(*) FROM meeting_table")
    int countMeetings();






}
