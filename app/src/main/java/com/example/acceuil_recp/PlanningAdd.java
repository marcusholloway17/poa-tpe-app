package com.example.acceuil_recp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class PlanningAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_add);

        Intent intent = getIntent();
        if (intent != null) {
            String selectedTimeSlot = intent.getStringExtra("selectedTimeSlot");
            // Utilisez la plage horaire sélectionnée pour pré-remplir le formulaire
            // ...
        }
    }
}