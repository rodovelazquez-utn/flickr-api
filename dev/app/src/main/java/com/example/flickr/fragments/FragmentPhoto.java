package com.example.flickr.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.flickr.R;
import com.example.flickr.activities.FlickrApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPhoto extends Fragment {

    private ImageView imageViewPhoto;
    private Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public FragmentPhoto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imageViewPhoto = container.findViewById(R.id.imageViewPhoto);
        //imageViewPhoto.setImageBitmap(bitmap);

        FlickrApplication.getBitmapProvider().getBitmap(getActivity(), imageViewPhoto);
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }
}
