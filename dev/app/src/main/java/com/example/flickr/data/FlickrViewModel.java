package com.example.flickr.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flickr.model.Album;
import com.example.flickr.model.Comment;
import com.example.flickr.model.Photo;

import java.util.List;

public class FlickrViewModel extends AndroidViewModel {

    private FlickrRepository repository;
    private LiveData<List<Album>> allAlbums;
    private int albumCount;
    private LiveData<List<Comment>> allComments;
    private LiveData<List<Photo>> allPhotos;

    public FlickrViewModel(@NonNull Application application) {
        super(application);

        repository = new FlickrRepository(application);
        allAlbums = repository.getAllAlbums();
        albumCount = repository.getAlbumCount();
        allComments = repository.getAllComments();
        allPhotos = repository.getAllPhotos();
    }

    public LiveData<List<Album>> getAllAlbums() {
        return allAlbums;
    }

    public int getAlbumCount() {
        return albumCount;
    }

    public LiveData<List<Comment>> getAllComments() {
        return allComments;
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return allPhotos;
    }

    public void insert(Album album) {
        repository.insert(album);
    }

    public void insert(Comment comment) {
        repository.insert(comment);
    }

    public void insert(Photo photo) {
        repository.insert(photo);
    }

}
