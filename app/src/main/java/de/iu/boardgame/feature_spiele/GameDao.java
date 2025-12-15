package de.iu.boardgame.feature_spiele;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Game game);

    @Update
    int update(Game game);

    @Delete
    int delete(Game game);

    @Query("DELETE FROM games WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM games ORDER BY name")
    List<Game> getAll();

    @Query("DELETE FROM games")
    void deleteAll();
}

