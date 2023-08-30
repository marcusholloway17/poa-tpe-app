package com.example.acceuil_recp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import okhttp3.OkHttpClient;

public class listDesAnnulés extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList;
    private OkHttpClient client;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_patient, container, false);

//
//        listView = view.findViewById(R.id.list_patient);
//
//        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap
//                <String, String>>();
//        OkHttpClient client = new OkHttpClient();
//        String url = "https://api.vaccitest.lik.tg/api/points";
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    final ResponseBody myResponse = response.body();
//
//
//                    try {
//                        JSONObject jsonobject = new JSONObject(myResponse.string());
//
//                        JSONObject items = jsonobject.getJSONObject("items");
//                        JSONArray data = items.getJSONArray("data");
//                        HashMap<String, String> map = new HashMap<String, String>();
//                        //  Log.e("cccc",  data.toString());
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject object = data.getJSONObject(i);
//                            // Log.e("testt",  object.toString());
//                            map = new HashMap<String, String>();
//                            map.put("titre", object.getString("label"));
//                            map.put("description" , object.getString("slug"));
//                            //   map.put("img", String.valueOf(R.drawable.excel));
//                            listItem.add(map);
//
//                            //do something with i
//                        }
//                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                                // Récupérer les données de l'élément sélectionné
//                                HashMap<String, String> selectedItem = (HashMap<String, String>) parent.getItemAtPosition(position);
//
//                                try {
//
//                                    JSONObject object = data.getJSONObject(position);
//                                    Intent intent = new Intent(getActivity(), Detail_demanderdv.class);
//                                    intent.putExtra("titre", object.getString("label"));
//                                    intent.putExtra("description", object.getString("slug") );
//                                    startActivity(intent);
//
//                                } catch (JSONException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//                        });
//
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                SimpleAdapter aa = new SimpleAdapter (getActivity(), listItem,
//                                        R.layout.affichageacceuil, new String[] { "titre", "description"}, new int[]
//                                        { R.id.titre, R.id.description});
//
//                                listView.setAdapter(aa);
//
//
//                            }
//                        });
//
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                }
//
//            }
//
//        });
        return view;

    }
}