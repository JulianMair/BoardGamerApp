package de.iu.boardgame.feature_spiele;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.iu.boardgame.R;

public class ManageGamesActivity extends AppCompatActivity implements ManageGameAdapter.Listener {

    private GamesDatabase db;
    private ManageGameAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_games);

        db = GamesDatabase.getInstance(getApplicationContext());

        RecyclerView rv = findViewById(R.id.rvGames);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ManageGameAdapter(this);
        rv.setAdapter(adapter);

        loadGames();
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
                .setMessage("Willst du \"" + game.name + "\" wirklich löschen?")
                .setNegativeButton("Abbrechen", null)
                .setPositiveButton("Löschen", (d, w) -> {
                    GamesDatabase.runDb(() -> {
                        db.gameDao().deleteById(game.id);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Gelöscht", Toast.LENGTH_SHORT).show();
                            loadGames();
                        });
                    });
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
            etName.setText(existing.name);
            etDuration.setText(String.valueOf(existing.durationMinutes));
            etCategory.setText(existing.category);
        }

        new AlertDialog.Builder(this)
                .setTitle(isEdit ? "Spiel bearbeiten" : "Spiel hinzufügen")
                .setView(view)
                .setNegativeButton("Abbrechen", null)
                .setPositiveButton("Speichern", null) // wir überschreiben gleich, damit Validierung klappt
                .create();

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
                existing.name = name;
                existing.category = category;
                existing.durationMinutes = duration;

                GamesDatabase.runDb(() -> {
                    db.gameDao().update(existing);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Aktualisiert", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadGames();
                    });
                });
            } else {
                Game g = new Game(name, duration, category);
                GamesDatabase.runDb(() -> {
                    db.gameDao().insert(g);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Hinzugefügt", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadGames();
                    });
                });
            }
        });
    }
}
