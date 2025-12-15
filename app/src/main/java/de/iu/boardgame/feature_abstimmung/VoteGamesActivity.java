package de.iu.boardgame.feature_abstimmung;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_abstimmung.GameVoteInfo;
import de.iu.boardgame.feature_spiele.GamesDatabase;
import de.iu.boardgame.feature_abstimmung.Vote;

public class VoteGamesActivity extends AppCompatActivity implements VoteListAdapter.Listener {

    public static final String EXTRA_MEETING_ID = "meeting_id";
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    private GamesDatabase db;
    private long meetingId;
    private long userId;

    private TextView tvMyVotes;
    private VoteListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_games);

        meetingId = getIntent().getLongExtra(EXTRA_MEETING_ID, -1L);
        if (meetingId <= 0) {
            finish();
            return;
        }

        // TODO: userId aus SharedPreferences holen. Aktuell gemocked.
        // 1) aus Intent (Test)
        userId = getIntent().getLongExtra(EXTRA_USER_ID, -1L);

        // 2) Fallback: aus SharedPrefs (Produktiv)
        if (userId <= 0) {
            userId = getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .getLong("login_id", -1L);
        }

        if (userId <= 0) {
            Toast.makeText(this, "Kein User eingeloggt", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        db = GamesDatabase.getInstance(getApplicationContext());

        tvMyVotes = findViewById(R.id.tvMyVotes);

        RecyclerView rv = findViewById(R.id.rvVoteGames);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new VoteListAdapter(this);
        rv.setAdapter(adapter);

        load();
    }

    private void load() {
        GamesDatabase.runDb(() -> {
            int myCount = db.voteDao().countVotesByUser(meetingId, userId);
            List<GameVoteInfo> list = db.voteDao().getGamesWithVotes(meetingId, userId);

            runOnUiThread(() -> {
                tvMyVotes.setText("Deine Stimmen: " + myCount + "/3");
                adapter.setItems(list, myCount);
            });
        });
    }

    @Override
    public void onToggleVote(GameVoteInfo game) {
        GamesDatabase.runDb(() -> {
            boolean alreadyVoted = game.isVotedByMe();
            if (alreadyVoted) {
                db.voteDao().deleteVote(meetingId, userId, game.id);
            } else {
                int myCount = db.voteDao().countVotesByUser(meetingId, userId);
                if (myCount >= 3) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Maximal 3 Spiele pro User", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }
                db.voteDao().insertVote(new Vote(meetingId, userId, game.id));
            }
            runOnUiThread(this::load);
        });
    }
}
