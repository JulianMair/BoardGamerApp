package com.example.boardgamerapp.featureEvaluate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boardgamerapp.R;

public class Evaluate extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluate);

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        EditText editFeedback = findViewById(R.id.editFeedback);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String feedback = editFeedback.getText().toString().trim();

            if (feedback.isEmpty()) {
                Toast.makeText(this, "Bitte schreibe eine Bewertung.", Toast.LENGTH_SHORT).show();
                return;
            }

            // → Hier könntest du die Bewertung speichern (DB, API, SharedPreferences)
            Toast.makeText(this,
                    "Bewertung: " + rating + " Sterne\n" + feedback,
                    Toast.LENGTH_LONG).show();
        });
    }
}
