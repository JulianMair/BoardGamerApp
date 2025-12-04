package de.iu.boardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import de.iu.boardgame.feature_termine.MeetingCreateForm;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_termin = (Button) findViewById(R.id.TerminVerwaltung);
        btn_termin.setOnClickListener(v -> {
            Intent intent = new Intent(this, MeetingCreateForm.class);
            startActivity(intent);
        });



    }
}