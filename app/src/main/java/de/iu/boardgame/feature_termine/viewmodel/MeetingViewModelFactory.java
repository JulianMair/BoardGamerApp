package de.iu.boardgame.feature_termine.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Der 'ViewModelProvider' von Android kann standardmäßig nur ViewModels erstellen,
 * die KEINE Parameter im Konstruktor haben (leerer Konstruktor).
 *
 * Da 'MeetingViewModel' aber die 'Application' braucht (für die Datenbank),
 * Dient diese Factory als Bauanleitung für Android.
 */
public class MeetingViewModelFactory implements ViewModelProvider.Factory {

    // Zwischenspeierung der Application
    private final Application application;

    /**
     * Konstruktor der Factory.
     * Hier muss die Application EINMALIG von der Activity übergeben werden
     */
    public MeetingViewModelFactory(@NonNull Application application){
        this.application = application;
    }


    /**
     * Diese Methode wird intern vom ViewModelProvider aufgerufen
     *
     * @param modelClass Die Klasse, die erstellt werden soll.
     * @return Das fertig gebaute ViewModel mit allen Abhängigkeiten.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        // Sicherheitscheck
        if(modelClass.isAssignableFrom(MeetingViewModel.class)) {
            // manueler Konstruktor aufruf
            // weitergabe der Application
            // (T) ist ein Cast, damit Java weiß, dass es das richtige Rückgabeformat ist.
            return (T) new MeetingViewModel(application);
        }
        return null;
    }
}
