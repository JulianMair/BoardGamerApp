package de.iu.boardgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;


import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.ui.MeetingListActivity;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MeetingAdaptertest adapter;
    private List<Meeting> eventList;
    private Button btnCreateEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Beispiel-Daten (später aus Room DB)
        eventList = new ArrayList<>();
        long now = System.currentTimeMillis();

        eventList.add(new Meeting(
                "Projekt Kickoff",
                now,
                "Konferenzraum A",
                1001L,
                "geplant"
        ));

        eventList.add(new Meeting(
                "Sprint Review",
                now + 3 * 24 * 60 * 60 * 1000L,  // +3 Tage
                "Meetingraum B",
                1002L,
                "bestätigt"
        ));

        eventList.add(new Meeting(
                "Team Lunch",
                now + 7 * 24 * 60 * 60 * 1000L,  // +7 Tage
                "Cafeteria",
                1003L,
                "abgesagt"
        ));

        eventList.add(new Meeting(
                "Retrospektive",
                now + 10 * 24 * 60 * 60 * 1000L, // +10 Tage
                "Raum 204",
                1004L,
                "geplant"
        ));

        adapter = new MeetingAdaptertest(this, eventList);
        recyclerView.setAdapter(adapter);

        btnCreateEvent.setOnClickListener(v -> {
            Intent intent = new Intent(this, MeetingListActivity.class);
            this.startActivity(intent);
        });
    }
}

