package de.iu.boardgame.feature_user.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_termine.data.AppDatabase;

public class UsersRepository {
    private static volatile UsersRepository INSTANCE;

    private final UserDao userDao;
    private final LiveData<List<User>> allUsers;

    private UsersRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
    }

    public static UsersRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (UsersRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UsersRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void insert(User user){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try { userDao.insert(user); } catch (Exception e) { e.printStackTrace(); }
        });
    }

    public User getUserByIdSync(long id) {
        return userDao.getUserByIdSync(id);
    }

    public interface InsertCallback { void onInserted(long id); }

    public void insertWithCallback(User user, InsertCallback callback){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                long id = userDao.insert(user);
                if (callback != null) callback.onInserted(id);
            } catch (Exception e) { e.printStackTrace(); if (callback!=null) callback.onInserted(-1); }
        });
    }

    public void update(User user){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try { userDao.update(user); } catch (Exception e) { e.printStackTrace(); }
        });
    }

    public void deleteById(long id) {
        AppDatabase.databaseWriteExecutor.execute(() -> userDao.deleteById(id));
    }

    public LiveData<User> getCurrentUser(long userId) {
        return userDao.getById(userId);
    }
}
