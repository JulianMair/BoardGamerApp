package de.iu.boardgame.feature_evaluate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_evaluate.data.MeetingRating;
import de.iu.boardgame.feature_evaluate.viewmodel.RatingViewModel;

public class RatingListActivity extends AppCompatActivity {

    private RatingAdapter adapter;
    private RatingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewBewertungen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        View btnNewRating = findViewById(R.id.fabNeueBewertung);

        adapter = new RatingAdapter();
        recyclerView.setAdapter(adapter);

        // ViewModel initialisieren
        viewModel = new ViewModelProvider(this).get(RatingViewModel.class);

        // LiveData aus der DB beobachten
        int meetingId = getIntent().getIntExtra("meeting_id", -1);
        if (meetingId != -1) {
            viewModel.getRatingsForMeeting(meetingId).observe(this, ratings -> {
                adapter.setData(ratings);
            });
        }

        btnNewRating.setOnClickListener(v -> {
            Intent intent = new Intent(RatingListActivity.this, RatingAtivity.class);
            intent.putExtra("meeting_id", meetingId); // ✅ Meeting-ID weitergeben
            startActivity(intent);
        });

    }
}
