package com.example.acceuil_recp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class Accueiladd2 extends AppCompatActivity {
    private RadioGroup radioGroupAcc;
    private Spinner spinnermotif;
    private EditText editTextMotif;
    private Button buttonInscription;
    private LinearLayout layoutMotif , layoutPatient ,  layoutAutre;
    private RadioGroup radioGroupPatientType;
    private RadioButton radioButtonPatient;
    private RadioButton radioButtonOther;
    private Spinner spinnerPatient;
    private EditText editTextNomPrenom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueiladd2);

        spinnermotif = findViewById(R.id.spinnermotif);
        layoutMotif = findViewById(R.id.layout_motif);
        layoutPatient=findViewById(R.id.layout_patient);
        layoutAutre=findViewById(R.id.layout_autre);
        radioGroupPatientType = findViewById(R.id.radioGroupPatientType);
        radioButtonPatient = findViewById(R.id.radioButtonPatient);
        radioButtonOther = findViewById(R.id.radioButtonOther);
        spinnerPatient = findViewById(R.id.spinnerPatient);
        editTextNomPrenom = findViewById(R.id.spinnerPrépatient);


        boolean showFields = getIntent().getBooleanExtra("showFields", false);
        if (showFields) {
            layoutMotif.setVisibility(View.GONE);
        }



        radioGroupPatientType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonPatient) {
                    layoutPatient.setVisibility(View.VISIBLE);
                    layoutAutre.setVisibility(View.GONE);


                } else {
                    layoutPatient.setVisibility(View.GONE);
                    layoutAutre.setVisibility(View.VISIBLE);

                }
            }
        });

        buttonInscription = findViewById(R.id.buttonInscription);
        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to be executed when the button is clicked
                Intent intent = new Intent(Accueiladd2.this, filleattente.class);
                startActivity(intent);
            }
        });

         radioGroupAcc = findViewById(R.id.radioGroupacc);
        RadioButton radioOui = findViewById(R.id.radiooui);

        radioOui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment2 dialogFragment = new CustomDialogFragment2();
                dialogFragment.show(getSupportFragmentManager(), "CustomDialog");
            }
        });
    }
}