package com.example.flickr.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.flickr.model.Photo;
import com.example.flickr.utils.AdapterPhotos;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAlbum extends Fragment {

    private static final String TAG = "FragmentHome";

    private RecyclerView recyclerView;
    private AdapterPhotos adapter;
    private GridLayoutManager layoutManager;
    private String albumID;
    private SharedPreferences sharedPreferences;
    private Album album;
    private boolean thumbnailsSearched;

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setThumbnailsSearched(boolean thumbnailsSearched) {
        this.thumbnailsSearched = thumbnailsSearched;
    }

    public interface PhotoSelectedListener {
        void onPhotoSelected(Photo photo);
    }

    public void setAlbumID(String id) {
        albumID = id;
    }

    public void setAdapter(AdapterPhotos adapter) {
        this.adapter = adapter;
    }

    public FragmentAlbum() {
        thumbnailsSearched = false;
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
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);
        rootView.setTag(TAG);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewPhotos);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        this.setRecyclerViewLayoutManager();

        // adapter = new AdapterAlbums(getActivity());
        recyclerView.setAdapter(adapter);

        if (sharedPreferences.getString("order_field", "id").equals("name_asc")){
            FlickrApplication.getViewModel().getPhotosWhereAlbumIdOrderTitle(albumID).
                    observe(getActivity(), new Observer<List<Photo>>() {
                @Override
                public void onChanged(List<Photo> photos) {
                    adapter.setPhotos(photos);
                    if (!(photos.size() < Integer.parseInt(album.getAlbumCount()))
                            && !adapter.getThumbnailsReceived()) {
                        buscarThumbnails(photos);
                        //adapter.setThumbnails(bitmaps);
                        //adapter.setHasImagesToShow(true);
                    }
                    if (adapter.getThumbnailsReceived()) {
                        adapter.searchPhotosThumbnails();
                    }
                }
            });
        }
        else {
            FlickrApplication.getViewModel().getPhotosWhereAlbumId(albumID).
                    observe(getActivity(), new Observer<List<Photo>>() {
                @Override
                public void onChanged(List<Photo> photos) {
                    adapter.setPhotos(photos);
                    if (!(photos.size() < Integer.parseInt(album.getAlbumCount()))
                            && !adapter.getThumbnailsReceived()) {
                        buscarThumbnails(photos);
                        //FlickrApplication.getBitmapProvider().getPhotosThumbnailsFromAPI(photos, sharedPreferences, adapter);
                        //adapter.setThumbnails(bitmaps);
                        //adapter.setHasImagesToShow(true);
                    }
                    if (adapter.getThumbnailsReceived()) {
                        adapter.searchPhotosThumbnails();
                    }
                }
            });
        }


        /*if (adapter.getPhotos() == null){
            // TODO: NO HAY FOTOS
        }*/
        //String albumId = adapter.getPhotos().get(0).getAlbumID();
        FlickrApplication.getDataProvider().loadFlickrPhotos(adapter, albumID);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void buscarThumbnails(List<Photo> photos) {
        if (!thumbnailsSearched) {
            FlickrApplication.getBitmapProvider().getPhotoThumbnails(photos, sharedPreferences,
                    adapter, Integer.parseInt(album.getAlbumCount()));
            thumbnailsSearched = true;
        }
        if (adapter.getThumbnailsReceived() && adapter.getHasImagesToShow()) {
            adapter.searchPhotosThumbnails();
        }
    }

    public void setOnPhotoSelectedListener(PhotoSelectedListener listener){
        adapter.setOnPhotoSelectedListener(listener);
    }

    private void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        layoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    /*@Override
    public void onStop() {
        super.onStop();
        adapter.setPhotos(null);
    }*/


}
