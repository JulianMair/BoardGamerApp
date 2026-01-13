package de.iu.boardgame.feature_evaluate.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import de.iu.boardgame.feature_termine.data.AppDatabase;

public class RatingRepository {

    private final RatingDao ratingDao;
    private final ExecutorService executorService;
    private final MutableLiveData<List<MeetingRating>> allRatingsLiveData = new MutableLiveData<>();

    public RatingRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        ratingDao = db.ratingDao();
        executorService = Executors.newSingleThreadExecutor();
        loadAllRatings();
    }

    /** Lädt alle Ratings asynchron und setzt sie ins LiveData */
    private void loadAllRatings() {
        executorService.execute(() -> {
            List<MeetingRating> ratings = ratingDao.getAllRatings().getValue();
            allRatingsLiveData.postValue(ratings);
        });
    }

    public LiveData<List<MeetingRating>> getAllRatings() {
        return ratingDao.getAllRatings();  // Room liefert automatisch LiveData
    }

    /** Neues Rating einfügen */
    public void insert(MeetingRating rating) {
        executorService.execute(() -> {
            ratingDao.insert(rating);
            loadAllRatings(); // nach Insert neu laden
        });
    }

    /** Rating löschen */
    public void delete(MeetingRating rating) {
        executorService.execute(() -> {
            ratingDao.delete(rating);
            loadAllRatings(); // nach Delete neu laden
        });
    }
    public LiveData<List<MeetingRating>> getRatingsForMeeting(int meetingId) {
        return ratingDao.getRatingsForMeeting(meetingId);
    }

}
