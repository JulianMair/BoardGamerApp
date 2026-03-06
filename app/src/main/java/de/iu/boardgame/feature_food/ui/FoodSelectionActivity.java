package de.iu.boardgame.feature_food.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_food.data.FoodVoteResult;
import de.iu.boardgame.feature_food.viewmodel.FoodViewModel;
import de.iu.boardgame.feature_user.helpers.SessionManager;

public class FoodSelectionActivity extends AppCompatActivity {

    private RadioGroup rgFood;
    private TextView tvResults;
    private FoodViewModel foodViewModel;
    private int meetingId;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_activity_selection);

        meetingId = getIntent().getIntExtra("MEETING_ID", -1);
        userId = SessionManager.getCurrentUserId(this);

        rgFood = findViewById(R.id.rgFood);
        tvResults = new TextView(this); // Optional: Ergebnisse anzeigen
        // Da das Layout keinen tvResults hat, füge ich hier eine einfache Logik ein, 
        // um den Speicherbutton auszublenden und Klicks direkt zu verarbeiten.
        
        View btnSave = findViewById(R.id.btnSaveFood);
        if (btnSave != null) btnSave.setVisibility(View.GONE);

        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        // Klick auf RadioButton löst sofort Speichern aus
        rgFood.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = findViewById(checkedId);
            if (rb != null && rb.isPressed()) {
                String foodType = rb.getText().toString();
                foodViewModel.vote(meetingId, userId, foodType);
                Toast.makeText(this, "Stimme für " + foodType + " abgegeben", Toast.LENGTH_SHORT).show();
                finish(); // Sofort zurück nach der Wahl
            }
        });

        // Aktuelle Wahl des Users vorselektieren
        foodViewModel.getVoteByUser(meetingId, userId).observe(this, vote -> {
            if (vote != null) {
                if (vote.foodType.equals("Italienisch")) rgFood.check(R.id.rbItalian);
                else if (vote.foodType.equals("Deutsch")) rgFood.check(R.id.rbGerman);
                else if (vote.foodType.equals("Griechisch")) rgFood.check(R.id.rbGreek);
            }
        });
    }
}
