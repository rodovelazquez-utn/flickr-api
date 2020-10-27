package com.example.flickr.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.flickr.R;
import com.example.flickr.data.FlickrViewModel;
import com.example.flickr.fragments.FragmentHome;
import com.example.flickr.fragments.FragmentProfile;
import com.example.flickr.fragments.FragmentSearch;
import com.example.flickr.model.Album;
import com.example.flickr.utils.BottomNavigationBarHelper;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ActivityMain extends AppCompatActivity implements FragmentHome.AlbumSelectedListener {

    private static final int ACTIVITY_NUMBER = 0;
    private static final String TAG = "ActivityMain";
    private Context context = this;
    private FragmentManager fragManager = getSupportFragmentManager();

    //private FlickrViewModel flickrViewModel;
    private FrameLayout frameLayoutFragments;

    public FragmentHome fragmentHome;
    public FragmentSearch fragmentSearch;
    public FragmentProfile fragmentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // End SplashScreen
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlickrApplication.setViewModelStoreOwner(this);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        FlickrApplication.getDataProvider().setConnectivityManager(cm);
        FlickrApplication.getDataProvider().setGson(new Gson());

        frameLayoutFragments = (FrameLayout) findViewById(R.id.frameLayoutFragments);
        this.initializeFragments();
        this.setupBottomNavigationBar();
        this.startHomeFragment();
    }

    @Override
    public void onAlbumSelected(Album album) {
        // TODO: Replace HomeFragment with PhotoFragment
        Log.d(TAG, "onAlbumSelected: Here the fragment is replaced");
    }

    private void initializeFragments() {
        fragmentHome = new FragmentHome();
        fragmentHome.setOnAlbumSelectedListener(this);
        fragmentSearch = new FragmentSearch();
        fragmentProfile = new FragmentProfile();
    }

    private void setupBottomNavigationBar() {
        BottomNavigationViewEx bottomNavigationViewEx =
                (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);

        bottomNavigationViewEx.setCurrentItem(0); // First one to open is Home
        BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavigationViewEx);
        BottomNavigationBarHelper.enableNavigation(fragManager, bottomNavigationViewEx,
                fragmentHome, fragmentSearch, fragmentProfile);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }

    private void startHomeFragment() {
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        // fragTransaction.addToBackStack(null);
        fragTransaction.add(R.id.frameLayoutFragments, fragmentHome, "HomeFragment").commit();
    }

}
