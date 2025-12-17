package de.iu.boardgame.feature_termine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.iu.boardgame.MainActivity;
import de.iu.boardgame.feature_termine.ui.adapter.MeetingAdapter;
import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModelFactory;

public class MeetingListActivity extends AppCompatActivity {

    private MeetingViewModel meetingViewModel;
    MeetingViewModelFactory factory;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meeting_list);
        //factory = new MeetingViewModelFactory(this.getApplication());
        btnAdd = findViewById(R.id.btdAdd);
        meetingViewModel = new ViewModelProvider(this).get(MeetingViewModel.class);
        //meetingViewModel = new ViewModelProvider(this, factory).get(MeetingViewModel.class);
        // RecylerView finden
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //Adapter erstellen
        MeetingAdapter adapter = new MeetingAdapter();

        adapter.setOnItemClickListener(meeting -> {
           Intent intent = new Intent(MeetingListActivity.this, MeetingDetailActivity.class);
           intent.putExtra("MEETING_ID", meeting.getMeeting_id());
           startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //ViewModel beobachten
        meetingViewModel.getAllMeetings().observe(this, meetings -> {
            if(meetings != null) {
                adapter.setMeetings(meetings);
            }
        });

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(MeetingListActivity.this, MeetingCreateForm.class));
        });
    }
}