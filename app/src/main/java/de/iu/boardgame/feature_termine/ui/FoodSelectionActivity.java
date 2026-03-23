package de.iu.boardgame.feature_termine.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_food.viewmodel.FoodViewModel;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModelFactory;
import de.iu.boardgame.feature_user.helpers.SessionManager;

public class FoodSelectionActivity extends AppCompatActivity {

    private RadioGroup rgFood;
    private TextView tvOrderReminder;
    private MeetingViewModel meetingViewModel;
    private FoodViewModel foodViewModel;
    private int meetingId;
    private Meeting currentMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_activity_selection);

        meetingId = getIntent().getIntExtra("MEETING_ID", -1);
        long currentUserId = SessionManager.getCurrentUserId(this);

        rgFood = findViewById(R.id.rgFood);
        tvOrderReminder = findViewById(R.id.tvOrderReminder);
        
        findViewById(R.id.btnSaveFood).setOnClickListener(v -> saveSelection());

        // ViewModels
        MeetingViewModelFactory factory = new MeetingViewModelFactory(this.getApplication());
        meetingViewModel = new ViewModelProvider(this, factory).get(MeetingViewModel.class);
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        // Meeting Daten (für Titel/Status)
        meetingViewModel.getcurrentMeeting(meetingId).observe(this, meeting -> {
            if (meeting != null) {
                currentMeeting = meeting;
                checkOrderReminder();
            }
        });

        // Eigenen Vote laden
        foodViewModel.getVoteByUser(meetingId, currentUserId).observe(this, vote -> {
            if (vote != null) {
                preselectFood(vote.foodType);
            }
        });
    }

    private void checkOrderReminder() {
        if (currentMeeting == null) return;
        
        long currentUserId = SessionManager.getCurrentUserId(this);
        boolean isHost = (currentUserId == currentMeeting.getHost_id());
        boolean isPlanned = "planned".equals(currentMeeting.getStatus());
        
        if (isHost && isPlanned) {
            tvOrderReminder.setVisibility(View.VISIBLE);
        } else {
            tvOrderReminder.setVisibility(View.GONE);
        }
    }

    private void preselectFood(String foodType) {
        if (foodType == null) return;
        switch (foodType) {
            case "Italienisch":
                rgFood.check(R.id.rbItalian);
                break;
            case "Deutsch":
                rgFood.check(R.id.rbGerman);
                break;
            case "Griechisch":
                rgFood.check(R.id.rbGreek);
                break;
        }
    }

    private void saveSelection() {
        int selectedId = rgFood.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Bitte wähle eine Essensrichtung!", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedButton = findViewById(selectedId);
        String foodType = selectedButton.getText().toString();
        long currentUserId = SessionManager.getCurrentUserId(this);

        // In food_votes Tabelle speichern
        foodViewModel.vote(meetingId, currentUserId, foodType);
        
        // Zusätzlich im Meeting-Objekt für die Schnellansicht
        if (currentMeeting != null) {
            currentMeeting.setFoodType(foodType);
            meetingViewModel.update(currentMeeting);
        }

        Toast.makeText(this, "Essen gespeichert: " + foodType, Toast.LENGTH_SHORT).show();
        finish();
    }
}
