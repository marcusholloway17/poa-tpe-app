package com.example.acceuil_recp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.tabs.TabLayout;


public class RendezVousAnnulActivity extends AppCompatActivity {



    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private MyFragmentAdapter2 adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patients);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setTitle("       Rendez-vous       ");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);

        tabLayout.addTab(tabLayout.newTab().setText("Liste des rendez-vous annulés"));
        tabLayout.addTab(tabLayout.newTab().setText("Liste des rendez-vous reportés"));


        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new MyFragmentAdapter2(fragmentManager , getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent first_intent = new Intent(getApplication(), MainActivity.class);
            startActivity(first_intent);
        }
        return  super.onOptionsItemSelected(item);
    }

}




