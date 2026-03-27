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


    //Inner Join um zu den Ratings den User anzugeben
    @Query("""
        SELECT
            r.id,
            r.meeting_id AS meetingId,
            r.userId,
            r.hostRating,
            r.foodRating,
            r.eveningRating,
            r.comment,
            r.timestamp,
            u.name AS username
        FROM meeting_ratings r
        INNER JOIN users u ON r.userId = u.id
        WHERE r.meeting_id = :meetingId
        ORDER BY r.timestamp DESC
    """)
    LiveData<List<RatingWithUser>> getRatingsForMeetingWithUser(int meetingId);

    @Delete
    void delete(MeetingRating rating);
}
