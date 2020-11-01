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
    private LiveData<List<Album>> albumsById;
    private int albumCount;
    private LiveData<List<Comment>> allComments;
    private LiveData<List<Comment>> commentsById;
    private LiveData<List<Comment>> commentsByPhotoId;
    private LiveData<List<Photo>> allPhotos;
    private LiveData<List<Photo>> photosById;
    private LiveData<List<Photo>> photosByAlbumId;

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

    public LiveData<List<Album>> getAlbumsWhereId(String id) {
        return repository.getAlbumsWhereId(id);
    }

    public int getAlbumCount() {
        return albumCount;
    }

    public String getAlbumPhotoCount() { return repository.getAlbumPhotoCount(); }

    public LiveData<List<Comment>> getAllComments() {
        return allComments;
    }

    public LiveData<List<Comment>> getCommentsWhereId(String id) {
        return repository.getCommentsWhereId(id);
    }

    public LiveData<List<Comment>> getCommentsWherePhotoId(String photoId) {
        return repository.getCommentsWhereIdPhoto(photoId);
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return allPhotos;
    }

    public LiveData<List<Photo>> getPhotosWhereId(String id) {
        return repository.getPhotosWhereId(id);
    }

    public LiveData<List<Photo>> getPhotosWhereAlbumId(String id) {
        return repository.getPhotosWhereAlbumId(id);
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
