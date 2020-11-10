package com.example.flickr.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.flickr.R;

public class FragmentSettings extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
