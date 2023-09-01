package com.example.acceuil_recp;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.HttpUrl;

public class fragment1 extends Fragment {

    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private JSONArray jsonArray;

    private ArrayList<HashMap<String, String>> pendingList; // Renommé en minuscules


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
                updateListViewData();

                // Arrêtez l'indicateur de rafraîchissement
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        updateListViewData();

        ArrayList<HashMap<String, String>> listItem = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        String webhookUrl = "http://192.168.5.80:3000/third-party/webhook";

        //   Log.e("JSON", String.valueOf(requestBody));


        //
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            List<KeyValuePair<String, Object>> includeList = new ArrayList<>();
//            includeList.add(new KeyValuePair<>("model", "Medical_person"));
//            includeList.add(new KeyValuePair<>("include", new String[]{"Speciality"}));
//
//            List<KeyValuePair<String, Object>> whereList = new ArrayList<>();
//            whereList.add(new KeyValuePair<>("state", "pending"));
//
//            List<KeyValuePair<String, Object>> orderList = new ArrayList<>();
//            orderList.add(new KeyValuePair<>("createdAt", "DESC"));
//
//            QueryObject<KeyValuePair<String, Object>> queryObject = new QueryObject<>();
//            queryObject.setInclude(includeList);
//            queryObject.setWhere(whereList);
//            queryObject.setOrder(orderList);
//
//            String jsonBody = objectMapper.writeValueAsString(queryObject);
//
//
//            // build _queryobject
//            ObjectNode _queryObject = objectMapper.createObjectNode();
//            _queryObject.put("_query", jsonBody);
//
//            // build _jsonBodyBodyobject
//            ObjectNode _jsonBodyBodyobject = objectMapper.createObjectNode();
//            _jsonBodyBodyobject.put("data", _queryObject);
//
//            // build global request body
//            ObjectNode _globalRequestBody = objectMapper.createObjectNode();
//            _globalRequestBody.put("url", "http://192.168.5.80:3000/third-party/appointments");
//            _globalRequestBody.put("x-api-key", "9af3c45c-10a0-43de-b254-ab312036eb95");
//            _globalRequestBody.put("data", _jsonBodyBodyobject);


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
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    },\n" +
                "    \"x-api-key\": \"9af3c45c-10a0-43de-b254-ab312036eb95\"\n" +
                "}";


//            MediaType mediaType = MediaType.parse("application/json");
//            RequestBody requestBody = RequestBody.create(json, mediaType);
//
//
//            request = new Request.Builder()
//                    .url(webhookUrl)
//                    .addHeader("x-api-key", "9af3c45c-10a0-43de-b254-ab312036eb95")
//                    .addHeader("Content-Type", "application/json")
//                    .post(RequestBody.create(MediaType.parse("application/json"), requestBody))
//                    .build();


        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(json, mediaType);

        Request request = new Request.Builder()
                .url("http://192.168.5.80:3000/third-party/webhook")
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
                Log.e("la reponse du webcook", String.valueOf(response));
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

                                    //   map.put("patient", "Nom et prénom: " + patient.getString("firstname") + " " + patient.getString("lastname"));
                                } else {

                                    Log.d("Missing Data", "No 'Patient' key in JSON object");
                                }

                                map.put("date", "Date de demande: " + object.getString("date_start"));
                                if (object.has("Priority")) {
                                    map.put("priority", object.getJSONObject("Priority").getString("name"));
                                }
                                pendingList.add(map);
                            }
                        }


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                SimpleAdapter aa = new SimpleAdapter(getActivity(), pendingList,
                                        R.layout.affichageitemdeande, new String[]{"id", "titre", "description", "priority"}, new int[]
                                        {R.id.textViewID, R.id.textViewMotif, R.id.textViewDateVenue, R.id.textViewPriority});
