package de.iu.boardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import de.iu.boardgame.feature_spiele.ui.GamesListActivity;
import de.iu.boardgame.feature_termine.ui.MeetingCreateForm;
import de.iu.boardgame.feature_termine.ui.MeetingListActivity;
import de.iu.boardgame.feature_user.helpers.SessionManager;
import de.iu.boardgame.feature_user.ui.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termine_activity_main);

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

    }
}
