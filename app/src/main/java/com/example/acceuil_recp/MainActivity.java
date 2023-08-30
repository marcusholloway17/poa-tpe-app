package com.example.acceuil_recp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList;
    private OkHttpClient client;

    private FloatingActionButton fabAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        fabAdd = findViewById(R.id.fabb);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Acceuiladd.class);
                startActivity(intent);
            }
        });



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);

                if(id == R.id.bottom_search){
                    Intent intent = new Intent(MainActivity.this, RendezVousAnnulActivity.class);
                    startActivity(intent);
                    finish();

                } else if (id == R.id.bottom_ticket) {

                    Intent intent = new Intent(MainActivity.this, GestionPlanning.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.bottom_profile) {
                    Intent intent = new Intent(MainActivity.this,rendez_vous.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        listView = findViewById(R.id.list_acceuil);

        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap
                <String, String>>();
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.vaccitest.lik.tg/api/points";

        Request request = new Request.Builder()
                .url(url)
                .build();
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
                        JSONObject jsonobject = new JSONObject(myResponse.string());

                        JSONObject items = jsonobject.getJSONObject("items");
                        JSONArray data = items.getJSONArray("data");
                        HashMap<String, String> map = new HashMap<String, String>();
                      //  Log.e("cccc",  data.toString());
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                           // Log.e("testt",  object.toString());
                            map = new HashMap<String, String>();
                            map.put("titre", object.getString("label"));
                            map.put("description" , object.getString("slug"));
                            //   map.put("img", String.valueOf(R.drawable.excel));
                            listItem.add(map);

                            //do something with i
                        }
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                // Récupérer les données de l'élément sélectionné
                                HashMap<String, String> selectedItem = (HashMap<String, String>) parent.getItemAtPosition(position);

                              try {

                                    JSONObject object = data.getJSONObject(position);
                                    Intent intent = new Intent(MainActivity.this, Detail_demanderdv.class);
                                    intent.putExtra("titre", object.getString("label"));
                                    intent.putExtra("description", object.getString("slug") );
                                    startActivity(intent);

                            } catch (JSONException e) {
                                  throw new RuntimeException(e);
                              }
                            }
                        });

                       MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                SimpleAdapter aa = new SimpleAdapter (MainActivity.this, listItem,
                                        R.layout.afficheitemacceuil, new String[] { "titre", "description"}, new int[]
                                        { R.id.textViewPatientName, R.id.textViewPatientName});

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




