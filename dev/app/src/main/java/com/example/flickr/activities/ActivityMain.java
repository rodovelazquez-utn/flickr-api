package com.example.flickr.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.flickr.R;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // End SplashScreen
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
