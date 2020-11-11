package com.example.flickr.utils;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.flickr.fragments.FragmentComments;
import com.example.flickr.fragments.FragmentPhoto;

public class ViewpagerAdapter extends FragmentPagerAdapter {

    private Bitmap bitmap;

    public void setBitmap(Bitmap b) {
        this.bitmap = b;
    }

    public ViewpagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        if (position == 0) {
            FragmentPhoto fp = new FragmentPhoto();
            //fp.setBitmap(bitmap);
            bundle.putString("message", "Photo");
            fp.setArguments(bundle);
            return fp;
        }
        else if (position == 1) {
            FragmentComments fc = new FragmentComments();
            bundle.putString("message", "Comments");
            fc.setArguments(bundle);
            return fc;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
