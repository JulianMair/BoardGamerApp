package de.iu.boardgame.feature_termine.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.helpers.DatePickerFragment;
import de.iu.boardgame.feature_termine.helpers.TimePickerFragment;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModelFactory;

/**
 * Activity zum Erstellen eines neuen Termins.
 * * WICHTIG: Diese Klasse implementiert zwei Interfaces (DatePickerListener, TimePickerListener).
 * Das bedeutet, diese Activity verspricht, die Methoden 'onDateSelected' und 'onTimeSelected'
 * bereitzustellen, damit die Dialog-Fragmente ihre Daten hierher zurückschicken können.
 */
public class MeetingCreateForm extends AppCompatActivity
            implements DatePickerFragment.DatePickerListener,
            TimePickerFragment.TimePickerListener {

    // UI-Elemente
    DialogFragment datepicker;
    DialogFragment timepicker;
    long timestamp;
    MaterialButton btnsave;
    Button btndate;
    MaterialButton btncancle;
    TextView tvDateRes;
    TextView tvTitle;
    TextView tvLocation;

    // Logik-Elemente
    MeetingViewModel meetingViewModel;
    int duration = Toast.LENGTH_SHORT;

    // Temporäre Speicher für das Datum, bevor wir speichern
    private int year, month, day;
    private int hour, minute;
    Calendar c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Aktiviert das moderne "Edge-to-Edge" Design (Inhalte gehen bis hinter die Statusleiste)
        EdgeToEdge.enable(this);
        setContentView(R.layout.termine_activity_meeting_create_form);

        // Views verbinden
        btnsave = findViewById(R.id.btnSave);
        btndate = findViewById(R.id.btnSelect);
        btncancle = findViewById(R.id.btnCancel);
        tvDateRes = findViewById(R.id.tvDateResult);
        tvTitle = findViewById(R.id.tvtitle);
        tvLocation = findViewById(R.id.tvLocation);

        // --- VIEWMODEL SETUP ---
        // Wir brauchen eine Factory, weil unser ViewModel Parameter im Konstruktor benötigt (Application).
        MeetingViewModelFactory factory = new MeetingViewModelFactory(this.getApplication());

        // Der ViewModelProvider sorgt dafür, dass wir das existierende ViewModel bekommen (oder ein neues erstellt wird)
        meetingViewModel = new ViewModelProvider(this, factory).get(MeetingViewModel.class);

        // Kalender initialisieren (Standard: Aktuelle Zeit)
        c = Calendar.getInstance();

        // System-Bars (Statusleiste/Navi) Padding setzen, damit nichts überdeckt wird
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. SCHRITT: Datum auswählen
        btndate.setOnClickListener(view -> {
            DatePickerFragment dateFragment = new DatePickerFragment();

            dateFragment.show(getSupportFragmentManager(), "datePicker");
        });

        // 3. SCHRITT: Speichern
        // TODO: Host_id muss vom User kommen (Login-System noch nicht fertig), daher 0
        // TODO: Status ist vorerst fest auf "dummy"
        btnsave.setOnClickListener(view -> {
            // Neues Meeting-Objekt erstellen
            Meeting meeting = new Meeting(tvTitle.getText().toString(),
                                          c.getTimeInMillis(),
                                          tvLocation.getText().toString(),
                                          0,
                                          "dummy");

            // An das ViewModel übergeben (Das kümmert sich um DB-Speicherung im Hintergrund)
            meetingViewModel.insert(meeting);

            // Feedback an den User
            Toast.makeText(MeetingCreateForm.this, "Dein Spieleabend wurde erstellt.", Toast.LENGTH_SHORT).show();

            // Activity schließen und zurück zur Liste
            finish();
        });

        // Abbrechen Button
        btncancle.setOnClickListener(view -> {
           finish();        // Schließt einfach das Fenster ohne Speichern
        });
    }


    /**
     * Hilfsmethode, um den TimePicker zu öffnen.
     * Wird automatisch aufgerufen, sobald ein Datum gewählt wurde (für besseren Flow).
     */
    public void create_Time_Dialog() {
        // Doppelklick verhindern: Prüfen, ob der Dialog schon offen ist
        if (getSupportFragmentManager().findFragmentByTag("timePicker") != null) {
            return;
        }

        TimePickerFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }

    // --- CALLBACKS VOM DATEPICKER ---

    @Override
    public void onDateSelected(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        // Workflow: Sobald das Datum gewählt ist, öffnen wir sofort die Uhrzeit-Auswahl
        create_Time_Dialog();
    }

    // --- CALLBACKS VOM TIMEPICKER ---

    @Override
    public void onTimeSelected(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;

        // Jetzt haben wir alles (Datum + Zeit). Wir aktualisieren das Calendar-Objekt.
        // WICHTIG: Das ist nötig, um später c.getTimeInMillis() für die DB zu bekommen.
        c.set(year, month, day, hour, minute);

        // UI Update: Dem User zeigen, was er gewählt hat.
        // ACHTUNG BEIM MONAT: Java Calendar zählt Monate von 0 (Jan) bis 11 (Dez).
        // Für die Anzeige müssen wir also (month + 1) rechnen!
        tvDateRes.setText("am: " + day + "-" + month+1 + "-" + year + "\num: " + hour + ":" + minute);
    }
}
