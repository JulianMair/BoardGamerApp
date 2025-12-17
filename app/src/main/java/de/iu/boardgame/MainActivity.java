package de.iu.boardgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.LocalDate;


import de.iu.boardgame.feature_termine.Meeting;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MeetingAdapter adapter;
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
        eventList.add(new Meeting("test", LocalDate.now(),LocalTime.of(19,30), "Bei Anna zuhause", 1212313,"open"));
        eventList.add(new Meeting("test", LocalDate.now(),LocalTime.of(19,30), "Bei Anna zuhause", 1212313,"open"));
        eventList.add(new Meeting("test", LocalDate.now(),LocalTime.of(19,30), "Bei Anna zuhause", 1212313,"open"));
        eventList.add(new Meeting("test", LocalDate.now(),LocalTime.of(19,30), "Bei Anna zuhause", 1212313,"open"));
        eventList.add(new Meeting("test", LocalDate.now(),LocalTime.of(19,30), "Bei Anna zuhause", 1212313,"open"));
        eventList.add(new Meeting("test", LocalDate.now(),LocalTime.of(19,30), "Bei Anna zuhause", 1212313,"open"));


        adapter = new MeetingAdapter(this, eventList);
        recyclerView.setAdapter(adapter);

        btnCreateEvent.setOnClickListener(v -> {
            // Öffne CreateEventActivity
        });
    }
}

