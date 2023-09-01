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
    private RadioGroup radioButtonRdv;

    private boolean showFields = false;
    private Button buttonInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuiladd);

         radioButtonRdv = findViewById(R.id.radioGrouprdv);


        radioButtonRdv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonrdv) {
                    showFields = true;
                    showReferencePopup();
                } else {
                    showFields = false;
                }
            }
        });
//

         buttonInscription = findViewById(R.id.buttonInscription);
        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to be executed when the button is clicked
                Intent intent = new Intent(Acceuiladd.this, Accueiladd2.class);
                intent.putExtra("showFields", showFields);
                startActivity(intent);
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