//                                aa.setViewBinder(new SimpleAdapter.ViewBinder() {
//                                    @Override
//                                    public boolean setViewValue(View view, Object data, String textRepresentation) {
//                                        if (view.getId() == R.id.textViewPriority) {
//                                            TextView priorityTextView = (TextView) view;
//                                            priorityTextView.setText(textRepresentation);
//
//                                          //  GradientDrawable priorityIndicator = (GradientDrawable) priorityTextView.getBackground();
////                                            if ("elevee".equals(textRepresentation)) {
////                                                priorityIndicator.setColor(getResources().getColor(R.color.priority_high));
////                                            } else if ("normale".equals(textRepresentation)) {
////                                                priorityIndicator.setColor(getResources().getColor(R.color.priority_normal));
////                                            } else {
////                                                priorityIndicator.setColor(getResources().getColor(R.color.priority_low));
////                                            }
//
//                                            return true;
//                                        }
//                                        return false;
//                                    }
//                                });

                                listView.setAdapter(aa);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        HashMap<String, String> selectedItem = pendingList.get(position);
                                        String appointmentId = selectedItem.get("id");
                                        Log.e("id de appoitment", appointmentId);
                                        String appointmentReference = "RDV" + appointmentId;

                                        Log.e("reference", appointmentReference);

                                        Intent intent = new Intent(getActivity(), Detail_demanderdv.class);
                                        intent.putExtra("motif", selectedItem.get("motif"));
                                        intent.putExtra("date", selectedItem.get("date"));
                                        intent.putExtra("id", appointmentId);
                                        intent.putExtra("reference", appointmentReference);

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
                // Cette méthode est appelée à chaque fois que le texte dans le champ de recherche change
                // Vous pouvez également déclencher la recherche ici si vous souhaitez une mise à jour en temps réel
                // Par exemple, vous pouvez appeler searchAppointmentById(newText) ici

                // Filtrez les rendez-vous en attente par ID
                filterAppointmentsById(newText);

                return true;
            }
        });
        return view;
    }

    private void updateListViewData() {

            pendingList.clear();

            OkHttpClient client = new OkHttpClient();
            String baseUrl = ApiConfig.API_BASE_URL + "/third-party/appointments";

            JSONObject query = new JSONObject();
            JSONObject includeObject1 = new JSONObject();
            try {
                JSONObject whereObject = new JSONObject();
                whereObject.put("state", "pending");
                query.put("where", whereObject);

//                includeObject1.put("model", "Medical_person");
//                includeObject1.put("include", new String[]{"Speciality"});
//                query.put("include", new String[]{"Patient", String.valueOf(includeObject1), "Priority"});



                JSONArray orderArray = new JSONArray();
                JSONArray orderInnerArray = new JSONArray();
                orderInnerArray.put("createdAt");
                orderInnerArray.put("DESC");
                orderArray.put(orderInnerArray);
                query.put("order", orderArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        Request request;
        try {
            String queryParam = URLEncoder.encode(query.toString(), "UTF-8");
            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
            urlBuilder.addQueryParameter("_query", queryParam);
            String url = urlBuilder.build().toString();

            request = new Request.Builder()
                    .url(url)
                    .addHeader("x-api-key", "9af3c45c-10a0-43de-b254-ab312036eb95")
                    .build();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
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

                            Log.d("JSON Object", object.toString());
                            String state = object.getString("state");
                            if ("pending".equals(state)) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("motif", "Motif: " + object.getString("appointment_type"));
                                map.put("id", "Référence de rendez-vous: " +object.getString("id"));
                                map.put("date", " Date de demande: "+ object.getString("date_start"));

                                pendingList.add(map);
                            }
                        }

                        // Mettre à jour l'UI sur le thread principal
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SimpleAdapter aa = new SimpleAdapter(getActivity(), pendingList,
                                        R.layout.affichageitemdeande, new String[]{"id","motif", "date", "priority"}, new int[]
                                        {R.id.textViewID,R.id.textViewMotif, R.id.textViewDateVenue, R.id.textViewPriority});
                                listView.setAdapter(aa);
                            }
                        });

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    private void filterAppointmentsById(String query) {
        ArrayList<HashMap<String, String>> filteredList = new ArrayList<>();

        for (HashMap<String, String> appointment : pendingList) {
            String appointmentId = appointment.get("id");
            if (appointmentId.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(appointment);
            }
        }

        // Mettez à jour la liste affichée avec la nouvelle liste filtrée
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

