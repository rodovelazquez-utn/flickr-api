package com.example.flickr.utils;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.flickr.R;
import com.example.flickr.fragments.FragmentHome;
import com.example.flickr.fragments.FragmentProfile;
import com.example.flickr.fragments.FragmentSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationBarHelper {

    public static void setupBottomNavigationBar(BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final FragmentManager fragManager, BottomNavigationViewEx view,
                                        final FragmentHome fh, final FragmentSearch fs, final FragmentProfile fp){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.iconHome:
                        FragmentTransaction fragTransactionHome = fragManager.beginTransaction();
                        //fragTransactionHome.addToBackStack(null);
                        fragTransactionHome.replace(R.id.frameLayoutFragments, fh, "HomeFragment").commit();
                        break;

                    case R.id.iconSearch:
                        FragmentTransaction fragTransactionSearch = fragManager.beginTransaction();
                        //fragTransactionSearch.addToBackStack(null);
                        fragTransactionSearch.replace(R.id.frameLayoutFragments, fs, "HomeFragment").commit();
                        break;

                    case R.id.iconProfile:
                        FragmentTransaction fragTransactionProfile = fragManager.beginTransaction();
                        //fragTransactionProfile.addToBackStack(null);
                        fragTransactionProfile.replace(R.id.frameLayoutFragments, fp, "HomeFragment").commit();
                        break;
                }

                return false;
            }
        });
    }

}
