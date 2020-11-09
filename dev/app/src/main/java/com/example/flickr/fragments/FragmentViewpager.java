package com.example.flickr.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flickr.R;
import com.example.flickr.utils.ViewpagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentViewpager extends Fragment {

    private static final String TAG = "FragmentViewpager";

    private FragmentPhoto fragmentPhoto;
    private FragmentComments fragmentComments;
    private String albumID;
    private String photoID;

    private ViewPager viewPager;
    private ViewpagerAdapter viewPagerAdapter;
    private FragmentManager fragManager;
    private Bitmap bitmap;

    public void setFragmentPhoto(FragmentPhoto fragmentPhoto) {
        this.fragmentPhoto = fragmentPhoto;
    }

    public void setFragmentComments(FragmentComments fragmentComments) {
        this.fragmentComments = fragmentComments;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public void setAdapter(ViewpagerAdapter viewPagerAdapter) {
        this.viewPagerAdapter = viewPagerAdapter;
    }

    public void setFragManager(FragmentManager fm){
        this.fragManager = fm;
    }

     public void setBitmap(Bitmap b) {
        this.bitmap = b;
     }

    public FragmentViewpager() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_viewpager, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPagerPhotos);
        //viewPagerAdapter = new ViewpagerAdapter(fragManager); DONE IN ACTIVITY
        //viewPagerAdapter.setBitmap(bitmap);
        viewPager.setAdapter(viewPagerAdapter);
    }
}
