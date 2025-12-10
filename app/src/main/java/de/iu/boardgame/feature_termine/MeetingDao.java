package de.iu.boardgame.feature_termine;

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
    @Query("SELECT * FROM meeting_table")
    List<Meeting> getAll();
    @Query("SELECT * FROM meeting_table WHERE meeting_id = :id")
    Meeting getById(int id);

    //UPDATE
    @Update
    void update(Meeting meeting);

    //DELETE
    @Delete
    void delete(Meeting meeting);

    @Query("SELECT * FROM meeting_table ORDER BY date ASC")
    LiveData<List<Meeting>> getAllMeetings();







}
