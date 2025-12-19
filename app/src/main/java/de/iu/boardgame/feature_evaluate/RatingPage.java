package de.iu.boardgame.feature_evaluate;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;

import de.iu.boardgame.R;

public class RatingPage extends AppCompatActivity {
    private RatingBar ratingHost, ratingFood, ratingEvening;
    private EditText editComment;
    private Button btnSaveRating;

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

        btnSaveRating.setOnClickListener(v -> saveRating());
    }

    private void saveRating() {
        float host = ratingHost.getRating();
        float food = ratingFood.getRating();
        float evening = ratingEvening.getRating();
        String comment = editComment.getText().toString().trim();

        if (comment.isEmpty()) {
            Toast.makeText(this, "Bitte gib einen Kommentar ein.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Bewertung erstellen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            MeetingRating rating = new MeetingRating(
                    host,
                    food,
                    evening,
                    comment,
                    LocalDateTime.now()
            );
        }

        // Speichern in Datenbank (ASYNC!)

            runOnUiThread(() -> {
                Toast.makeText(this, "Bewertung gespeichert!", Toast.LENGTH_SHORT).show();
                finish(); // Activity schließen, wenn gewünscht
            });
    };
}

