package com.example.flickr.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flickr.R;
import com.example.flickr.activities.FlickrApplication;
import com.example.flickr.model.Album;
import com.example.flickr.utils.AdapterAlbums;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {

    private static final String TAG = "FragmentHome";

    private RecyclerView recyclerView;
    private AdapterAlbums adapter;
    private GridLayoutManager layoutManager;

    public void setAdapter(AdapterAlbums ad) {
        adapter = ad;
    }

    public FragmentHome() {
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        rootView.setTag(TAG);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewAlbumes);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        this.setRecyclerViewLayoutManager();

        // adapter = new AdapterAlbums(getActivity());
        recyclerView.setAdapter(adapter);

        FlickrApplication.getViewModel().getAllAlbums().observe(getActivity(), new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                adapter.setAlbums(albums);
            }
        });

        FlickrApplication.getDataProvider().loadFlickrAlbums(adapter);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setOnAlbumSelectedListener(AlbumSelectedListener listener){
        adapter.setOnAlbumSelectedListener(listener);
    }

    private void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        layoutManager = new GridLayoutManager(getActivity(), 3);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    public interface AlbumSelectedListener {
        void onAlbumSelected(Album album);
    }
}
