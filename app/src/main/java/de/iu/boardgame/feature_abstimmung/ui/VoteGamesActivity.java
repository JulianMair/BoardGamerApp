package de.iu.boardgame.feature_abstimmung.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_abstimmung.data.GameVoteInfo;
import de.iu.boardgame.feature_abstimmung.ui.adapter.VoteListAdapter;
import de.iu.boardgame.feature_abstimmung.viewmodel.VotesViewModel;

public class VoteGamesActivity extends AppCompatActivity implements VoteListAdapter.Listener {

    public static final String EXTRA_MEETING_ID = "meeting_id";
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    private long meetingId;
    private long userId;

    private TextView tvMyVotes;
    private VoteListAdapter adapter;

    private VotesViewModel votesViewModel;

    private int myCountCached = 0; // für Adapter-disable

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_games);

        meetingId = getIntent().getLongExtra(EXTRA_MEETING_ID, -1L);
        if (meetingId <= 0) {
            finish();
            return;
        }

        userId = getIntent().getLongExtra(EXTRA_USER_ID, -1L);
        if (userId <= 0) {
            userId = getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .getLong("login_id", -1L);
        }

        if (userId <= 0) {
            Toast.makeText(this, "Kein User eingeloggt", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvMyVotes = findViewById(R.id.tvMyVotes);

        RecyclerView rv = findViewById(R.id.rvVoteGames);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VoteListAdapter(this);
        rv.setAdapter(adapter);

        votesViewModel = new ViewModelProvider(this).get(VotesViewModel.class);

        // ✅ Count beobachten
        votesViewModel.getMyCount(meetingId, userId).observe(this, count -> {
            int c = (count == null) ? 0 : count;
            myCountCached = c;
            tvMyVotes.setText("Deine Stimmen: " + c + "/3");
            // Liste wird separat beobachtet, aber Adapter braucht den Count.
            // setItems wird beim Listen-Observer aufgerufen.
        });

        // ✅ Liste beobachten
        votesViewModel.getGames(meetingId, userId).observe(this, list -> {
            adapter.setItems(list, myCountCached);
        });
    }

    @Override
    public void onToggleVote(GameVoteInfo game) {
        votesViewModel.toggleVote(meetingId, userId, game,
                () -> runOnUiThread(() ->
                        Toast.makeText(this, "Maximal 3 Spiele pro User", Toast.LENGTH_SHORT).show()
                )
        );
        // Kein reload nötig: LiveData aktualisiert automatisch, sobald DB geändert wurde.
    }
}
