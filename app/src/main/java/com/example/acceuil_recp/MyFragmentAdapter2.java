package com.example.acceuil_recp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyFragmentAdapter2 extends FragmentStateAdapter {



    public MyFragmentAdapter2(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new listDesAnnulés();
        } else if (position == 1) {
            return new listDesReports();
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
