package de.iu.boardgame.feature_termine.ui;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModelFactory;

public class FoodSelectionActivity extends AppCompatActivity {

    private RadioGroup rgFood;
    private MeetingViewModel meetingViewModel;
    private int meetingId;
    private Meeting currentMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_activity_selection);

        meetingId = getIntent().getIntExtra("MEETING_ID", -1);

        rgFood = findViewById(R.id.rgFood);
        findViewById(R.id.btnSaveFood).setOnClickListener(v -> saveSelection());

        MeetingViewModelFactory factory = new MeetingViewModelFactory(this.getApplication());
        meetingViewModel = new ViewModelProvider(this, factory).get(MeetingViewModel.class);

        meetingViewModel.getcurrentMeeting(meetingId).observe(this, meeting -> {
            if (meeting != null) {
                currentMeeting = meeting;
                preselectFood(meeting.getFoodType());
            }
        });
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

        if (currentMeeting != null) {
            currentMeeting.setFoodType(foodType);
            meetingViewModel.update(currentMeeting);
            Toast.makeText(this, "Essen gespeichert: " + foodType, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
