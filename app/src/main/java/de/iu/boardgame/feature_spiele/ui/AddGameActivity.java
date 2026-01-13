package de.iu.boardgame.feature_spiele.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_spiele.data.Game;
import de.iu.boardgame.feature_spiele.viewmodel.GamesViewModel;

/**
 * Activity zum Hinzufügen eines neuen Spiels.
 *
 * Verantwortlichkeiten:
 * - Eingaben auslesen & validieren
 * - Game-Objekt erstellen
 * - Speichern über ViewModel (MVVM)
 */
public class AddGameActivity extends AppCompatActivity {

    // UI-Elemente
    private EditText nameInput;
    private EditText durationInput;
    private EditText categoryInput;
    private Button saveButton;

    // MVVM
    private GamesViewModel gamesViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        bindViews();
        setupViewModel();
        setupListeners();
    }

    /**
     * Findet und cached alle Views.
     */
    private void bindViews() {
        nameInput = findViewById(R.id.etGameName);
        durationInput = findViewById(R.id.etGameDuration);
        categoryInput = findViewById(R.id.etGameCategory);
        saveButton = findViewById(R.id.btnSaveGame);
    }

    /**
     * ViewModel beziehen.
     */
    private void setupViewModel() {
        gamesViewModel = new ViewModelProvider(this).get(GamesViewModel.class);
    }

    /**
     * Click-Listener registrieren.
     */
    private void setupListeners() {
        saveButton.setOnClickListener(v -> onSaveClicked());
    }

    /**
     * Handler für den Speichern-Button.
     * Validiert Eingaben und speichert das Game.
     */
    private void onSaveClicked() {
        Game gameToSave = buildGameFromInputs();
        if (gameToSave == null) return; // Validierung fehlgeschlagen

        // Persistieren über ViewModel
        gamesViewModel.insert(gameToSave);

        Toast.makeText(this, "Spiel gespeichert!", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Liest Eingaben aus, validiert sie und erzeugt ein Game.
     *
     * @return Game bei Erfolg, sonst null (Fehler wird direkt am Feld angezeigt)
     */
    @Nullable
    private Game buildGameFromInputs() {
        String title = readTrimmed(nameInput);
        String category = readTrimmed(categoryInput);
        String durationText = readTrimmed(durationInput);

        // Pflichtfelder
        if (TextUtils.isEmpty(title)) {
            nameInput.setError("Bitte Spielname eingeben");
            return null;
        }
        if (TextUtils.isEmpty(category)) {
            categoryInput.setError("Bitte Kategorie eingeben");
            return null;
        }

        // Dauer: optional, aber wenn gesetzt, muss es eine Zahl sein
        Integer durationMinutes = parseOptionalInt(durationText);
        if (durationMinutes == null) {
            durationInput.setError("Bitte Zahl eingeben");
            return null;
        }

        return new Game(title, durationMinutes, category);
    }

    /**
     * Hilfsmethode: EditText auslesen und trimmen.
     */
    private String readTrimmed(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * Parst eine optionale Zahl:
     * - leer -> 0
     * - gültige Zahl -> Zahl
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
