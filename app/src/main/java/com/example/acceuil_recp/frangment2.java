package com.example.acceuil_recp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class frangment2 extends Fragment {

    private ListView listView;
    private JSONArray jsonArray;
    private ArrayList<HashMap<String, String>> ConfirmedList;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_frangment2, container, false);

        listView = view.findViewById(R.id.list_rendezvous);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout); // Initialisez ici

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateListViewData();

                // Créez un nouvel adaptateur avec les données mises à jour
                CustomListAdapter updatedAdapter = new CustomListAdapter(getActivity(), ConfirmedList);

                listView.setAdapter(updatedAdapter);

                // Arrêtez l'indicateur de rafraîchissement
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        ArrayList<HashMap<String, String>> listItem = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        String baseUrl = ApiConfig.API_BASE_URL + "/third-party/appointments";

        JSONObject query = new JSONObject();
        JSONObject includeObject1 = new JSONObject();
        try {
            includeObject1.put("model", "Medical_person");
            includeObject1.put("include", new String[]{"Speciality"});
            query.put("include", new String[]{"Patient", String.valueOf(includeObject1), "Priority"});

            JSONObject whereObject = new JSONObject();
            whereObject.put("state", "confirmed");
            query.put("where", new JSONObject[]{whereObject});

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
                        jsonArray = new JSONArray(myResponse.string()); // Affectez jsonArray ici

                        ConfirmedList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            Log.d("JSON Object", object.toString());
                            String state = object.getString("state");
                            if ("confirmed".equals(state)) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("motif", "Motif: " + object.getString("appointment_type"));
                                map.put("id", "Référence de rendez-vous: " + object.getString("id"));



//                                if (object.has("Patient")) {
//                                    JSONObject patient = object.getJSONObject("Patient");
//
//
//                                    Log.d("Patient JSON", patient.toString());
//
//                                    map.put("patient", "Nom et prénom: " + patient.getString("firstname") + " " + patient.getString("lastname"));
//                                } else {
//
//                                    Log.d("Missing Data", "No 'Patient' key in JSON object");
//                                }

                                map.put("date", "Rendez-vous prévu pour : " + object.getString("date_start"));
                                if (object.has("Priority")) {
                                    map.put("priority", object.getJSONObject("Priority").getString("name"));
                                }
                                ConfirmedList.add(map);
                            }
                        }


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                CustomListAdapter aa = new CustomListAdapter(getActivity(), ConfirmedList);

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
                                        HashMap<String, String> selectedItem = ConfirmedList.get(position);
                                        try {
                                            JSONObject object = jsonArray.getJSONObject(position);
                                            String appointmentId = object.getString("id");
                                            //  String appointmentReference = "RDV" + appointmentId;
                                            Intent intent = new Intent(getActivity(), Detail_demanderdv.class);
                                            intent.putExtra("motif", object.getString("appointment_type"));
                                            //       intent.putExtra("patient", object.getJSONObject("Patient").getString("firstname") + " " + object.getJSONObject("Patient").getString("lastname"));
                                            intent.putExtra("date", object.getString("date_start"));
                                            intent.putExtra("id", appointmentId);
                                            intent.putExtra("reference", object.getString("id")); // Passer la référence du rendez-vous

                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
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


        // Inflate the layout for this fragment
        return view;
    }
    private void updateListViewData() {
        ConfirmedList.clear();

        OkHttpClient client = new OkHttpClient();
        String baseUrl = ApiConfig.API_BASE_URL + "/third-party/appointments";

        JSONObject query = new JSONObject();
        JSONObject includeObject1 = new JSONObject();
        try {
            includeObject1.put("model", "Medical_person");
            includeObject1.put("include", new String[]{"Speciality"});
            query.put("include", new String[]{"Patient", String.valueOf(includeObject1), "Priority"});

            JSONObject whereObject = new JSONObject();
            whereObject.put("state", "confirmed");
            query.put("where", new JSONObject[]{whereObject});

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
                            if ("confirmed".equals(state)) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("motif", "Motif: " + object.getString("appointment_type"));
                                map.put("id", object.getString("id"));
                                map.put("date","Rendez-vous prévu pour : " + object.getString("date_start"));



                                ConfirmedList.add(map);
                            }
                        }

                        // Mettre à jour l'UI sur le thread principal
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Mettre à jour l'adaptateur de la liste avec les nouvelles données
                                CustomListAdapter aa = new CustomListAdapter(getActivity(), ConfirmedList);
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
}
