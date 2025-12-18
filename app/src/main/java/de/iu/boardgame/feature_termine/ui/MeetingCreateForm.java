package de.iu.boardgame.feature_termine.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.helpers.DatePickerFragment;
import de.iu.boardgame.feature_termine.helpers.TimePickerFragment;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModelFactory;

import de.iu.boardgame.feature_termine.helpers.DatePickerFragment;
import de.iu.boardgame.feature_termine.helpers.TimePickerFragment;

public class MeetingCreateForm extends AppCompatActivity
            implements DatePickerFragment.DatePickerListener,
            TimePickerFragment.TimePickerListener {

    DialogFragment datepicker;
    DialogFragment timepicker;
    String date_str = "";
    String time_str = "";
    MaterialButton btnsave;
    Button btndate;
    MaterialButton btncancle;
    TextView tvDateRes;
    TextView tvTitle;
    TextView tvLocation;
    MeetingViewModel meetingViewModel;
    int duration = Toast.LENGTH_SHORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meeting_create_form);


        btnsave = findViewById(R.id.btnSave);
        btndate = findViewById(R.id.btnSelect);
        btncancle = findViewById(R.id.btnCancel);
        tvDateRes = findViewById(R.id.tvDateResult);
        tvTitle = findViewById(R.id.tvtitle);
        tvLocation = findViewById(R.id.tvLocation);
        MeetingViewModelFactory factory = new MeetingViewModelFactory(this.getApplication());
        meetingViewModel = new ViewModelProvider(this, factory).get(MeetingViewModel.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Öffnen des Datums auswahl Dialogs
        btndate.setOnClickListener(view -> {
            DatePickerFragment dateFragment = new DatePickerFragment();

            dateFragment.show(getSupportFragmentManager(), "datePicker");
        });

        // TODO: Host_id muss vom User kommen dh DUMMY
        // TODO: Status = dummy
        btnsave.setOnClickListener(view -> {
            Log.d("FEHLER", "1");
            Meeting meeting = new Meeting(tvTitle.getText().toString(),
                                          date_str,
                                          time_str,
                                          tvLocation.getText().toString(),
                                          0,
                                          "dummy");

            Log.d("FEHLER", "2");
            meetingViewModel.insert(meeting);
            Log.d("FEHLER", "3");

            // Feedback anzeigen
            Toast.makeText(MeetingCreateForm.this, "Dein Spieleabend wurde erstellt.", Toast.LENGTH_SHORT).show();

            //Activity beenden
            finish();
        });

        btncancle.setOnClickListener(view -> {
           finish();
        });
    }


    public void create_Time_Dialog() {
        //Doppelklick verhinden
        if (getSupportFragmentManager().findFragmentByTag("timePicker") != null) {
            return;
        }

        TimePickerFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }

    String create_date_str(int year, int month, int day){
        return String.format("%02d/%02d/%04d", day, month, year);
    }

    String create_time_str(int hour, int min){
        return String.valueOf(hour) + ":" + String.valueOf(min);
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        android.util.Log.d("DEBUG_DATE", "Activity: onDateSelected erreicht! Datum: " + day + "." + month);
        this.date_str = create_date_str(year, month + 1, day);
        create_Time_Dialog();
    }

    @Override
    public void onTimeSelected(int hour, int minute) {
        time_str = create_time_str(hour, minute);
        String tvShowString = "am: " + date_str + "\num: " + time_str + " Uhr";
        tvDateRes.setText(tvShowString);
    }
}
