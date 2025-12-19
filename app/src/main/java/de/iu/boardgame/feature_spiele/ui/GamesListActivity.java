package de.iu.boardgame.feature_spiele.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_spiele.data.Game;
import de.iu.boardgame.feature_spiele.ui.adapter.GameAdapter;
import de.iu.boardgame.feature_spiele.viewmodel.GamesViewModel;

public class GamesListActivity extends AppCompatActivity {

    private GameAdapter adapter;
    private GamesViewModel gamesViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = findViewById(R.id.rvGames);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GameAdapter();
        rv.setAdapter(adapter);

        gamesViewModel = new ViewModelProvider(this).get(GamesViewModel.class);

        // ✅ LiveData beobachten -> kein onResume/loadGames mehr nötig
        gamesViewModel.getAllGames().observe(this, games -> adapter.setItems(games));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_games_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_game) {
            showAddDialog();
            return true;
        }

        if (id == R.id.action_manage_games) {
            startActivity(new android.content.Intent(this, ManageGamesActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
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

            if (TextUtils.isEmpty(name)) { etName.setError("Pflichtfeld"); return; }
            if (TextUtils.isEmpty(category)) { etCategory.setError("Pflichtfeld"); return; }

            int duration = 0;
            if (!TextUtils.isEmpty(durationStr)) {
                try { duration = Integer.parseInt(durationStr); }
                catch (NumberFormatException e) { etDuration.setError("Zahl erforderlich"); return; }
            }

            gamesViewModel.insert(new Game(name, duration, category));
            Toast.makeText(this, "Hinzugefügt", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }
}
