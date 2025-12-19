package de.iu.boardgame.feature_evaluate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.MainActivity;
import de.iu.boardgame.R;

public class RatingList extends AppCompatActivity {

    private RatingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewBewertungen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        View btnNewRating = findViewById(R.id.fabNeueBewertung);

        adapter = new RatingAdapter();
        recyclerView.setAdapter(adapter);


        btnNewRating.setOnClickListener(v -> {
            startActivity(new Intent(RatingList.this, RatingPage.class));
        });

        loadBewertungen();
    }

    private void loadBewertungen() {
        // Raum DB NICHT im Main Thread
        List<MeetingRating> ratings = new ArrayList<>();
        ratings.add(new MeetingRating(3.5F, 2.5F,4,"war gut", LocalDateTime.now()));
        ratings.add(new MeetingRating(3.5F, 2.5F,4,"war gut", LocalDateTime.now()));
        ratings.add(new MeetingRating(3.5F, 2.5F,4,"war gut", LocalDateTime.now()));

        adapter.setData(ratings);
    }
}
