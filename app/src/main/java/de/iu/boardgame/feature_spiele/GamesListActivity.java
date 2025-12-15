package de.iu.boardgame.feature_spiele;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.iu.boardgame.R;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class GamesListActivity extends AppCompatActivity {

    private GameAdapter adapter;
    private GamesDatabase db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        db = GamesDatabase.getInstance(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = findViewById(R.id.rvGames);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GameAdapter();
        rv.setAdapter(adapter);

        loadGames();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_games_list, menu);
        return true;
    }

    // 🔽 Klick auf Icon im Menü
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add_game) {
            showAddDialog();
            return true;
        }

        if (id == R.id.action_manage_games) {
            startActivity(new Intent(this, ManageGamesActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadGames();
    }

    private void loadGames() {
        GamesDatabase.runDb(() -> {
            List<Game> games = db.gameDao().getAll();
            runOnUiThread(() -> adapter.setItems(games));
        });
    }


    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_game_edit, null, false);

        EditText etName = view.findViewById(R.id.etGameName);
        EditText etDuration = view.findViewById(R.id.etDuration);
        EditText etCategory = view.findViewById(R.id.etCategory);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Spiel hinzufügen")
                .setView(view)
                .setNegativeButton("Abbrechen", null)
                .setPositiveButton("Speichern", null)
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String category = etCategory.getText().toString().trim();
            String durationStr = etDuration.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                etName.setError("Pflichtfeld");
                return;
            }
            if (TextUtils.isEmpty(category)) {
                etCategory.setError("Pflichtfeld");
                return;
            }

            int duration = 0;
            if (!TextUtils.isEmpty(durationStr)) {
                try {
                    duration = Integer.parseInt(durationStr);
                } catch (NumberFormatException e) {
                    etDuration.setError("Zahl erforderlich");
                    return;
                }
            }

            Game g = new Game(name, duration, category);

            GamesDatabase.runDb(() -> {
                db.gameDao().insert(g);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Hinzugefügt", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadGames(); // Liste aktualisieren
                });
            });
        });
    }

}

