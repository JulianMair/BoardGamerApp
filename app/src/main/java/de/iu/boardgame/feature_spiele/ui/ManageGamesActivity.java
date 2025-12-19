package de.iu.boardgame.feature_spiele.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_spiele.data.Game;
import de.iu.boardgame.feature_spiele.ui.adapter.ManageGameAdapter;
import de.iu.boardgame.feature_spiele.viewmodel.GamesViewModel;

public class ManageGamesActivity extends AppCompatActivity implements ManageGameAdapter.Listener {

    private ManageGameAdapter adapter;
    private GamesViewModel gamesViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_games);

        RecyclerView rv = findViewById(R.id.rvGames);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ManageGameAdapter(this);
        rv.setAdapter(adapter);

        gamesViewModel = new ViewModelProvider(this).get(GamesViewModel.class);

        // ✅ LiveData beobachten -> kein onResume/loadGames mehr nötig
        gamesViewModel.getAllGames().observe(this, games -> adapter.setItems(games));
    }

    public void showAddDialog() {
        showEditDialog(null);
    }

    @Override
    public void onEdit(Game game) {
        showEditDialog(game);
    }

    @Override
    public void onDelete(Game game) {
        new AlertDialog.Builder(this)
                .setTitle("Spiel löschen")
                .setMessage("Willst du \"" + game.gameTitle + "\" wirklich löschen?")
                .setNegativeButton("Abbrechen", null)
                .setPositiveButton("Löschen", (d, w) -> {
                    gamesViewModel.deleteById(game.id);
                    Toast.makeText(this, "Gelöscht", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showEditDialog(@Nullable Game existing) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_game_edit, null, false);

        EditText etName = view.findViewById(R.id.etGameName);
        EditText etDuration = view.findViewById(R.id.etDuration);
        EditText etCategory = view.findViewById(R.id.etCategory);

        boolean isEdit = existing != null;

        if (isEdit) {
            etName.setText(existing.gameTitle);
            etDuration.setText(String.valueOf(existing.gameDuration));
            etCategory.setText(existing.category);
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(isEdit ? "Spiel bearbeiten" : "Spiel hinzufügen")
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

            if (isEdit) {
                // id behalten!
                existing.gameTitle = name;
                existing.category = category;
                existing.gameDuration = duration;

                gamesViewModel.update(existing);

                Toast.makeText(this, "Aktualisiert", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                gamesViewModel.insert(new Game(name, duration, category));

                Toast.makeText(this, "Hinzugefügt", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
