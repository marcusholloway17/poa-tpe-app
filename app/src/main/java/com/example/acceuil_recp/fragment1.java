package com.example.acceuil_recp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.SimpleAdapter;


import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import okhttp3.ResponseBody;

public class fragment1 extends Fragment {

    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private JSONArray jsonArray;

    private ArrayList<HashMap<String, String>> pendingList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);


        listView = view.findViewById(R.id.list_demande);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        pendingList = new ArrayList<>();// Initialisez ici

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pendingList.clear();



                swipeRefreshLayout.setRefreshing(false);
            }
        });



        ArrayList<HashMap<String, String>> listItem = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        String baseUrl = ApiConfig.API_BASE_URL + "/third-party/webhook";
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
                "                    \"state\": \"pending\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    },\n" +
                "    \"x-api-key\": \"9af3c45c-10a0-43de-b254-ab312036eb95\"\n" +
                "}";


        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(json, mediaType);

        Request request = new Request.Builder()
                .url( baseUrl)
                .post(requestBody)
                .addHeader("x-api-key", "9af3c45c-10a0-43de-b254-ab312036eb95")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("la reponse du webcook ne marche pas", "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("la reponse du webcook", response.body().string());
            }
        });

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final ResponseBody myResponse = response.body();


                    try {
                        JSONArray jsonArray = new JSONArray(myResponse.string());


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String state = object.getString("state");
                            if ("pending".equals(state)) {

                                Log.d("JSON Object", object.toString());
                                HashMap<String, String> map = new HashMap<>();

                                map.put("id", "Référence de rendez-vous: " + object.getString("id"));

                                map.put("motif", "Motif: " + object.getString("appointment_type"));


                                if (object.has("Patient")) {
                                    JSONObject patient = object.getJSONObject("Patient");


                                    Log.d("Patient JSON", patient.toString());

                                      map.put("patient", "Nom et prénom: " + patient.getString("firstname") + " " + patient.getString("lastname"));
                                } else {

                                    Log.d("Missing Data", "No 'Patient' key in JSON object");
                                }

                                map.put("date", "Date de demande: " + object.getString("date_start"));

                                JSONObject medicalPerson = object.getJSONObject("Medical_person");
                                String medecinNom = medicalPerson.getString("firstname");
                                String medecinPrenom = medicalPerson.getString("lastname");

// ...
                                map.put("medecin_nom", medecinNom);
                                map.put("medecin_prenom", medecinPrenom);



                                pendingList.add(map);
                            }
                        }


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                SimpleAdapter aa = new SimpleAdapter(getActivity(), pendingList,
                                        R.layout.affichageitemdeande, new String[]{"id", "motif", "patient", "date", "priority" }, new int[]
                                        {R.id.textViewID, R.id.textViewMotif,R.id.textViewPatientName, R.id.textViewDateVenue, R.id.textViewPriority});
//

                                listView.setAdapter(aa);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        HashMap<String, String> selectedItem = pendingList.get(position);
                                        String appointmentId = selectedItem.get("id").replace("Référence de rendez-vous:","").trim();
                                        Log.e("id de appoitment", appointmentId);
                                        String appointmentReference = "RDV" + appointmentId;

                                        Log.e("reference", appointmentReference);

                                        Intent intent = new Intent(getActivity(), Detail_demanderdv.class);
                                        intent.putExtra("motif", selectedItem.get("motif"));
                                        intent.putExtra("date", selectedItem.get("date"));
                                        intent.putExtra("id", appointmentId);
                                        intent.putExtra("reference", appointmentReference);
                                        intent.putExtra("medecin_nom", selectedItem.get("medecin_nom"));
                                        intent.putExtra("medecin_prenom", selectedItem.get("medecin_prenom"));





                                        startActivity(intent);
                                    }
                                });

                            }
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        SearchView searchview = view.findViewById(R.id.searchview);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterAppointmentsById(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterAppointmentsById(newText);

                return true;
            }
        });
        return view;
    }



    private void filterAppointmentsById(String query) {
        ArrayList<HashMap<String, String>> filteredList = new ArrayList<>();

        for (HashMap<String, String> appointment : pendingList) {
            String appointmentId = appointment.get("id");
            if (appointmentId.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(appointment);
            }
        }


        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                filteredList,
                R.layout.affichageitemdeande,
                new String[]{"id", "motif", "date", "priority"},
                new int[]{R.id.textViewID, R.id.textViewMotif, R.id.textViewDateVenue, R.id.textViewPriority}
        );
        listView.setAdapter(adapter);
    }
}

