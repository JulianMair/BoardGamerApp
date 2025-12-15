package de.iu.boardgame.feature_spiele;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import de.iu.boardgame.R;

public class AddGameActivity extends AppCompatActivity {

    private EditText etName, etDuration, etCategory;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        etName = findViewById(R.id.etGameName);
        etDuration = findViewById(R.id.etGameDuration);
        etCategory = findViewById(R.id.etGameCategory);
        btnSave = findViewById(R.id.btnSaveGame);

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

        Game game = new Game(name, duration, category);

        GamesDatabase db = GamesDatabase.getInstance(getApplicationContext());

        GamesDatabase.runDb(() -> {
            db.gameDao().insert(game);

            runOnUiThread(() -> {
                Toast.makeText(this, "Spiel gespeichert!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
