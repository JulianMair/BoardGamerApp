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
import de.iu.boardgame.feature_abstimmung.ui.VoteGamesActivity;
import de.iu.boardgame.feature_abstimmung.viewmodel.VotesViewModel;
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
    private TextView tvTopGameValue;
    private ImageButton btnBack;
    private ImageButton btnDelete;
    private MaterialButton btnFood;
    private MaterialButton btnVoteGames;
    private SwitchMaterial switchStatus;

    // Logik
    private MeetingViewModel meetingViewModel;
    private UsersViewModel userViewMode;
    private VotesViewModel votesViewModel;
    private int meetingId;
    //private LiveData<Meeting> currentMeeting;
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
        tvTopGameValue = findViewById(R.id.tvTopGameValue);

        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        btnFood = findViewById(R.id.btnFood);
        btnVoteGames = findViewById(R.id.btnVoteGames);

        switchStatus = findViewById(R.id.switchStatus);

        // --- VIEWMODEL INITIALISIEREN ---
        MeetingViewModelFactory factory = new MeetingViewModelFactory(this.getApplication());
        meetingViewModel = new ViewModelProvider(this, factory).get(MeetingViewModel.class);
        userViewMode = new ViewModelProvider(this).get(UsersViewModel.class);
        votesViewModel = new ViewModelProvider(this).get(VotesViewModel.class);

        // --- DATEN EMPFANGEN ---
        // Wir holen die ID, die uns die MeetingListActivity (Adapter) mitgeschickt hat.
        // "-1" ist der Standardwert, falls irgendwas schiefgelaufen ist (keine ID gefunden).
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
                        // Hier passiert nichts, Dialog schließt sich einfach
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
        // läuft dieser Codeblock (Lambda) ab.
        meetingViewModel.getcurrentMeeting(meetingId).observe(this, meeting -> {
            // WICHTIG: Prüfung auf null.
            // Wenn wir das Meeting gerade gelöscht haben, feuert LiveData evtl. nochmal 'null'.
            // Ohne das 'if' würde die App hier abstürzen.
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

        btnFood.setOnClickListener(v -> {
            // TODO implement
            Toast.makeText(this, "Essens-Planung kommt bald!", Toast.LENGTH_SHORT).show();
            // Später: Intent zur Essens-Activity
        });

        btnVoteGames.setOnClickListener(v -> {
            if (meetingId <= 0) {
                return;
            }
            long userId = SessionManager.getCurrentUserId(MeetingDetailActivity.this);
            if (userId <= 0) {
                Toast.makeText(this, "Kein User eingeloggt", Toast.LENGTH_SHORT).show();
                return;
            }
            android.content.Intent intent = new android.content.Intent(this, VoteGamesActivity.class);
            intent.putExtra(VoteGamesActivity.EXTRA_MEETING_ID, (long) meetingId);
            intent.putExtra(VoteGamesActivity.EXTRA_USER_ID, userId);
            startActivity(intent);
        });

        votesViewModel.getTopVotedGame(meetingId).observe(this, game -> {
            if (game == null || game.voteCount <= 0) {
                tvTopGameValue.setText("-");
                return;
            }
            tvTopGameValue.setText(game.name);
        });

    }

    private boolean isMyMeeting() {
        // Überprüft ob das Meeting dem aktuell Eingelogten User gehört
        return SessionManager.getCurrentUserId(MeetingDetailActivity.this) == currentMeeting.getHost_id();
    }

    private void fillTextViews(){
        runOnUiThread(() -> {
            tvtitle.setText(currentMeeting.getTitle());

            // Hier nutzen wir unsere schönen Formatier-Methoden aus der Meeting-Klasse
            tvdate.setText("Datum: " + currentMeeting.getFormatedDate());
            tvtime.setText("Uhrzeit: " + currentMeeting.getFormatedTime());
            tvhost.setText("Gastgeber: " + currentUser.name);
            tvlocation.setText("Ort: " + currentMeeting.getLocation());
        });
    }

    private void meetingStatusUpdate(String newStatus){

        long now = System.currentTimeMillis();

        if(currentMeeting.getTimestmap() < now && !currentMeeting.getStatus().equals("closed")){
            // Automatisch schließen!
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
            btnFood.setVisibility(android.view.View.GONE);
            btnVoteGames.setEnabled(false);
        }
        else if (newStatus.equals("planned")) {
            // Wenn geplant
            switchStatus.setChecked(true); // Schalter an
            switchStatus.setText("Planung fertig");
            switchStatus.setEnabled(true);
            btnFood.setVisibility(android.view.View.GONE);
            btnVoteGames.setEnabled(true);
        }
        else {
            // Wenn open
            switchStatus.setChecked(false); // Schalter aus
            switchStatus.setText("Planung offen");
            switchStatus.setEnabled(true);
            btnFood.setVisibility(android.view.View.VISIBLE);
            btnVoteGames.setEnabled(true);
        }

        // Nur der Host darf den Status  ändern!
        if (!isMyMeeting() && !newStatus.equals("closed")) {
            switchStatus.setEnabled(false);
            btnFood.setVisibility(android.view.View.GONE);
        }
    }

}
