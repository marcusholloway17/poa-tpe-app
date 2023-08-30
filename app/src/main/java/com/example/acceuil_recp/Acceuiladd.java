package com.example.acceuil_recp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Acceuiladd extends AppCompatActivity {
    private RadioGroup radioGroupAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuiladd);

        RadioButton radioButtonRdv = findViewById(R.id.radioButtonrdv);
        RadioButton radioButtonPatient = findViewById(R.id.radioButtonPatient);
        EditText spinnerPrenomPatient = findViewById(R.id.spinnerPrépatient);

        radioButtonRdv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showReferencePopup();
                }
            }
        });

        radioButtonPatient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinnerPrenomPatient.setEnabled(!isChecked);
            }
        });

        Button buttonInscription = findViewById(R.id.buttonInscription);
        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to be executed when the button is clicked
                Intent intent = new Intent(Acceuiladd.this, filleattente.class);
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
       

   

   
    private void showReferencePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Saisir la référence");

        // Ajouter un champ de texte au popup
        EditText editTextReference = new EditText(this);
        editTextReference.setHint("Saisir la référence");
        builder.setView(editTextReference);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtenir la valeur saisie dans le champ de texte
                String reference = editTextReference.getText().toString();
                // Faire quelque chose avec la référence (par exemple, l'enregistrer)
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}

