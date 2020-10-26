package com.example.flickr.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.flickr.model.Album;
import com.example.flickr.model.Comment;
import com.example.flickr.model.Photo;

import java.util.List;

public class FlickrRepository {

    private AlbumDao albumDao;
    private CommentDao commentDao;
    private PhotoDao photoDao;

    private LiveData<List<Album>> allAlbums;
    private int albumCount;
    private LiveData<List<Comment>> allComments;
    private LiveData<List<Photo>> allPhotos;

    FlickrRepository(Application application) {
        FlickrRoomDatabase db = FlickrRoomDatabase.getDataBase(application);

        albumDao = db.albumDao();
        allAlbums = albumDao.getAlbums();
        albumCount = albumDao.countAlbums();

        commentDao = db.commentDao();
        allComments = commentDao.getComments();

        photoDao = db.photoDao();
        allPhotos = photoDao.getPhotos();
    }

    public LiveData<List<Album>> getAllAlbums() {
        return allAlbums;
    }
    public LiveData<List<Comment>> getAllComments() {
        return allComments;
    }
    public LiveData<List<Photo>> getAllPhotos() {
        return allPhotos;
    }
    public int getAlbumCount() { return albumCount; }

    public void insert(Album album) {
        FlickrRoomDatabase.databaseWriteExecutor.execute(() -> {
            albumDao.insert(album);
        });
    }

    public void insert(Comment comment) {
        FlickrRoomDatabase.databaseWriteExecutor.execute(() -> {
            commentDao.insert(comment);
        });
    }

    public void insert(Photo photo) {
        FlickrRoomDatabase.databaseWriteExecutor.execute(() -> {
            photoDao.insert(photo);
        });
    }

}
