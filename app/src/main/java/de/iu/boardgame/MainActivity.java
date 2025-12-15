package de.iu.boardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import de.iu.boardgame.feature_spiele.GamesDatabase;
import de.iu.boardgame.feature_spiele.GamesListActivity;
import de.iu.boardgame.feature_abstimmung.VoteGamesActivity;
import de.iu.boardgame.logging.AppLog;
import de.iu.boardgame.feature_termine.MeetingCreateForm;
import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.MeetingCreateForm;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppLog.i("MAIN", "MainActivity onCreate()");

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


    }
}