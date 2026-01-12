package de.iu.boardgame.feature_spiele.ui;

import android.app.AlertDialog;
import android.content.Intent;
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

    // UI
    private GameAdapter gameAdapter;

    // MVVM
    private GamesViewModel gamesViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        setupToolbar();
        setupRecyclerView();
        setupViewModel();
        observeGames();
    }

    /**
     * Initialisiert Toolbar und aktiviert die ActionBar.
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * RecyclerView: LayoutManager + Adapter setzen.
     */
    private void setupRecyclerView() {
        RecyclerView gamesRecyclerView = findViewById(R.id.rvGames);
        gamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        gameAdapter = new GameAdapter();
        gamesRecyclerView.setAdapter(gameAdapter);
    }

    /**
     * ViewModel beziehen (über ViewModelProvider).
     */
    private void setupViewModel() {
        gamesViewModel = new ViewModelProvider(this).get(GamesViewModel.class);
    }

    /**
     * LiveData beobachten:
     * Sobald sich die Daten in Room ändern (insert/update/delete),
     * wird die Liste automatisch aktualisiert.
     */
    private void observeGames() {
        gamesViewModel.getAllGames().observe(this, games -> gameAdapter.submitList(games));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_games_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if (itemId == R.id.action_add_game) {
            showAddGameDialog();
            return true;
        }

        if (itemId == R.id.action_manage_games) {
            openManageGamesScreen();
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    /**
     * Öffnet den Screen zum Verwalten (Edit/Delete) der Spiele.
     */
    private void openManageGamesScreen() {
        startActivity(new Intent(this, ManageGamesActivity.class));
    }

    /**
     * Zeigt einen Dialog zum Hinzufügen eines neuen Spiels.
     * Validierung erfolgt im Dialog (Fehler direkt am Feld).
     */
    private void showAddGameDialog() {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_game_edit, null, false);

        EditText nameInput = dialogView.findViewById(R.id.etGameName);
        EditText durationInput = dialogView.findViewById(R.id.etDuration);
        EditText categoryInput = dialogView.findViewById(R.id.etCategory);

        AlertDialog addDialog = new AlertDialog.Builder(this)
                .setTitle("Spiel hinzufügen")
                .setView(dialogView)
                // PositiveButton wird später überschrieben, damit Dialog nicht bei Fehlern schließt
                .setNegativeButton("Abbrechen", null)
                .setPositiveButton("Speichern", null)
                .show();

        addDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            Game newGame = buildGameFromInputs(nameInput, durationInput, categoryInput);
            if (newGame == null) return; // Validierung fehlgeschlagen

            gamesViewModel.insert(newGame);
            Toast.makeText(this, "Hinzugefügt", Toast.LENGTH_SHORT).show();
            addDialog.dismiss();
        });
    }

    /**
     * Liest die Eingaben aus, validiert sie und baut ein Game-Objekt.
     *
     * @return Game bei Erfolg, sonst null (und setzt Feld-Errors).
     */
    @Nullable
    private Game buildGameFromInputs(EditText nameInput,
                                     EditText durationInput,
                                     EditText categoryInput) {

        String name = readTrimmed(nameInput);
        String category = readTrimmed(categoryInput);
        String durationText = readTrimmed(durationInput);

        // Pflichtfelder prüfen
        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Pflichtfeld");
            return null;
        }
        if (TextUtils.isEmpty(category)) {
            categoryInput.setError("Pflichtfeld");
            return null;
        }

        // Dauer ist optional, aber wenn gesetzt, muss es eine Zahl sein
        Integer duration = parseOptionalInt(durationText);
        if (duration == null) {
            durationInput.setError("Zahl erforderlich");
            return null;
        }

        return new Game(name, duration, category);
    }

    /**
     * Hilfsmethode: Text aus EditText lesen und trimmen.
     */
    private String readTrimmed(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * Parst optionalen Integer:
     * - leer -> 0
     * - Zahl -> Zahl
     * - ungültig -> null
     */
    @Nullable
    private Integer parseOptionalInt(String text) {
        if (TextUtils.isEmpty(text)) return 0;
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
