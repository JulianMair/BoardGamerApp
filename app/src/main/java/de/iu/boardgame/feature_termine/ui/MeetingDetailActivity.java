package de.iu.boardgame.feature_termine.ui;

import de.iu.boardgame.BaseActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_abstimmung.ui.VoteGamesActivity;
import de.iu.boardgame.feature_abstimmung.data.GameVoteInfo;
import de.iu.boardgame.feature_abstimmung.viewmodel.VotesViewModel;
import de.iu.boardgame.feature_evaluate.ui.RatingListActivity;
import de.iu.boardgame.feature_food.ui.FoodSelectionActivity;
import de.iu.boardgame.feature_food.viewmodel.FoodViewModel;
import de.iu.boardgame.feature_send_message.ui.ChatActivity;
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
public class MeetingDetailActivity extends BaseActivity {

    // UI Elemente
    private TextView tvtitle;
    private TextView tvdate;
    private TextView tvtime;
    private TextView tvlocation;
    private TextView tvhost;
    private TextView tvVoteStatus;
    private ImageButton btnBack;
    private ImageButton btnDelete;
    private MaterialButton btnFood;
    private ImageButton btnMessageHost;
    private MaterialButton btnRate;
    private SwitchMaterial switchStatus;
    private RecyclerView rvTopGames;

    // Logik
    private MeetingViewModel meetingViewModel;
    private UsersViewModel userViewMode;
    private VotesViewModel votesViewModel;
    private FoodViewModel foodViewModel;
    private TopGamesAdapter topGamesAdapter;
    private int myVoteCount = 0;
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
        tvVoteStatus = findViewById(R.id.tvVoteStatus);
        tvtitle = findViewById(R.id.tvtitle);

        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        btnFood = findViewById(R.id.btnFood);
        btnMessageHost = findViewById(R.id.btnMessageHost);
        btnRate = findViewById(R.id.btnRate);

        switchStatus = findViewById(R.id.switchStatus);
        rvTopGames = findViewById(R.id.rvGames);
        rvTopGames.setLayoutManager(new LinearLayoutManager(this));
        topGamesAdapter = new TopGamesAdapter();
        rvTopGames.setAdapter(topGamesAdapter);

        // --- VIEWMODEL INITIALISIEREN ---
        MeetingViewModelFactory factory = new MeetingViewModelFactory(this.getApplication());
        meetingViewModel = new ViewModelProvider(this, factory).get(MeetingViewModel.class);
        userViewMode = new ViewModelProvider(this).get(UsersViewModel.class);
        votesViewModel = new ViewModelProvider(this).get(VotesViewModel.class);
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        // --- DATEN EMPFANGEN ---
        // Wir holen die ID, die uns die MeetingListActivity (Adapter) mitgeschickt hat.
        // "-1" ist der Standardwert, falls keine ID gefunden wurde
        meetingId = getIntent().getIntExtra("MEETING_ID", -1);
        long currentUserId = SessionManager.getCurrentUserId(this);
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
        tvVoteStatus.setOnClickListener(v -> {
            if (meetingId <= 0) {
                Toast.makeText(this, "Meeting ungueltig", Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentUserId <= 0) {
                Toast.makeText(this, "Kein User eingeloggt", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, VoteGamesActivity.class);
            intent.putExtra(VoteGamesActivity.EXTRA_MEETING_ID, (long) meetingId);
            intent.putExtra(VoteGamesActivity.EXTRA_USER_ID, currentUserId);
            startActivity(intent);
        });

        
        updateVoteStatus(0);
        if (meetingId > 0) {
            long voteUserId = currentUserId > 0 ? currentUserId : -1L;
            votesViewModel.getMyCount(meetingId, voteUserId).observe(this, count -> {
                myVoteCount = (count == null) ? 0 : count;
                updateVoteStatus(myVoteCount);
            });
            votesViewModel.getGames(meetingId, voteUserId).observe(this, this::updateTopGames);
            
            // Food Results beobachten
            foodViewModel.getResults(meetingId).observe(this, results -> {
                if (results != null && !results.isEmpty()) {
                    btnFood.setText("Essen: " + results.get(0).foodType);
                } else {
                    btnFood.setText("Essen");
                }
            });
        }

        btnFood.setOnClickListener(v -> {
            Intent intent = new Intent(this, FoodSelectionActivity.class);
            intent.putExtra("MEETING_ID", meetingId);
            startActivity(intent);
        });

        btnMessageHost.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("meeting_id", currentMeeting.getMeeting_id());
            intent.putExtra("user", currentUser.name);
            this.startActivity(intent);
        });

        btnRate.setOnClickListener(v -> {
            Intent intent = new Intent(this, RatingListActivity.class);
            intent.putExtra("meeting_id", currentMeeting.getMeeting_id());
            this.startActivity(intent);
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

    private void updateVoteStatus(int count) {
        int capped = Math.min(count, 3);
        if (capped <= 0) {
            tvVoteStatus.setText("Spiel: Abstimmen");
        } else {
            tvVoteStatus.setText("Spiel: Abgestimmt (" + capped + "/3)");
        }
    }

    private void updateTopGames(List<GameVoteInfo> games) {
        List<GameVoteInfo> top = new ArrayList<>();
        if (games != null) {
            for (GameVoteInfo game : games) {
                if (game.voteCount <= 0) {
                    continue;
                }
                top.add(game);
                if (top.size() >= 3) {
                    break;
                }
            }
        }
        topGamesAdapter.setItems(top);
    }

    private void setVoteEnabled(boolean enabled) {
        tvVoteStatus.setEnabled(enabled);
        tvVoteStatus.setAlpha(enabled ? 1f : 0.6f);
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
            setVoteEnabled(false);
            btnFood.setVisibility(android.view.View.VISIBLE); // Auch bei closed anzeigen (aber evtl disabled?)
            btnFood.setEnabled(false);
            btnRate.setVisibility(View.VISIBLE);
        }
        else if (newStatus.equals("planned")) {
            // Wenn geplant
            switchStatus.setChecked(true); // Schalter an
            switchStatus.setText("Planung fertig");
            switchStatus.setEnabled(true);
            setVoteEnabled(false);
            btnFood.setVisibility(android.view.View.VISIBLE);
            btnFood.setEnabled(true);
            btnRate.setVisibility(View.GONE);
        }
        else {
            // Wenn open
            switchStatus.setChecked(false); // Schalter aus
            switchStatus.setText("Planung offen");
            switchStatus.setEnabled(true);
            setVoteEnabled(true);
            btnFood.setVisibility(android.view.View.VISIBLE);
            btnFood.setEnabled(true);
            btnRate.setVisibility(View.GONE);
        }

        // Nur der Host darf den Status  ändern!
        if (!isMyMeeting() && !newStatus.equals("closed")) {
            switchStatus.setEnabled(false);
        }

    }

    // Private Klasse, um die aktuell meist gewählten Spiele anzuzeigen
    private static class TopGamesAdapter extends RecyclerView.Adapter<TopGamesAdapter.ViewHolder> {
        private final List<GameVoteInfo> items = new ArrayList<>();

        void setItems(List<GameVoteInfo> newItems) {
            items.clear();
            if (newItems != null) {
                items.addAll(newItems);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            GameVoteInfo game = items.get(position);
            holder.text.setText((position + 1) + ". " + game.name);
            holder.text.setTextColor(Color.BLACK);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView text;

            ViewHolder(View itemView) {
                super(itemView);
                text = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
