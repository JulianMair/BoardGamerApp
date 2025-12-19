package de.iu.boardgame.feature_spiele.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
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

    @Query("SELECT * FROM games WHERE id = :id LIMIT 1")
    LiveData<Game> getById(long id);

    @Query("DELETE FROM games WHERE id = :id")
    void deleteById(long id);

    @Query("SELECT * FROM games ORDER BY gameTitle")
    List<Game> getAll();

    @Query("SELECT * FROM games ORDER BY gameTitle")
    LiveData<List<Game>> getAllGames();
}
