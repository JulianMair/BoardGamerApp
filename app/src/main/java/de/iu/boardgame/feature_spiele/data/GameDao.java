package de.iu.boardgame.feature_spiele.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO für CRUD-Operationen auf der "games" Tabelle.
 */
@Dao
public interface GameDao {

    /**
     * Insert eines neuen Spiels.
     * OnConflictStrategy.REPLACE: ersetzt Datensatz bei Konflikt.     *
     * @return rowId / neue ID
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Game game);

    /**
     * Aktualisiert ein vorhandenes Spiel (match über PrimaryKey id).
     *
     * @return Anzahl der geänderten Zeilen (0 oder 1)
     */
    @Update
    int update(Game game);

    /**
     * Liefert ein Spiel anhand der ID als LiveData (für Details/Edit Screen).
     */
    @Query("SELECT * FROM games WHERE id = :id LIMIT 1")
    LiveData<Game> getById(long id);

    /**
     * Löscht ein Spiel anhand der ID.
     */
    @Query("DELETE FROM games WHERE id = :id")
    void deleteById(long id);

    /**
     * Liefert alle Spiele synchron.
     */
    @Query("SELECT * FROM games ORDER BY gameTitle")
    List<Game> getAll();

    /**
     * Liefert alle Spiele als LiveData (für UI).
     */
    @Query("SELECT * FROM games ORDER BY gameTitle")
    LiveData<List<Game>> getAllGames();
}
