package de.iu.boardgame.feature_termine.ui;

import de.iu.boardgame.BaseActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import de.iu.boardgame.feature_spiele.ui.GamesListActivity;
import de.iu.boardgame.feature_termine.data.Meeting;
import de.iu.boardgame.feature_termine.logic.NextHostCalculator;
import de.iu.boardgame.feature_termine.ui.adapter.MeetingAdapter;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModelFactory;
import de.iu.boardgame.feature_user.data.User;
import de.iu.boardgame.feature_user.helpers.SessionManager;
import de.iu.boardgame.feature_user.ui.LoginActivity;
import de.iu.boardgame.feature_user.viewmodel.UsersViewModel;

/**
 * Die Haupt-Activity für Termine.
 * Zeigt eine Liste aller geplanten Spieleabende an.
 * Von hier aus kann man Details ansehen (Klick auf Item) oder neue Termine erstellen.
 */
public class MeetingListActivity extends BaseActivity {

    private MeetingViewModel meetingViewModel;
    private UsersViewModel userViewModel;
    private TextView tvNextHostName;
    private MeetingViewModelFactory factory;
    private FloatingActionButton btnAdd;
    private ImageButton btnBack;
    private ImageButton btnMenu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MeetingAdapter adapter;
    private List<User> loadedUsers = null;
    private List<Meeting> loadedMeetings = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.termine_activity_meeting_list);

        // Burger Menü initialisieren
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        btnMenu = findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START)
        );

        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_logout) {
                SessionManager.clearCurrentUserId(MeetingListActivity.this);
                Intent intent = new Intent(MeetingListActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            else if (id == R.id.nav_add_game) {
                startActivity(new Intent(MeetingListActivity.this, GamesListActivity.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Views verbinden
        btnAdd = findViewById(R.id.btdAdd);
        btnBack = findViewById(R.id.btnBack);
        tvNextHostName = findViewById(R.id.tvNextHostName);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // --- 1. VIEWMODEL SETUP ---

        MeetingViewModelFactory factory = new MeetingViewModelFactory(this.getApplication());
        meetingViewModel = new ViewModelProvider(this, factory).get(MeetingViewModel.class);

        // Usermodel Setup
        userViewModel = new ViewModelProvider(this).get(UsersViewModel.class);


        // --- 2. RECYCLERVIEW SETUP ---

        // a) Initialisierung des Adapters
        adapter = new MeetingAdapter();

        // b) Klick-Logik definieren
        adapter.setOnItemClickListener(meeting -> {
            Intent intent = new Intent(MeetingListActivity.this, MeetingDetailActivity.class);

            // Übergeben der ID des angeklickten Meetings in den Intent.
            intent.putExtra("MEETING_ID", meeting.getMeeting_id());
            startActivity(intent);
        });

        // c) Verknüpfung
        recyclerView.setAdapter(adapter);

        // d) LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- 3. BEOBACHTEN (OBSERVER) ---
        meetingViewModel.getDisplayMeetings().observe(this, meetings -> {
            if(meetings != null) {
                adapter.setMeetings(meetings);
            }
        });

        meetingViewModel.getAllMeetings().observe(this, meetings -> {
            if(meetings != null) {
                loadedMeetings = meetings;
                setNextHost(loadedMeetings);
            }
        });

        userViewModel.getAllUsers().observe(this, users -> {
            if(users != null){
                loadedUsers = users;
                setNextHost(loadedMeetings);
            }
            if(loadedUsers != null && allMeetingCatch != null){
                setNextHost(allMeetingCatch);
            }
        });

        // --- 4. NEUEN TERMIN ERSTELLEN ---
        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(MeetingListActivity.this, MeetingCreateForm.class));
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SessionManager.getCurrentUserId(this) <= 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setNextHost(List<Meeting> currentMeetingList){
        if (loadedUsers != null && currentMeetingList != null) {
            User nextHost = NextHostCalculator.calculateNextHostId(currentMeetingList, loadedUsers);
            if (nextHost != null) {
                tvNextHostName.setText(nextHost.name);
            } else {
                tvNextHostName.setText("Alea nondum iacta est.");
            }
        }
    }
}
