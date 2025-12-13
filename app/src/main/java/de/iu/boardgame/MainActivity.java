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
import de.iu.boardgame.feature_spiele.GamesDatabase;
import de.iu.boardgame.logging.AppLog;
import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.MeetingCreateForm;

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
        AppLog.i("MAIN", "MainActivity onCreate()");

        Button btnAdministration = findViewById(R.id.btnAdministration);
        Button btnAppointment = findViewById(R.id.btnAppointment);
        Button btnEvaluate = findViewById(R.id.btnEvaluate);
        Button btnUser = findViewById(R.id.btnUser);
        Button btnVoting = findViewById(R.id.btnVoting);
        Button btnFoodAdministration = findViewById(R.id.btnFoodAdministration);

        // Triggert:
        // - DB existiert → nichts passiert
        // - DB existiert nicht → onCreate + Dummy-Daten
        GamesDatabase db = GamesDatabase.getInstance(this);
        AppLog.i("BGA-MAIN", "DB instance requested: " + db);

        new Thread(() -> {
            try {
                int count = db.gameDao().getAll().size();
                AppLog.i("BGA-MAIN", "Games in DB: " + count);
            } catch (Exception e) {
                AppLog.e("BGA-MAIN", "DB test read failed: " + e.getMessage(), e);
            }
        }).start();
        btnAdministration.setOnClickListener(v ->
                startActivity(new Intent(this, MeetingCreateForm.class)));

        //Activity aufrufen

/*        btnAdministration.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity1.class)));

        btnAppointment.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity2.class)));

        btnEvaluate.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity3.class)));

        btnAppointment.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity4.class)));

        btnUser.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity4.class)));

        btnVoting.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity4.class)));

        btnFoodAdministration.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity4.class)));*/


        adapter = new MeetingAdapter(this, eventList);
        recyclerView.setAdapter(adapter);

        btnCreateEvent.setOnClickListener(v -> {
            // Öffne CreateEventActivity
        });
    }
}

