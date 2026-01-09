package de.iu.boardgame.feature_termine.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_abstimmung.ui.VoteGamesActivity;
import de.iu.boardgame.feature_abstimmung.viewmodel.VotesViewModel;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;
import de.iu.boardgame.feature_user.helpers.SessionManager;

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
    private TextView tvTopGame;
    private Button btnBack;
    private Button btnDelete;
    private Button btnVoteGames;

    // Logik
    private MeetingViewModel viewModel;
    private VotesViewModel votesViewModel;
    private int meetingId;
    private LiveData<Meeting> currentMeeting;

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
        tvTopGame = findViewById(R.id.tvTopGame);

        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        btnVoteGames = findViewById(R.id.btnVoteGames);

        // --- VIEWMODEL INITIALISIEREN ---
        // TODO: Viewmodel darf nicht mit new erstellt werden!!!
        //        sMeetingViewModelFactory factory = new MeetingViewModelFactory(this.getApplication());
        //        viewModel = new ViewModelProvider(this, factory).get(MeetingViewModel.class);
        viewModel = new MeetingViewModel(getApplication());
        votesViewModel = new ViewModelProvider(this).get(VotesViewModel.class);

        // --- DATEN EMPFANGEN ---
        // Wir holen die ID, die uns die MeetingListActivity (Adapter) mitgeschickt hat.
        // "-1" ist der Standardwert, falls irgendwas schiefgelaufen ist (keine ID gefunden).
        meetingId = getIntent().getIntExtra("MEETING_ID", -1);


        currentMeeting = viewModel.getcurrentMeeting(meetingId);

        // Zurück Button
        btnBack.setOnClickListener(view -> {
           finish();
        });

        btnVoteGames.setOnClickListener(view -> {
            long userId = SessionManager.getCurrentUserId(this);
            if (userId <= 0) {
                Toast.makeText(this, "Kein User eingeloggt", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MeetingDetailActivity.this, VoteGamesActivity.class);
            intent.putExtra(VoteGamesActivity.EXTRA_MEETING_ID, (long) meetingId);
            intent.putExtra(VoteGamesActivity.EXTRA_USER_ID, userId);
            startActivity(intent);
        });

        // Löschen Button: Sicherheits Dialog
        btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MeetingDetailActivity.this);
            builder.setTitle("Alle Achtung");
            builder.setMessage("Weg damit?");

            // Button 1 Ja -> löschen
            builder.setPositiveButton("Ja", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    // Befehl ans ViewModel: "Lösch das Ding aus der Datenbank"
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

            // Dialog anzeigen
            AlertDialog dialog = builder.create();
            dialog.show();

        });

        // --- BEOBACHTEN (OBSERVER) ---
        // Sobald die Datenbank die Daten geladen hat (oder sie sich ändern),
        // läuft dieser Codeblock (Lambda) ab.
        viewModel.getcurrentMeeting(meetingId).observe(this, meeting -> {
            // WICHTIG: Prüfung auf null.
            // Wenn wir das Meeting gerade gelöscht haben, feuert LiveData evtl. nochmal 'null'.
            // Ohne das 'if' würde die App hier abstürzen.
            if (meeting != null){
                tvtitle.setText(meeting.getTitle());

                // Hier nutzen wir unsere schönen Formatier-Methoden aus der Meeting-Klasse
                tvdate.setText("Datum: " + meeting.getFormatedDate());
                tvtime.setText("Uhrzeit: " + meeting.getFormatedTime());
                // TODO: den host namen getten
                tvhost.setText("Gastgeber: " + meeting.getHost_id());
                tvlocation.setText("Ort: " + meeting.getLocation());
            }
        });

        votesViewModel.getTopVotedGame(meetingId).observe(this, game -> {
            if (game == null) {
                tvTopGame.setText("Noch keine Spiele");
                return;
            }

            if (game.voteCount <= 0) {
                tvTopGame.setText("Noch keine Stimmen");
            } else {
                tvTopGame.setText(game.name + " (" + game.voteCount + " Stimmen)");
            }
        });
    }
}
