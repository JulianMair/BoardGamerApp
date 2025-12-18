package de.iu.boardgame.feature_abstimmung;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(
        tableName = "votes",
        primaryKeys = {"meeting_id", "user_id", "game_id"},
        indices = {
                @Index(value = {"meeting_id"}),
                @Index(value = {"meeting_id", "user_id"})
        }
)
public class Vote {
    @ColumnInfo(name="meeting_id")
    public long meeting_id;
    @ColumnInfo(name="user_id")
    public long user_id;
    @ColumnInfo(name="game_id")
    public long game_id;

    public Vote(long meeting_id, long user_id, long game_id) {
        this.meeting_id = meeting_id;
        this.user_id = user_id;
        this.game_id = game_id;
    }
}
