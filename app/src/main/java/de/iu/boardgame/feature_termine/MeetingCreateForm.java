package de.iu.boardgame.feature_termine;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.helpers.DatePickerFragment;
import de.iu.boardgame.feature_termine.helpers.TimePickerFragment;

public class MeetingCreateForm extends AppCompatActivity
            implements DatePickerDialog.OnDateSetListener,
                       TimePickerDialog.OnTimeSetListener {

    DialogFragment datepicker;
    DialogFragment timepicker;
    String date_str = "";
    String time_str = "";
    Button btnsave;
    Button btndate;
    Button btnCreateAppointment;
    TextView tvDateRes;
    TextView tvTitle;
    TextView tvLocation;
    MeetingViewModel meetingViewModel;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_meeting_create_form);

            btnsave = findViewById(R.id.btnSave);
            btndate = findViewById(R.id.btnSelect);
            btnCreateAppointment = findViewById(R.id.btnSave);
            tvDateRes = findViewById(R.id.tvDateResult);
            tvTitle = findViewById(R.id.tvtitle);
            tvLocation = findViewById(R.id.tvLocation);

            meetingViewModel = new MeetingViewModel(getApplication());

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

            btnCreateAppointment.setOnClickListener(view -> {

            });
            // TODO: Host_id muss vom User kommen dh DUMMY
            // TODO: Status = dummy
            btnsave.setOnClickListener(view -> {
                Meeting meeting = new Meeting(tvTitle.getText().toString(),
                                              date_str,
                                              time_str,
                                              tvLocation.getText().toString(),
                                              1,
                                              "dummy");

                meetingViewModel.insert(meeting);
                //finish();
            });
        }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.date_str = create_date_str(year, month, dayOfMonth);
        create_Time_Dialog();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time_str = create_time_str(hourOfDay, minute);
        String tvShowString = "am: " + date_str + "\num: " + time_str + " Uhr";
        tvDateRes.setText(tvShowString);

    }

    public void create_Time_Dialog() {
        TimePickerFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }

    String create_date_str(int year, int month, int day){
        return String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
    }

    String create_time_str(int hour, int min){
        return String.valueOf(hour) + ":" + String.valueOf(min);
    }
}
