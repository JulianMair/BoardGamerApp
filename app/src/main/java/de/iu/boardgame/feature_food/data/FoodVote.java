package de.iu.boardgame.feature_food.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(
        tableName = "food_votes",
        primaryKeys = {"meeting_id", "user_id"}
)
public class FoodVote {
    @ColumnInfo(name="meeting_id")
    public int meetingId;
    @ColumnInfo(name="user_id")
    public long userId;
    @ColumnInfo(name="food_type")
    public String foodType;

    public FoodVote(int meetingId, long userId, String foodType) {
        this.meetingId = meetingId;
        this.userId = userId;
        this.foodType = foodType;
    }
}
