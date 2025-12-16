package de.iu.boardgame.feature_termine.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.iu.boardgame.feature_termine.ui.adapter.MeetingAdapter;
import de.iu.boardgame.R;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;

public class MeetingListActivity extends AppCompatActivity {

    private MeetingViewModel meetingViewModel;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meeting_list);
        btnAdd = findViewById(R.id.btnAdd)

        meetingViewModel = new ViewModelProvider(this).get(MeetingViewModel.class);
        // RecylerView finden
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //Adapter erstellen
        MeetingAdapter adapter = new MeetingAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //ViewModel beobachten
        meetingViewModel.getAllMeetings().observe(this, adapter::setMeetings);
    }
}