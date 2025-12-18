package de.iu.boardgame.feature_spiele.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_spiele.data.Game;
import de.iu.boardgame.feature_spiele.viewmodel.GamesViewModel;

public class AddGameActivity extends AppCompatActivity {

    private EditText etName, etDuration, etCategory;
    private Button btnSave;

    private GamesViewModel gamesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        etName = findViewById(R.id.etGameName);
        etDuration = findViewById(R.id.etGameDuration);
        etCategory = findViewById(R.id.etGameCategory);
        btnSave = findViewById(R.id.btnSaveGame);

        // ✅ ViewModel holen
        gamesViewModel = new ViewModelProvider(this).get(GamesViewModel.class);

        btnSave.setOnClickListener(v -> saveGame());
    }

    private void saveGame() {
        String name = etName.getText().toString().trim();
        String durationStr = etDuration.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Bitte Spielname eingeben");
            return;
        }
        if (TextUtils.isEmpty(category)) {
            etCategory.setError("Bitte Kategorie eingeben");
            return;
        }

        int duration = 0;
        if (!TextUtils.isEmpty(durationStr)) {
            try {
                duration = Integer.parseInt(durationStr);
            } catch (NumberFormatException e) {
                etDuration.setError("Bitte Zahl eingeben");
                return;
            }
        }

        // ✅ über ViewModel speichern
        gamesViewModel.insert(new Game(name, duration, category));

        Toast.makeText(this, "Spiel gespeichert!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
