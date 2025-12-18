package de.iu.boardgame.feature_spiele.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "games")
public class Game {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    @ColumnInfo(name="gameTitle")
    public String gameTitle;

    @ColumnInfo(name="gameDuration")
    public int gameDuration;

    @NonNull
    @ColumnInfo(name="category")
    public String category;

    public Game(@NonNull String gameTitle, int gameDuration, @NonNull String category) {
        this.gameTitle = gameTitle;
        this.gameDuration = gameDuration;
        this.category = category;
    }
}

