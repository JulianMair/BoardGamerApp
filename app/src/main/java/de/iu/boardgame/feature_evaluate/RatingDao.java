package de.iu.boardgame.feature_evaluate;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RatingDao {

    @Insert
    long insert(MeetingRating rating);

    @Query("SELECT * FROM meeting_rating ORDER BY timestamp DESC")
    List<MeetingRating> getAllRatings();

    @Delete
    void delete(MeetingRating rating);
}
