package de.iu.boardgame.feature_food.data;

import androidx.room.ColumnInfo;

public class FoodVoteResult {
    @ColumnInfo(name="food_type")
    public String foodType;
    @ColumnInfo(name="voteCount")
    public int voteCount;
}
