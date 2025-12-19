package de.iu.boardgamer.feature_user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;


public interface userDao {

    // CRUD
    //CREATE
    @Insert
    void create(User... users);

    //READ
    @Query("SELECT * FROM users")
    List<User> getAll();

   // Update? -  @Query("SELECT * FROM users WHERE userId = :id")
   //            User getById(int id);


}
