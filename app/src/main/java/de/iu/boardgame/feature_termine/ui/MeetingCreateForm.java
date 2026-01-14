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
import de.iu.boardgame.feature_user.helpers.SessionManager;


/**
 * Activity zum Erstellen eines neuen Termins.
 * WICHTIG: Diese Klasse implementiert zwei Interfaces (DatePickerListener, TimePickerListener).
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
    SessionManager usersession;
    int duration = Toast.LENGTH_SHORT;

    private int year, month, day;
    private int hour, minute;
    Calendar c = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.termine_activity_meeting_create_form);

        // Views verbinden
        btnsave = findViewById(R.id.btnSave);
        btndate = findViewById(R.id.pickTime);
        btncancle = findViewById(R.id.btnCancel);
        tvDateRes = findViewById(R.id.tvDateResult);
        tvTitle = findViewById(R.id.tvtitle);
        tvLocation = findViewById(R.id.tvLocation);

        // --- VIEWMODEL SETUP ---
        // Initialisierung des ViewModels über Factory (für Application Dependency)
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

        // Datum auswählen
        btndate.setOnClickListener(view -> {
            DatePickerFragment dateFragment = new DatePickerFragment();

            dateFragment.show(getSupportFragmentManager(), "datePicker");
        });

        // Speichern
        // TODO: Host_id muss vom User kommen (Login-System noch nicht fertig), daher 0
        btnsave.setOnClickListener(view -> {

            if(!validateInput()){
                return;
            }
            // Neues Meeting-Objekt erstellen
            Meeting meeting = createMeeting();

            // An das ViewModel übergeben (Das kümmert sich um DB-Speicherung im Hintergrund)
            meetingViewModel.insert(meeting);

            // Feedback an den User
            Toast.makeText(MeetingCreateForm.this, "Dein Spieleabend wurde erstellt.", Toast.LENGTH_SHORT).show();

            // Activity schließen und zurück zur Liste
            finish();
        });

        // Abbrechen Button
        btncancle.setOnClickListener(view -> {
           finish();        // Schließt das Fenster ohne Speichern
        });
    }

    private Meeting createMeeting() {
        long currentUserId = SessionManager.getCurrentUserId(MeetingCreateForm.this);

        Meeting meeting;
        if (currentUserId == -1) {
            Toast.makeText(this, "Fehler: Kein User eingeloggt!", Toast.LENGTH_SHORT).show();
            return null; // Abbrechen
        } else {
            meeting = new Meeting(tvTitle.getText().toString(),
                    c.getTimeInMillis(),
                    tvLocation.getText().toString(),
                    currentUserId,
                    "open");
        }
        return meeting;
    }


    /**
     * Hilfsmethode, um den TimePicker zu öffnen.
     * Wird automatisch aufgerufen, sobald ein Datum gewählt wurde.
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

        // Sobald das Datum gewählt ist, öffnen sich die Uhrzeit-Dialog
        create_Time_Dialog();
    }

    // --- CALLBACKS VOM TIMEPICKER ---

    @Override
    public void onTimeSelected(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;

        // Aktualisieren des Calendar-Objekts.
        // WICHTIG: Das ist nötig, um später c.getTimeInMillis() für die DB zu bekommen.
        c.set(year, month, day, hour, minute);

        // UI Update: Dem User zeigen, was er gewählt hat.
        // ACHTUNG BEIM MONAT: Java Calendar zählt Monate von 0 (Jan) bis 11 (Dez).
        tvDateRes.setText(String.format("am: %02d-%02d-%d\num: %02d:%02d", day, (month+1), year, hour, minute));
    }

    /**
     * Prüft alle Eingabefelder.
     * Setzt Fehlertexte an den Feldern, falls nötig.
     * @return true, wenn alles valide ist.
     */
    private boolean validateInput(){
        boolean isValid = true;

        // 1. Text auslesen und Leerzeichen am Rand entfernen
        String title = tvTitle.getText().toString().trim();
        String location = tvLocation.getText().toString().trim();
        String datetime = tvDateRes.getText().toString();

        // 2. Titel prüfen
        if(title.isEmpty()){
            tvTitle.setError("Titel darf nicht leer sein");
            tvTitle.requestFocus(); // Springt in das Feld
            isValid = false;
        }
        // Mindest länge
        else if (title.length() < 3) {
            tvTitle.setError("Titel muss mind. 3 Zeichen haben");
            isValid = false;
        }

        // 3. Adresse prüfen
        if(location.isEmpty()){
            tvLocation.setError("Bitte eine Adresse eingeben");
            if(isValid) {
                tvLocation.requestFocus(); // Springt in das Feld
            }
            isValid = false;
        }
        return isValid;
    }
}
