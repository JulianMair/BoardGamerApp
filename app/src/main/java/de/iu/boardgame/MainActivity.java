package de.iu.boardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import de.iu.boardgame.feature_spiele.GamesDatabase;
import de.iu.boardgame.logging.AppLog;

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