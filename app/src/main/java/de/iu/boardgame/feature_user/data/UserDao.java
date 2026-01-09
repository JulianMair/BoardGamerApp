package de.iu.boardgame.feature_user.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Update
    int update(User user);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    LiveData<User> getById(long id);

    @Query("DELETE FROM users WHERE id = :id")
    void deleteById(long id);

    @Query("SELECT * FROM users ORDER BY name")
    List<User> getAll();

    @Query("SELECT * FROM users ORDER BY name")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM users WHERE id = :id")
    User getUserByIdSync(long id);
}
