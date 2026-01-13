package de.iu.boardgame.feature_termine.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModelFactory;
import de.iu.boardgame.feature_user.data.User;
import de.iu.boardgame.feature_user.helpers.SessionManager;
import de.iu.boardgame.feature_user.viewmodel.UsersViewModel;

/**
 * Diese Activity zeigt die Details eines einzelnen Termins an.
 * Sie wird geöffnet, wenn man in der Liste auf ein Element klickt.
 * Die ID des angeklickten Elements wird per Intent übergeben.
 */
public class MeetingDetailActivity extends AppCompatActivity {

    // UI Elemente
    private TextView tvtitle;
    private TextView tvdate;
    private TextView tvtime;
    private TextView tvlocation;
    private TextView tvhost;
    private ImageButton btnBack;
    private ImageButton btnDelete;
    private MaterialButton btnFood;
    private Button btnAddGame;
    private SwitchMaterial switchStatus;
    private ImageButton btnMessageHost;
    private MaterialButton btnRate;

    // Logik
    private MeetingViewModel meetingViewModel;
    private UsersViewModel userViewMode;
    private int meetingId;
    private Meeting currentMeeting;
    private User currentUser;

    @ Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.termine_activity_meeting_detail);

        // Views verknüpfen
        tvdate = findViewById(R.id.tvdate);
        tvtime = findViewById(R.id.tvtime);
        tvlocation = findViewById(R.id.tvlocation);
        tvhost = findViewById(R.id.tvhost);
        tvtitle = findViewById(R.id.tvtitle);

        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        btnAddGame = findViewById(R.id.btnAddGame);
        btnFood = findViewById(R.id.btnFood);
        btnMessageHost = findViewById(R.id.btnMessageHost);
        btnRate = findViewById(R.id.btnRate);

        switchStatus = findViewById(R.id.switchStatus);

        // --- VIEWMODEL INITIALISIEREN ---
        MeetingViewModelFactory factory = new MeetingViewModelFactory(this.getApplication());
        meetingViewModel = new ViewModelProvider(this, factory).get(MeetingViewModel.class);
        userViewMode = new ViewModelProvider(this).get(UsersViewModel.class);

        // --- DATEN EMPFANGEN ---
        // Wir holen die ID, die uns die MeetingListActivity (Adapter) mitgeschickt hat.
        // "-1" ist der Standardwert, falls keine ID gefunden wurde
        meetingId = getIntent().getIntExtra("MEETING_ID", -1);

        // Zurück Button
        btnBack.setOnClickListener(view -> {
           finish();
        });

        // Löschen Button: Sicherheits Dialog
        btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MeetingDetailActivity.this);
            if(isMyMeeting()) {
                builder.setTitle("Alle Achtung");
                builder.setMessage("Weg damit?");

                // Button 1 Ja -> löschen
                builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Befehl ans ViewModel: "Lösch das Ding aus der Datenbank"
                        meetingViewModel.deleteById(meetingId);
                        finish();
                    }
                });

                // BUTTON 2 NEIN -> Abbrechen
                builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Hier passiert nichts, Dialog schließt sich.
                        dialog.dismiss();
                    }
                });

                // Dialog anzeigen
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                builder.setTitle("Zugriff verweigert");
                builder.setMessage("Dieser Termin kann nur vom Gastgeber gelöscht werden.");
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });

        // --- BEOBACHTEN (OBSERVER) ---
        // Sobald die Datenbank die Daten geladen hat (oder sie sich ändern),
        meetingViewModel.getcurrentMeeting(meetingId).observe(this, meeting -> {
            // WICHTIG: Prüfung auf null.
            if (meeting != null){
                currentMeeting = meeting;
                userViewMode.getUserByIdOneShot(currentMeeting.getHost_id(), user -> {
                    if (user != null) {
                        currentUser = user;
                        fillTextViews();
                    }
                });
                meetingStatusUpdate(currentMeeting.getStatus());
            }
        });

        switchStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(currentMeeting == null) {return;}
            if(!buttonView.isPressed()) {return;}

            // Wenn der User klickt, ändern wir den Status
            // isChecked = true -> "planned"
            // isChecked = false -> "open"
            String newStatus = isChecked ? "planned" : "open";

            // Update nur bei ändeurngen
            if(!currentMeeting.getStatus().equals(newStatus)){
                currentMeeting.setStatus(newStatus);
                meetingViewModel.update(currentMeeting);
                meetingStatusUpdate(newStatus);
            }
        });

        btnAddGame.setOnClickListener(v -> {
            // TODO: Hier später Intent zur Spiele-Auswahl
            Toast.makeText(this, "Hier öffnet sich bald die Spiele-Suche!", Toast.LENGTH_SHORT).show();

            // TODO: Intent intent = new Intent(this, GameSelectActivity.class);
            // startActivity(intent);
        });

        btnFood.setOnClickListener(v -> {
            // TODO implement
            Toast.makeText(this, "Essens-Planung kommt bald!", Toast.LENGTH_SHORT).show();
            // Später: Intent zur Essens-Activity
        });

        // Listener für Nachricht
        btnMessageHost.setOnClickListener(v -> {
            Toast.makeText(this, "Nachricht an den Host", Toast.LENGTH_SHORT).show();
            // TODO: Nachrichten...
        });

        // Listener für Bewerten
        btnRate.setOnClickListener(v -> {
            Toast.makeText(this, "Bewertung öffnet sich...", Toast.LENGTH_SHORT).show();
            // TODO: Intent zur EvaluationActivity starten
        });

    }

    private boolean isMyMeeting() {
        // Überprüft ob das Meeting dem aktuell Eingelogten User gehört
        return SessionManager.getCurrentUserId(MeetingDetailActivity.this) == currentMeeting.getHost_id();
    }

    private void fillTextViews(){
        runOnUiThread(() -> {
            tvtitle.setText(currentMeeting.getTitle());

            tvdate.setText("Datum: " + currentMeeting.getFormatedDate());
            tvtime.setText("Uhrzeit: " + currentMeeting.getFormatedTime());
            tvhost.setText("Gastgeber: " + currentUser.name);
            tvlocation.setText("Ort: " + currentMeeting.getLocation());
        });
    }

    private void meetingStatusUpdate(String newStatus){

        long now = System.currentTimeMillis();

        if(currentMeeting.getTimestmap() < now && !currentMeeting.getStatus().equals("closed")){
            // Automatisch schließen.
            currentMeeting.setStatus("closed");
            meetingViewModel.update(currentMeeting);

            Toast.makeText(this, "Termin ist vorbei -> Archiviert", Toast.LENGTH_SHORT).show();
        }

        // --- UI Status ---
        if (newStatus.equals("closed")) {
            // Wenn vorbei: Alles sperren
            switchStatus.setChecked(true);
            switchStatus.setText("Abgeschlossen");
            switchStatus.setEnabled(false); // Kann nicht mehr geändert werden
            btnAddGame.setVisibility(android.view.View.GONE); // Keine Spiele mehr hinzufügen
            btnFood.setVisibility(android.view.View.GONE);
            btnRate.setVisibility(View.VISIBLE);
        }
        else if (newStatus.equals("planned")) {
            // Wenn geplant
            switchStatus.setChecked(true); // Schalter an
            switchStatus.setText("Planung fertig");
            switchStatus.setEnabled(true);
            btnAddGame.setVisibility(android.view.View.GONE);
            btnFood.setVisibility(android.view.View.GONE);
            btnRate.setVisibility(View.GONE);
        }
        else {
            // Wenn open
            switchStatus.setChecked(false); // Schalter aus
            switchStatus.setText("Planung offen");
            switchStatus.setEnabled(true);
            btnAddGame.setVisibility(android.view.View.VISIBLE);
            btnFood.setVisibility(android.view.View.VISIBLE);
        }

        // Nur der Host darf den Status  ändern!
        if (!isMyMeeting() && !newStatus.equals("closed")) {
            switchStatus.setEnabled(false);
            btnAddGame.setVisibility(android.view.View.GONE); // GÄSTE SPIELE ADDEn
            btnFood.setVisibility(android.view.View.GONE);
        }

        if(isMyMeeting()){
            //btnMessageHost.setVisibility(View.GONE);
        }
    }

}