package de.iu.boardgame.feature_spiele;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "games")
public class Game {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name;

    public int durationMinutes;

    @NonNull
    public String category;

    public Game(@NonNull String name, int durationMinutes, @NonNull String category) {
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.category = category;
    }
}

