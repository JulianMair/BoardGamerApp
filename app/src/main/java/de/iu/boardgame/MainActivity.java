package de.iu.boardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import de.iu.boardgame.feature_abstimmung.ui.VoteGamesActivity;
import de.iu.boardgame.feature_spiele.ui.GamesListActivity;
import de.iu.boardgame.feature_termine.ui.MeetingCreateForm;
import de.iu.boardgame.feature_termine.ui.MeetingListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdministration = findViewById(R.id.btnAdministration);
        Button btnAppointment = findViewById(R.id.btnAppointment);
        Button btnEvaluate = findViewById(R.id.btnEvaluate);
        Button btnUser = findViewById(R.id.btnUser);
        Button btnVoting = findViewById(R.id.btnVoting);
        Button btnFoodAdministration = findViewById(R.id.btnFoodAdministration);

        Button btnShowGames = findViewById(R.id.btnShowGames);
        btnShowGames.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, GamesListActivity.class))
        );

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

    }
}