package de.iu.boardgame.feature_user.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.iu.boardgame.feature_user.data.User;
import de.iu.boardgame.feature_user.data.UsersRepository;

public class UsersViewModel extends AndroidViewModel {
    private final UsersRepository repository;
    private final LiveData<List<User>> allUsers;

    public UsersViewModel(@NonNull Application application) {
        super(application);
        repository = UsersRepository.getInstance(application);
        allUsers = repository.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers(){ return allUsers; }

    public void createUser(User user, UsersRepository.InsertCallback callback){
        repository.insertWithCallback(user, callback);
    }

    public void deleteUser(long id){
        repository.deleteById(id);
    }
}
