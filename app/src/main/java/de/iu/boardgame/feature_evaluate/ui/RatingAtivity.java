package de.iu.boardgame.feature_evaluate.ui;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_evaluate.data.MeetingRating;
import de.iu.boardgame.feature_evaluate.viewmodel.RatingViewModel;
import de.iu.boardgame.feature_user.helpers.SessionManager;

public class RatingAtivity extends AppCompatActivity {

    private RatingBar ratingHost, ratingFood, ratingEvening;
    private EditText editComment;
    private Button btnSaveRating;
    private RatingViewModel viewModel;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // UI-Elemente verbinden
        ratingHost = findViewById(R.id.ratingHost);
        ratingFood = findViewById(R.id.ratingFood);
        ratingEvening = findViewById(R.id.ratingEvening);
        editComment = findViewById(R.id.editComment);
        btnSaveRating = findViewById(R.id.btnSaveRating);

        // ViewModel initialisieren
        viewModel = new ViewModelProvider(this).get(RatingViewModel.class);

        //Methode fürs speichern von Ratings
        btnSaveRating.setOnClickListener(v -> saveRating());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveRating() {
        float host = ratingHost.getRating();
        float food = ratingFood.getRating();
        float evening = ratingEvening.getRating();
        String comment = editComment.getText().toString().trim();
        long userId = SessionManager.getCurrentUserId(this);

        if (comment.isEmpty()) {
            Toast.makeText(this, "Bitte gib einen Kommentar ein.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Bewertung erstellen
        int meetingId = getIntent().getIntExtra("meeting_id", -1);

        MeetingRating rating = new MeetingRating(
                meetingId,
                userId,
                host,
                food,
                evening,
                comment,
                LocalDateTime.now()
        );

        // Speichern in der Datenbank über ViewModel (asynchron)
        viewModel.insert(rating);

        // Feedback an User
        Toast.makeText(this, "Bewertung gespeichert!", Toast.LENGTH_SHORT).show();
        finish(); // Activity schließen
    }
}
