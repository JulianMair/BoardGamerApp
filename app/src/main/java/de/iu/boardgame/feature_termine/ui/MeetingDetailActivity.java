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
import de.iu.boardgame.feature_food.data.FoodVoteResult;
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
    private TextView tvTotalVotes;
    private TextView tvFoodStatus;
    private TextView tvTotalFoodVotes;
    private TextView tvOrderNotification;
    private ImageButton btnBack;
    private ImageButton btnDelete;
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
    private int foodVoteCount = 0;
    private int meetingId;
    private Meeting currentMeeting;
    private User currentUser;
    private int totalUsers = 0;
    private String mostPopularFoodType = "";
    private boolean hasIVotedForFood = false;

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
        tvTotalVotes = findViewById(R.id.tvTotalVotes);
        tvFoodStatus = findViewById(R.id.tvFoodStatus);
        tvTotalFoodVotes = findViewById(R.id.tvTotalFoodVotes);
        tvtitle = findViewById(R.id.tvtitle);
        tvOrderNotification = findViewById(R.id.tvOrderNotification);

        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
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
        meetingId = getIntent().getIntExtra("MEETING_ID", -1);
        long currentUserId = SessionManager.getCurrentUserId(this);

        // Zurück Button
        btnBack.setOnClickListener(view -> {
           finish();
        });

        // Löschen Button
        btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MeetingDetailActivity.this);
            if(isMyMeeting()) {
                builder.setTitle("Alle Achtung");
                builder.setMessage("Weg damit?");
                builder.setPositiveButton("Ja", (dialog, which) -> {
                    meetingViewModel.deleteById(meetingId);
                    finish();
                });
                builder.setNegativeButton("Abbrechen", (dialog, which) -> dialog.dismiss());
                builder.create().show();
            } else {
                builder.setTitle("Zugriff verweigert");
                builder.setMessage("Dieser Termin kann nur vom Gastgeber gelöscht werden.");
                builder.create().show();
            }
        });

        // Meeting Observer
        meetingViewModel.getcurrentMeeting(meetingId).observe(this, meeting -> {
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
            if(currentMeeting == null || !buttonView.isPressed()) {return;}
            String newStatus = isChecked ? "planned" : "open";
            if(!currentMeeting.getStatus().equals(newStatus)){
                currentMeeting.setStatus(newStatus);
                meetingViewModel.update(currentMeeting);
                meetingStatusUpdate(newStatus);
            }
        });

        // Spiele Voting Click
        tvVoteStatus.setOnClickListener(v -> {
            if (meetingId <= 0 || currentUserId <= 0) return;
            Intent intent = new Intent(this, VoteGamesActivity.class);
            intent.putExtra(VoteGamesActivity.EXTRA_MEETING_ID, (long) meetingId);
            intent.putExtra(VoteGamesActivity.EXTRA_USER_ID, currentUserId);
            startActivity(intent);
        });

        // Essen Voting Click
        tvFoodStatus.setOnClickListener(v -> {
            Intent intent = new Intent(this, FoodSelectionActivity.class);
            intent.putExtra("MEETING_ID", meetingId);
            startActivity(intent);
        });

        // Observer für Statistiken
        foodViewModel.getTotalUserCount().observe(this, total -> {
            totalUsers = (total == null) ? 0 : total;
            updateTotalLabels();
            updateOrderNotificationVisibility();
        });

        if (meetingId > 0) {
            long voteUserId = currentUserId > 0 ? currentUserId : -1L;
            
            // Spiel-Votes
            votesViewModel.getMyCount(meetingId, voteUserId).observe(this, this::updateVoteStatus);
            votesViewModel.getTotalUsersVoted(meetingId).observe(this, count -> {
                int c = (count == null) ? 0 : count;
                tvTotalVotes.setText("Spiel-Voting: " + c + " von " + totalUsers + " Personen");
            });

            // Essen-Votes
            foodViewModel.getVoteByUser(meetingId, voteUserId).observe(this, vote -> {
                hasIVotedForFood = (vote != null);
                updateFoodStatusLabel();
            });

            foodViewModel.getResults(meetingId).observe(this, results -> {
                if (results != null && !results.isEmpty()) {
                    FoodVoteResult top = results.get(0);
                    for (FoodVoteResult r : results) {
                        if (r.voteCount > top.voteCount) {
                            top = r;
                        }
                    }
                    mostPopularFoodType = top.foodType;
                } else {
                    mostPopularFoodType = "";
                }
                updateFoodStatusLabel();
            });

            foodViewModel.getVotedUserCount(meetingId).observe(this, count -> {
                foodVoteCount = (count == null) ? 0 : count;
                tvTotalFoodVotes.setText("Essen-Voting: " + foodVoteCount + " von " + totalUsers + " Personen");
                updateOrderNotificationVisibility();
            });

            votesViewModel.getGames(meetingId, voteUserId).observe(this, this::updateTopGames);
        }

        btnMessageHost.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("meeting_id", currentMeeting.getMeeting_id());
            intent.putExtra("user", currentUser.name);
            startActivity(intent);
        });

        btnRate.setOnClickListener(v -> {
            Intent intent = new Intent(this, RatingListActivity.class);
            intent.putExtra("meeting_id", currentMeeting.getMeeting_id());
            startActivity(intent);
        });
    }

    private void updateTotalLabels() {
        // Triggered wenn totalUsers geladen wurde
        meetingViewModel.getcurrentMeeting(meetingId).observe(this, m -> {
             // Re-trigger observers if needed or just wait for their next emission
        });
    }

    private boolean isMyMeeting() {
        if (currentMeeting == null) return false;
        return SessionManager.getCurrentUserId(this) == currentMeeting.getHost_id();
    }

    private void fillTextViews(){
        tvtitle.setText(currentMeeting.getTitle());
        tvdate.setText("Datum: " + currentMeeting.getFormatedDate());
        tvtime.setText("Uhrzeit: " + currentMeeting.getFormatedTime());
        tvhost.setText("Gastgeber: " + currentUser.name);
        tvlocation.setText("Ort: " + currentMeeting.getLocation());
    }

    private void updateVoteStatus(Integer count) {
        int c = (count == null) ? 0 : count;
        int capped = Math.min(c, 3);
        if (capped <= 0) {
            tvVoteStatus.setText("Spiel: Abstimmen");
        } else {
            tvVoteStatus.setText("Spiel: Abgestimmt (" + capped + "/3)");
        }
    }

    private void updateFoodStatusLabel() {
        if (hasIVotedForFood) {
            String displayType = (mostPopularFoodType == null || mostPopularFoodType.isEmpty()) ? "" : " (" + mostPopularFoodType + ")";
            tvFoodStatus.setText("Essen: Abgestimmt" + displayType);
        } else {
            tvFoodStatus.setText("Essen: Abstimmen");
        }
    }

    private void updateTopGames(List<GameVoteInfo> games) {
        List<GameVoteInfo> top = new ArrayList<>();
        if (games != null) {
            for (GameVoteInfo game : games) {
                if (game.voteCount <= 0) continue;
                top.add(game);
                if (top.size() >= 3) break;
            }
        }
        topGamesAdapter.setItems(top);
    }

    private void setVoteEnabled(boolean enabled) {
        tvVoteStatus.setEnabled(enabled);
        tvVoteStatus.setAlpha(enabled ? 1f : 0.6f);
        tvFoodStatus.setEnabled(enabled);
        tvFoodStatus.setAlpha(enabled ? 1f : 0.6f);
    }

    private void updateOrderNotificationVisibility() {
        if (currentMeeting == null || tvOrderNotification == null) return;
        boolean isClosed = "closed".equals(currentMeeting.getStatus());
        boolean isHost = isMyMeeting();

        // Die Benachrichtigung wird nur für den Gastgeber angezeigt, 
        // wenn ALLE Personen beim Essen abgestimmt haben (foodVoteCount >= totalUsers)
        // und mindestens 1 User existiert und der Termin nicht abgeschlossen ist.
        if (isHost && totalUsers > 0 && foodVoteCount >= totalUsers && !isClosed) {
            tvOrderNotification.setText("Essen kann bestellt werden.");
            tvOrderNotification.setVisibility(View.VISIBLE);
        } else {
            tvOrderNotification.setVisibility(View.GONE);
        }
    }

    private void meetingStatusUpdate(String newStatus){
        long now = System.currentTimeMillis();
        if(currentMeeting.getTimestmap() < now && !currentMeeting.getStatus().equals("closed")){
            currentMeeting.setStatus("closed");
            meetingViewModel.update(currentMeeting);
        }

        if (newStatus.equals("closed")) {
            switchStatus.setChecked(true);
            switchStatus.setText("Abgeschlossen");
            switchStatus.setEnabled(false);
            setVoteEnabled(false);
            btnRate.setVisibility(View.VISIBLE);
        } else if (newStatus.equals("planned")) {
            switchStatus.setChecked(true);
            switchStatus.setText("Planung fertig");
            switchStatus.setEnabled(true);
            setVoteEnabled(false);
            btnRate.setVisibility(View.GONE);
        } else {
            switchStatus.setChecked(false);
            switchStatus.setText("Planung offen");
            switchStatus.setEnabled(true);
            setVoteEnabled(true);
            btnRate.setVisibility(View.GONE);
        }

        updateOrderNotificationVisibility();
        if (!isMyMeeting() && !newStatus.equals("closed")) {
            switchStatus.setEnabled(false);
        }
    }

    private static class TopGamesAdapter extends RecyclerView.Adapter<TopGamesAdapter.ViewHolder> {
        private final List<GameVoteInfo> items = new ArrayList<>();
        void setItems(List<GameVoteInfo> newItems) {
            items.clear();
            if (newItems != null) items.addAll(newItems);
            notifyDataSetChanged();
        }
        @NonNull @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }
        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            GameVoteInfo game = items.get(position);
            holder.text.setText((position + 1) + ". " + game.name);
            holder.text.setTextColor(Color.BLACK);
        }
        @Override public int getItemCount() { return items.size(); }
        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView text;
            ViewHolder(View itemView) { super(itemView); text = itemView.findViewById(android.R.id.text1); }
        }
    }
}
