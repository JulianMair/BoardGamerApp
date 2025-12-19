package de.iu.boardgame.feature_spiele.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room Entity für ein Spiel.
 *
 * - id wird von Room automatisch vergeben (autoGenerate)
 * - gameTitle und category sind Pflichtfelder (@NonNull)
 */
@Entity(tableName = "games")
public class Game {

    /**
     * Primärschlüssel (wird beim Insert automatisch gesetzt).
     */
    @PrimaryKey(autoGenerate = true)
    public long id;

    /**
     * Anzeigename des Spiels (Pflichtfeld).
     */
    @NonNull
    @ColumnInfo(name = "gameTitle")
    public String gameTitle;

    /**
     * Spieldauer in Minuten (optional, 0 wenn nicht gesetzt).
     */
    @ColumnInfo(name = "gameDuration")
    public int gameDuration;

    /**
     * Kategorie (Pflichtfeld), z.B. "Party", "Strategie", ...
     */
    @NonNull
    @ColumnInfo(name = "category")
    public String category;

    public Game(@NonNull String gameTitle, int gameDuration, @NonNull String category) {
        this.gameTitle = gameTitle;
        this.gameDuration = gameDuration;
        this.category = category;
    }
}
