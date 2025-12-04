package de.iu.boardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import de.iu.boardgame.R;
import de.iu.boardgame.featureEvaluate.Evaluate;

import de.iu.boardgame.feature_termine.MeetingCreateForm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdministration = findViewById(R.id.btnAdministration);
        Button btnAppointment = findViewById(R.id.btnAppointment);
        Button btnEvaluate = findViewById(R.id.btnEvaluate);
        Button btnUser = findViewById(R.id.btnUser);
        Button btnVoting = findViewById(R.id.btnVoting);
        Button btnFoodAdministration = findViewById(R.id.btnFoodAdministration);



        //Activity aufrufen


        btnEvaluate.setOnClickListener(v ->
                startActivity(new Intent(de.iu.boardgame.MainActivity.this, Evaluate.class)));


/*        btnAdministration.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity1.class)));

        btnAppointment.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity2.class)));



        btnAppointment.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity4.class)));

        btnUser.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity4.class)));

        btnVoting.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity4.class)));

        btnFoodAdministration.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Activity4.class)));*/


    }
}