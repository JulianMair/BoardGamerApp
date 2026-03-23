package de.iu.boardgame.feature_food.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodVoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(FoodVote vote);

    @Query("SELECT * FROM food_votes WHERE meeting_id = :meetingId AND user_id = :userId")
    LiveData<FoodVote> getVoteByUser(int meetingId, long userId);

    @Query("SELECT food_type, COUNT(*) as voteCount FROM food_votes WHERE meeting_id = :meetingId GROUP BY food_type ORDER BY voteCount DESC")
    LiveData<List<FoodVoteResult>> getResults(int meetingId);

    @Query("SELECT COUNT(DISTINCT user_id) FROM food_votes WHERE meeting_id = :meetingId")
    LiveData<Integer> getVotedUserCount(int meetingId);
}
