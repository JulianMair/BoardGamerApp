package de.iu.boardgame;

import static de.iu.boardgame.R.*;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        ) {

        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_add_game) {
                Toast.makeText(this, "Spiel hinzufügen", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.nav_logout) {
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            }
            drawerLayout.closeDrawers();
            return true;
        });

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
