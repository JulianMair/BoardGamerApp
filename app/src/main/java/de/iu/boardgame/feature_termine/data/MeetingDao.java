package de.iu.boardgame.feature_termine.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

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

    //UPDATE
    @Update
    void update(Meeting meeting);

    //DELETE
    @Delete
    void delete(Meeting meeting);

    //Löschen per ID
    @Query("DELETE FROM meeting_table WHERE meeting_id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM meeting_table ORDER BY timestamp")
    LiveData<List<Meeting>> getAllMeetings();

    @Query("SELECT COUNT(*) FROM meeting_table")
    int countMeetings();






}
