package de.iu.boardgame.feature_termine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.ui.adapter.MeetingAdapter;
import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModelFactory;
import de.iu.boardgame.feature_user.data.User;
import de.iu.boardgame.feature_user.viewmodel.UsersViewModel;

/**
 * Die Haupt-Activity für Termine.
 * Zeigt eine Liste aller geplanten Spieleabende an.
 * Von hier aus kann man Details ansehen (Klick auf Item) oder neue Termine erstellen (FAB).
 */
public class MeetingListActivity extends AppCompatActivity {

    private MeetingViewModel meetingViewModel;
    private UsersViewModel userViewModel;
    private TextView tvNextHostName;
    private MeetingViewModelFactory factory;
    private FloatingActionButton btnAdd;
    private ImageButton btnBack;
    private MeetingAdapter adapter;
    private List<User> loadedUsers = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Modernes Android-Feature: Die App nutzt den Platz hinter der Statusleiste
        EdgeToEdge.enable(this);
        setContentView(R.layout.termine_activity_meeting_list);

        // Views verbinden
        btnAdd = findViewById(R.id.btdAdd);
        btnBack = findViewById(R.id.btnBack);
        tvNextHostName = findViewById(R.id.tvNextHostName);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // --- 1. VIEWMODEL SETUP ---
        // #############################################################################
        MeetingViewModelFactory factory = new MeetingViewModelFactory(this.getApplication());
        meetingViewModel = new ViewModelProvider(this, factory).get(MeetingViewModel.class);
        // Usermodel Setup
        userViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        // --- 2. RECYCLERVIEW SETUP ---

        // a) Der Adapter (Der Kellner, der die Daten bringt)
        adapter = new MeetingAdapter();

        // b) Klick-Logik definieren
        // Interfaces des Adapters nutzen
        adapter.setOnItemClickListener(meeting -> {
           Intent intent = new Intent(MeetingListActivity.this, MeetingDetailActivity.class);

            // Übergeben der ID des angeklickten Meetings in den Intent.
           intent.putExtra("MEETING_ID", meeting.getMeeting_id());
           startActivity(intent);
        });

        // c) Verknüpfung
        recyclerView.setAdapter(adapter);

        // d) LayoutManager
        // LinearLayoutManager -> Einfache Liste von oben nach unten.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- 3. BEOBACHTEN (OBSERVER) ---
        // Live-Verbindung zur Datenbank.
        // Immer wenn sich in der DB etwas ändert (Insert/Delete), wird der Code aufgerufen.
        meetingViewModel.getDisplayMeetings().observe(this, meetings -> {
            if(meetings != null) {
                // NEUE Liste an den Adapter übergeben.
                // Der Adapter zeichnet die Liste neu.
                adapter.setMeetings(meetings);
            }
        });

        // Liste aller Meetings in der DB zur Berechnung des nächsten Hosts
        meetingViewModel.getAllMeetings().observe(this, meetings -> {
            if(meetings != null) {
                setNextHost(meetings);
            }
        });

        userViewModel.getAllUsers().observe(this, users -> {
            if(users != null){
                loadedUsers = users;
            }
        });

        // --- 4. NEUEN TERMIN ERSTELLEN ---
        btnAdd.setOnClickListener(v -> {
            //Wechsel zur Erstell-Maske
            startActivity(new Intent(MeetingListActivity.this, MeetingCreateForm.class));
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
        // Nächsten Host anzeigen
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setNextHost(List<Meeting> currentMeetingList){
        if (loadedUsers != null
                && currentMeetingList != null) {
            User nextHost = de.iu.boardgame.feature_termine.logic.NextHostCalculator.calculateNextHostId(currentMeetingList, loadedUsers);
            if (nextHost != null) {
                tvNextHostName.setText(nextHost.name);
            } else {
                tvNextHostName.setText("Alea nondum iacta est.");
            }
        }

    }
}