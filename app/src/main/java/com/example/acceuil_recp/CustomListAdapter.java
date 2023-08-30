package com.example.acceuil_recp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CustomListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> dataList;

    public CustomListAdapter(Context context, ArrayList<HashMap<String, String>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.affichagconfirmrdvl, parent, false);

        TextView textViewMotif = rowView.findViewById(R.id.textViewMotif);
        TextView textViewPatientName = rowView.findViewById(R.id.textViewPatientName);
        TextView textViewDateVenue = rowView.findViewById(R.id.textViewDateVenue);
        TextView textViewPriority = rowView.findViewById(R.id.textViewPriority);
        TextView textViewReference = rowView.findViewById(R.id.textViewID);

        CheckBox checkBoxMarkComplete = rowView.findViewById(R.id.checkBoxMarkComplete);

        HashMap<String, String> item = dataList.get(position);

        textViewMotif.setText(item.get("motif"));
        textViewPatientName.setText(item.get("patient"));
        textViewDateVenue.setText(item.get("date"));
        textViewPriority.setText(item.get("priority"));
        textViewReference.setText(item.get("id"));

        // Gestion du CheckBox
        checkBoxMarkComplete.setOnCheckedChangeListener(null); // Important pour éviter des problèmes de recyclage
        checkBoxMarkComplete.setChecked(false); // Réinitialiser l'état du CheckBox

        checkBoxMarkComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Voulez-vous  marquer ce rendez-vous comme complet ?");
                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String appointmentId = item.get("id");
                            String appointmentReference = item.get("reference");
                            markAppointmentAsComplete(item.get("id"));
                        }
                    });
                    builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkBoxMarkComplete.setChecked(false);
                        }
                    });
                    builder.show();
                }
            }
        });

        return rowView;
    }

    private void markAppointmentAsComplete(String appointmentId) {
        OkHttpClient client = new OkHttpClient();

        // Construction de l'URL de l'appel API POST
        String baseUrl = ApiConfig.API_BASE_URL + "/third-party/appointments/" + appointmentId + "/done";

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
                // Gestion de l'échec
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                } else {
                    // Échec : Le rendez-vous n'a pas pu être marqué comme complet
                }
            }
        });
    }

}
