package de.iu.boardgame.feature_termine.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.iu.boardgame.feature_termine.ui.adapter.MeetingAdapter;
import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModelFactory;

/**
 * Die Haupt-Activity für Termine.
 * Zeigt eine Liste aller geplanten Spieleabende an.
 * Von hier aus kann man Details ansehen (Klick auf Item) oder neue Termine erstellen (FAB).
 */
public class MeetingListActivity extends AppCompatActivity {

    private MeetingViewModel meetingViewModel;
    MeetingViewModelFactory factory;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Modernes Android-Feature: Die App nutzt den Platz hinter der Statusleiste
        EdgeToEdge.enable(this);
        setContentView(R.layout.termine_activity_meeting_list);

        // Views verbinden
        btnAdd = findViewById(R.id.btdAdd);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // --- 1. VIEWMODEL SETUP ---
        // #############################################################################
        //meetingViewModel = new ViewModelProvider(this).get(MeetingViewModel.class);
        MeetingViewModelFactory factory = new MeetingViewModelFactory(this.getApplication());
        meetingViewModel = new ViewModelProvider(this, factory).get(MeetingViewModel.class);


        // --- 2. RECYCLERVIEW SETUP ---

        // a) Der Adapter (Der Kellner, der die Daten bringt)
        MeetingAdapter adapter = new MeetingAdapter();

        // b) Klick-Logik definieren
        // Wir nutzen hier das Interface, das wir im Adapter gebaut haben.
        adapter.setOnItemClickListener(meeting -> {
           Intent intent = new Intent(MeetingListActivity.this, MeetingDetailActivity.class);

            // Wir packen die ID des angeklickten Meetings in den "Rucksack" des Intents.
            // In der DetailActivity holen wir sie wieder raus.
            // (Hinweis: Achte drauf, dass der Getter in Meeting.java 'getMeetingId' heißt, camelCase!)
           intent.putExtra("MEETING_ID", meeting.getMeeting_id());
           startActivity(intent);
        });

        // c) Verknüpfung
        recyclerView.setAdapter(adapter);

        // d) LayoutManager
        // LinearLayoutManager -> Einfache Liste von oben nach unten.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- 3. BEOBACHTEN (OBSERVER) ---
        // Das ist die Live-Verbindung zur Datenbank.
        // Immer wenn sich in der DB etwas ändert (Insert/Delete), läuft dieser Code hier ab.
        meetingViewModel.getAllMeetings().observe(this, meetings -> {
            if(meetings != null) {
                // Wir geben die NEUE Liste an den Adapter.
                // Der Adapter kümmert sich dann darum, die Liste neu zu zeichnen.
                adapter.setMeetings(meetings);
            }
        });

        // --- 4. NEUEN TERMIN ERSTELLEN ---
        btnAdd.setOnClickListener(v -> {
            //Wechsel zur Erstell-Maske
            startActivity(new Intent(MeetingListActivity.this, MeetingCreateForm.class));
        });
    }
}