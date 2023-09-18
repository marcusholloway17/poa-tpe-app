package com.example.acceuil_recp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        final EditText editTextReference = new EditText(this);
        editTextReference.setHint("Saisir la référence");
        builder.setView(editTextReference);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtenir la valeur saisie dans le champ de texte
                String reference = editTextReference.getText().toString();

                // Construire dynamiquement le JSON en remplaçant la référence
                String json = "{\n" +
                        "    \"url\": \"http://localhost:3000/third-party/appointments\",\n" +
                        "    \"data\": {\n" +
                        "        \"_query\": {\n" +
                        "            \"include\": [\n" +
                        "                \"Patient\",\n" +
                        "                {\n" +
                        "                    \"model\": \"Medical_person\",\n" +
                        "                    \"include\": [\n" +
                        "                        \"Speciality\"\n" +
                        "                    ]\n" +
                        "                },\n" +
                        "                \"Priority\"\n" +
                        "            ],\n" +
                        "            \"where\": [\n" +
                        "                {\n" +
                        "                    \"state\": \"confirmed\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                    \"reference\": \"" + reference + "\"\n" +
                        "                }\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"x-api-key\": \"9af3c45c-10a0-43de-b254-ab312036eb95\"\n" +
                        "}";


                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                String postUrl = "http://192.168.5.136:3000/third-party/webhook";

                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(postUrl)
                        .addHeader("x-api-key", "9af3c45c-10a0-43de-b254-ab312036eb95")
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Erreur lors de la requête POST", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseBody = response.body().string();
                        ResponseManager.getInstance().setResponse(responseBody);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONArray jsonArray = new JSONArray(responseBody);
                                    if (jsonArray.length() > 0) {
                                        Toast.makeText(getApplicationContext(), "Référence trouvée", Toast.LENGTH_SHORT).show();
                                        ResponseManager.getInstance().setReferenceFound(true);


                                    } else {
                                        // Vous pouvez afficher un message à l'utilisateur
                                        Toast.makeText(getApplicationContext(), "Référence incorrecte : Aucun élément trouvé", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    // Gérer les erreurs de parsing JSON ici
                                    Toast.makeText(getApplicationContext(), "Erreur de parsing JSON", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                });
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


    private void showResponseContent(String responseContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contenu de la réponse");
        builder.setMessage(responseContent);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
