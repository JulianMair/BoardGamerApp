package de.iu.boardgame.feature_termine.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

// "Erklärt" wie man ein MeetingViewModel mit Application abhänigkeit erstellt
public class MeetingViewModelFactory implements ViewModelProvider.Factory {
    //Speichern der Abhänigkeit
    private final Application application;

    // Konstruktor über den die Application übergeben wird
    public MeetingViewModelFactory(@NonNull Application application){
        this.application = application;
    }


    /**
     * Diese Methode wird vom ViewModelProvider aufgerufen, wenn er ein ViewModel erstellen soll.
     *
     * @param modelClass Die Klasse des ViewModels, das erstellt werden soll (MeetingViewModel.class).
     * @return Eine neue Instanz des angeforderten ViewModels.
     */

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        // Prüfen ob MeetingViewModel angefordert wird
        if(modelClass.isAssignableFrom(MeetingViewModel.class)) {
            return (T) new MeetingViewModel(application);
        }
        return null;
    }
}
