package de.iu.boardgame.feature_abstimmung;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertVote(Vote vote);

    @Query("DELETE FROM votes WHERE meeting_id = :meetingId AND user_id = :userId AND game_id = :gameId")
    int deleteVote(long meetingId, long userId, long gameId);

    @Query("SELECT COUNT(*) FROM votes WHERE meeting_id = :meetingId AND user_id = :userId")
    int countVotesByUser(long meetingId, long userId);

    @Query("SELECT g.id AS id, g.name AS name, g.durationMinutes AS durationMinutes,  " +
            "g.category AS category, " +
            "(SELECT COUNT(*) FROM votes v WHERE v.meeting_id = :meetingId AND v.game_id = g.id) AS voteCount," +
            " (SELECT COUNT(*) FROM votes v2 WHERE v2.meeting_id = :meetingId AND v2.game_id = g.id AND v2.user_id = :userId)" +
            " AS votedByMe FROM games g ORDER BY voteCount DESC, name ASC")
    List<GameVoteInfo> getGamesWithVotes(long meetingId, long userId);
}
