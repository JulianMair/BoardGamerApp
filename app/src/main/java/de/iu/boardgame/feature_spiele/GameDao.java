package de.iu.boardgame.feature_spiele;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Game game);

    @Query(value = "SELECT * FROM games ORDER BY name")
    List<Game> getAll();

    @Query(value = "DELETE FROM games")
    void deleteAll();
}

