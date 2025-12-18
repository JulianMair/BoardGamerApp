package de.iu.boardgame.feature_termine.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import java.text.BreakIterator;
import java.util.Objects;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModelFactory;


public class MeetingDetailActivity extends AppCompatActivity {

    private TextView tvtitle;
    private TextView tvdate;
    private TextView tvtime;
    private TextView tvlocation;
    private TextView tvhost;
    private Button btnBack;
    private Button btnDelete;
    private MeetingViewModel viewModel;
    private int meetingId;
    private LiveData<Meeting> currentMeeting;

    @ Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meeting_detail);

        tvdate = findViewById(R.id.tvdate);
        tvtime = findViewById(R.id.tvtime);
        tvlocation = findViewById(R.id.tvlocation);
        tvhost = findViewById(R.id.tvhost);
        tvtitle = findViewById(R.id.tvtitle);

        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        viewModel = new MeetingViewModel(getApplication());
        meetingId = getIntent().getIntExtra("MEETING_ID", -1);

        currentMeeting = viewModel.getcurrentMeeting(meetingId);

        btnBack.setOnClickListener(view -> {
           finish();
        });

        btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MeetingDetailActivity.this);
            builder.setTitle("Alle Achtung");
            builder.setMessage("Weg damit?");

            // Button 1 Ja -> löschen
            builder.setPositiveButton("Ja", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    viewModel.deleteById(meetingId);
                    finish();
                }
            });

            // BUTTON 2 NEIN -> Abbrechen
            builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Hier passiert nichts, Dialog schließt sich einfach
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        });

        // Läd die Daten wenn sie vorhanden sind
        viewModel.getcurrentMeeting(meetingId).observe(this, meeting -> {
            if (meeting != null){
                tvtitle.setText(meeting.getTitle());
                tvdate.setText("Datum: " + meeting.getDate());
                tvtime.setText("Uhrzeit: " + meeting.getLocation());
                // TODO: den host namen getten
                tvhost.setText("Gastgeber: " + meeting.getHost_id());
                tvlocation.setText("Ort: " + meeting.getLocation());
            }
        });

    }
}