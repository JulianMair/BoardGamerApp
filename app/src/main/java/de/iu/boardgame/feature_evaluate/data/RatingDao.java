package de.iu.boardgame.feature_evaluate.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RatingDao {

    @Insert
    long insert(MeetingRating rating);

    @Query("SELECT * FROM meeting_ratings ORDER BY timestamp DESC")
    LiveData<List<MeetingRating>> getAllRatings();

    @Query("SELECT * FROM meeting_ratings WHERE meeting_id = :meetingId ORDER BY timestamp DESC")
    LiveData<List<MeetingRating>> getRatingsForMeeting(int meetingId);



    @Delete
    void delete(MeetingRating rating);
}
