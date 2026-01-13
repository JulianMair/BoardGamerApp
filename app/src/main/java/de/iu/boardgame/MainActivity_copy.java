package de.iu.boardgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.iu.boardgame.feature_spiele.ui.GamesListActivity;
import de.iu.boardgame.feature_termine.ui.MeetingListActivity;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;

public class MainActivity_copy extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MeetingAdaptertest adapter;
    private MeetingViewModel meetingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        Button btnCreateEvent;
        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MeetingAdaptertest(this, new java.util.ArrayList<>());
        recyclerView.setAdapter(adapter);

        // 🔹 ViewModel initialisieren
        meetingViewModel = new ViewModelProvider(this).get(MeetingViewModel.class);

        // 🔹 LiveData aus der DB beobachten
        meetingViewModel.getAllMeetings().observe(this, meetings -> {
            adapter.setData(meetings); // Methode im Adapter zum Aktualisieren
        });

        btnCreateEvent = findViewById(R.id.btnCreateEvent);
        btnCreateEvent.setOnClickListener(v -> {
            startActivity(new Intent(this, MeetingListActivity.class));
        });
        /*
        Button btnAdministration = findViewById(R.id.btnAdministration);
        Button btnAppointment = findViewById(R.id.btnAppointment);
        Button btnEvaluate = findViewById(R.id.btnEvaluate);
        Button btnUser = findViewById(R.id.btnUser);
        Button btnVoting = findViewById(R.id.btnVoting);
        Button btnFoodAdministration = findViewById(R.id.btnFoodAdministration);
*/
        Button btnShowGames = findViewById(R.id.btnManageGames);
        btnShowGames.setOnClickListener(v ->
                startActivity(new Intent(MainActivity_copy.this, GamesListActivity.class))
        );
/*
        Button btnTestVoting = findViewById(R.id.btnTestVoting);
        btnTestVoting.setOnClickListener(v -> {

            // Mock-Daten zum Testen
            //todo: echte Daten einfügen
            long mockMeetingId = 1L;
            long mockUserId = 1L;

            Intent intent = new Intent(MainActivity.this, VoteGamesActivity.class);
            intent.putExtra(VoteGamesActivity.EXTRA_MEETING_ID, mockMeetingId);
            intent.putExtra("EXTRA_USER_ID", mockUserId); // nur für Test

            startActivity(intent);
        });


        btnAdministration.setOnClickListener(v ->
                startActivity(new Intent(this, MeetingCreateForm.class)));

        btnAppointment.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, MeetingListActivity.class)));

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            SessionManager.clearCurrentUserId(MainActivity.this);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        */

    }
}