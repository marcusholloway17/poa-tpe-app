package com.example.acceuil_recp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class filleattente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filleattente);

        Button enregistrerButton = findViewById(R.id.buttonInscription);
        enregistrerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Afficher le custom dialog
                FragmentManager fragmentManager = getSupportFragmentManager();
                CustomDialogFragment dialog = new CustomDialogFragment();
                dialog.show(fragmentManager, "CustomDialog");
            }
        });
    }
}
