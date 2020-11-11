package com.example.flickr.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.flickr.R;
import com.example.flickr.data.FlickrViewModel;
import com.example.flickr.fragments.FragmentAlbum;
import com.example.flickr.fragments.FragmentComments;
import com.example.flickr.fragments.FragmentHome;
import com.example.flickr.fragments.FragmentPhoto;
import com.example.flickr.fragments.FragmentProfile;
import com.example.flickr.fragments.FragmentSearch;
import com.example.flickr.fragments.FragmentSettings;
import com.example.flickr.fragments.FragmentViewpager;
import com.example.flickr.model.Album;
import com.example.flickr.model.Photo;
import com.example.flickr.services.FlickrBitmapProvider;
import com.example.flickr.services.NetworkChangeReceiver;
import com.example.flickr.utils.AdapterAlbums;
import com.example.flickr.utils.AdapterComments;
import com.example.flickr.utils.AdapterPhotos;
import com.example.flickr.utils.BottomNavigationBarHelper;
import com.example.flickr.utils.ViewpagerAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivityMain extends AppCompatActivity implements
        FragmentHome.AlbumSelectedListener, FragmentAlbum.PhotoSelectedListener {

    private static final int ACTIVITY_NUMBER = 0;
    private static final String TAG = "ActivityMain";
    private Context context = this;
    private FragmentManager fragManager = getSupportFragmentManager();

    private SharedPreferences sharedPreferences;
    private FlickrViewModel flickrViewModel;
    private FrameLayout frameLayoutFragments;
    private NetworkChangeReceiver networkChangeReceiver;

    public FragmentHome fragmentHome;
    public FragmentSearch fragmentSearch;
    public FragmentProfile fragmentProfile;
    private FragmentViewpager fragmentViewPager;
    private FragmentSettings fragmentSettings;

    public Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // End SplashScreen
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateViewModelTask createViewModelTask = new CreateViewModelTask();
        createViewModelTask.setViewModelStoreOwner(this);
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.execute(createViewModelTask);
        FlickrApplication.getBitmapProvider().setExecutor(executor);

        // onCreate() method continues after creating the FlickrViewModel
    }

    private void continueOnCreate() {
        FlickrApplication.setFlickrViewModel(flickrViewModel);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        FlickrApplication.getDataProvider().setSharedPreferences(sharedPreferences);
        FlickrApplication.getDataProvider().setContext(this);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        FlickrApplication.getDataProvider().setConnectivityManager(cm);
        FlickrApplication.getDataProvider().setGson(new Gson());

        frameLayoutFragments = (FrameLayout) findViewById(R.id.frameLayoutFragments);
        View v = findViewById(R.id.relativeLayoutMainContent);
        networkChangeReceiver = new NetworkChangeReceiver();
        Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_SHORT);
        networkChangeReceiver.setSnackbar(snackbar);
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        this.initializeFragments();
        this.setupBottomNavigationBar();
        this.startHomeFragment();
        AdapterAlbums ada = new AdapterAlbums(this);
        fragmentHome.setAdapter(ada);
        networkChangeReceiver.setAdapterAlbums(ada);
        fragmentHome.setOnAlbumSelectedListener(this);
    }

    @Override
    public void onAlbumSelected(Album album) {
        int a = Integer.parseInt(album.getAlbumCount());
        FlickrApplication.getBitmapProvider().setPhotoCount(Integer.parseInt(album.getAlbumCount()));
        Log.d(TAG, "onAlbumSelected: Here the fragment is replaced");
        FragmentAlbum fragmentAlbum = new FragmentAlbum();
        fragmentAlbum.setAlbum(album);
        fragmentAlbum.setSharedPreferences(sharedPreferences);
        fragmentAlbum.setAdapter(new AdapterPhotos(this));
        fragmentAlbum.setAlbumID(album.getAlbumID());
        fragmentAlbum.setOnPhotoSelectedListener(this);

        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.addToBackStack(null);
        fragTransaction.replace(R.id.frameLayoutFragments, fragmentAlbum, "HomeFragment").commit();
    }

    @Override
    public void onPhotoSelected(Photo photo) {
        Log.d(TAG, "onPhotoSelected: Here the fragment is replaced");

        FragmentPhoto fragPhoto = new FragmentPhoto();
        fragPhoto.setFragmentManager(fragManager);
        fragPhoto.setFrameLayout(frameLayoutFragments);
        fragPhoto.setPhoto(photo);
        FlickrApplication.getBitmapProvider().getBitmapFromUrl(photo, fragPhoto);

        //fragPhoto.setBitmap(FlickrBitmapProvider.getBitmapFromUrl(photo, fragPhoto));

        //fragPhoto.setBitmap();
        //FragmentComments fragComments = new FragmentComments();
        //fragComments.setAdapter(new AdapterComments(this));

        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.addToBackStack(null);
        fragTransaction.replace(R.id.frameLayoutFragments, fragPhoto, "PhotoFragment").commit();
    }

    private void initializeFragments() {
        fragmentHome = new FragmentHome();
        fragmentHome.setSharedPreferences(sharedPreferences);
        fragmentSearch = new FragmentSearch();
        fragmentProfile = new FragmentProfile();
        fragmentViewPager = new FragmentViewpager();
        fragmentSettings = new FragmentSettings();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                //Toast.makeText(this, "Preferencias...", Toast.LENGTH_LONG).show();
                FragmentTransaction fragTransaction = fragManager.beginTransaction();
                fragTransaction.addToBackStack(null);
                fragTransaction.replace(R.id.frameLayoutFragments, fragmentSettings,
                        "HomeFragment").commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected class CreateViewModelTask implements Runnable {

        private ViewModelStoreOwner viewModelStoreOwner;
        void setViewModelStoreOwner(ViewModelStoreOwner vm) {
            viewModelStoreOwner = vm;
        }

        @Override
        public void run() {
            flickrViewModel = new ViewModelProvider(viewModelStoreOwner).get(FlickrViewModel.class);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    continueOnCreate();
                }
            });
        }
    }

}
