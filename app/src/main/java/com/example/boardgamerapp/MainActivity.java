package com.example.boardgamerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.boardgamerapp.featureEvaluate.Evaluate;

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
                startActivity(new Intent(MainActivity.this, Evaluate.class)));


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