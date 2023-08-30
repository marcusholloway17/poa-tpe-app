package com.example.acceuil_recp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class GestionPlanning extends AppCompatActivity {

    private String[] daysOfWeek = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String[] times = {"8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00",
            "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00",
            "17:30"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_planning);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle("Planning Medical");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        for (int i = 0; i <= times.length; i++) {
            TableRow row = new TableRow(this);

            for (int j = 0; j <= daysOfWeek.length; j++) {
                TextView textView = new TextView(this);

                if (i == 0 && j == 0) {
                    // Add an empty cell at the top-left corner
                    textView.setText("");
                } else if (i == 0) {
                    // Populate days of the week in the first row
                    textView.setText(daysOfWeek[j - 1]);
                } else if (j == 0) {
                    // Populate time slots in the first column
                    textView.setText(times[i - 1]);
                } else {
                    // Add your logic to populate timetable cells here
                    // For example: textView.setText("Course");
                }

                // Customize text appearance and padding if needed
                textView.setPadding(16, 16, 16, 16);
                row.addView(textView);
            }

            tableLayout.addView(row);
        }



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent first_intent = new Intent(getApplication(), MainActivity.class);
            startActivity(first_intent);
        }
        return  super.onOptionsItemSelected(item);
    }



}