package com.example.acceuil_recp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Detail_demanderdv extends AppCompatActivity {



        private String appointmentId;
        private String appointmentReference;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail_demanderdv);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String motif = extras.getString("motif");
                String date = extras.getString("date");
                appointmentId = extras.getString("id");
                Log.e("id de rendez-vous" ,  appointmentId);
                appointmentReference = extras.getString("reference");
                Log.e("reference de rendez-vous" ,  appointmentId);


                Button confirmButton = findViewById(R.id.acceptButton);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showConfirmationDialog();
                    }
                });

                Button rejectButton = findViewById(R.id.cancelButton);
                rejectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRejectionDialog();
                    }
                });
            }
        }

        private void showConfirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Voulez-vous vraiment confirmer ce rendez-vous ?");
            builder.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirmAppointment();
                }
            });
            builder.setNegativeButton("Annuler", null); // Pas d'action si l'utilisateur annule
            builder.show();
        }

        private void showRejectionDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Rejet du rendez-vous");
            builder.setMessage("Voulez-vous vraiment rejeter ce rendez-vous ?");
            builder.setPositiveButton("Rejeter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rejectAppointment();
                }
            });
            builder.setNegativeButton("Annuler", null); // Pas d'action si l'utilisateur annule
            builder.show();
        }

        private void confirmAppointment() {
            OkHttpClient client = new OkHttpClient();

            // Construction de l'URL de l'appel API POST
            String baseUrl = ApiConfig.API_BASE_URL + "/third-party/appointments/" + appointmentId + "/confirm";

            // Construction du corps de la requête JSON
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("reference", appointmentReference);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            // Construction de la requête POST
            Request request = new Request.Builder()
                    .url(baseUrl)
                    .addHeader("x-api-key", "9af3c45c-10a0-43de-b254-ab312036eb95")
                    .post(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()))
                    .build();

            // Exécution de la requête
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Detail_demanderdv.this, "Échec de la confirmation du rendez-vous", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Detail_demanderdv.this, "Rendez-vous confirmé avec succès", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Detail_demanderdv.this, "Échec de la confirmation du rendez-vous", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        private void rejectAppointment() {
            OkHttpClient client = new OkHttpClient();

            // Construction de l'URL de l'appel API POST pour le rejet du rendez-vous
            String baseUrl = ApiConfig.API_BASE_URL + "/third-party/appointments/" + appointmentId + "/cancel";

            // Construction de la requête POST
            Request request = new Request.Builder()
                    .url(baseUrl)
                    .addHeader("x-api-key", "9af3c45c-10a0-43de-b254-ab312036eb95")
                    .post(RequestBody.create(MediaType.parse("application/json"), ""))
                    .build();

            // Exécution de la requête
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Detail_demanderdv.this, "Échec du rejet du rendez-vous", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Detail_demanderdv.this, "Rendez-vous rejeté avec succès", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Detail_demanderdv.this, "Échec du rejet du rendez-vous", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }


