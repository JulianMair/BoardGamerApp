package de.iu.boardgame.feature_evaluate.ui;

import de.iu.boardgame.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_evaluate.data.MeetingRating;
import de.iu.boardgame.feature_evaluate.data.RatingWithUser;
import de.iu.boardgame.feature_evaluate.viewmodel.RatingViewModel;
import de.iu.boardgame.feature_termine.viewmodel.MeetingViewModel;
import de.iu.boardgame.feature_user.helpers.SessionManager;

public class RatingListActivity extends BaseActivity {

    private RatingAdapter adapter;
    private RatingViewModel viewModel;

    private ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewBewertungen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        View btnNewRating = findViewById(R.id.fabNeueBewertung);
        btnBack = findViewById(R.id.btnBack);

        //User ID aus Session holen
        long userId = SessionManager.getCurrentUserId(this);

        // Adapter initialisieren
        adapter = new RatingAdapter();
        recyclerView.setAdapter(adapter);

        //Zurück Button
        btnBack.setOnClickListener(view -> {
            finish();
        });

        // ViewModel initialisieren
        viewModel = new ViewModelProvider(this).get(RatingViewModel.class);

        //meeting ID über Intent mitnehmen
        int meetingId = getIntent().getIntExtra("meeting_id", -1);

        //Nur Bewerten wenn Spieler

        MeetingViewModel meetingViewModel =
                new ViewModelProvider(this).get(MeetingViewModel.class);
        meetingViewModel.getcurrentMeeting(meetingId)
                .observe(this, meeting -> {

                    long currentUserId = SessionManager.getCurrentUserId(this);

                    // Gastgeber darf nicht bewerten
                    if (meeting.getHost_id() == currentUserId) {
                        btnNewRating.setVisibility(View.INVISIBLE);
                    }
                });


        // LiveData aus der DB beobachten

        if (meetingId != -1) {
            viewModel.getRatingsForMeetingWithUser(meetingId).observe(this, ratings -> {
                for(RatingWithUser r :ratings){

                    if(r.getUserId() ==userId){
                        btnNewRating.setVisibility(View.INVISIBLE);
                    }
                }
                adapter.setData(ratings);


            });
        }

        //Aufruf der Rating Activity
        btnNewRating.setOnClickListener(v -> {
            Intent intent = new Intent(RatingListActivity.this, RatingAtivity.class);
            intent.putExtra("meeting_id", meetingId); //  Meeting-ID weitergeben
            startActivity(intent);
        });

    }



}
