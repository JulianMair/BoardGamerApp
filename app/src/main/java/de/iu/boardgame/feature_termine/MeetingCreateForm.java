package de.iu.boardgame.feature_termine;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.helpers.TimePickerFragment;

public class MeetingCreateForm extends AppCompatActivity
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_meeting_create_form);
            Button pickTimeButton = findViewById(R.id.pickTime);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            pickTimeButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    DialogFragment timepicker = new TimePickerFragment();
                    timepicker.show(getSupportFragmentManager(), "time picker");
                }
            });
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
            Log.d("MeetingForm", "Zeit erfolgreich ausgewählt: " + selectedTime);
        }
}
