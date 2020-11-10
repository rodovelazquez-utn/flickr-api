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
    private LiveData<List<Album>> allAlbumsOrderedTitle;
    private int albumCount;
    private LiveData<List<Comment>> allComments;
    private LiveData<List<Photo>> allPhotos;

    FlickrRepository(Application application) {
        FlickrRoomDatabase db = FlickrRoomDatabase.getDataBase(application);

        albumDao = db.albumDao();
        allAlbums = albumDao.getAlbums();
        allAlbumsOrderedTitle = albumDao.getAlbumsOrderTitle();
        albumCount = albumDao.countAlbums();

        commentDao = db.commentDao();
        allComments = commentDao.getComments();

        photoDao = db.photoDao();
        allPhotos = photoDao.getPhotos();
    }

    public LiveData<List<Album>> getAllAlbums() {
        return allAlbums;
    }
    public LiveData<List<Album>> getAlbumsWhereId(String id) {
        return albumDao.getAlbumsWhereId(id);
    }
    public LiveData<List<Album>> getAlbumsOrderTitle() { return allAlbumsOrderedTitle; }
    public LiveData<List<Album>> getAlbumsWhereIdOrderTitle(String id) {
        return albumDao.getAlbumsWhereIdOrderTitle(id);
    }

    public int getAlbumCount() { return albumCount; }
    public String getAlbumPhotoCount() { return albumDao.getAlbumPhotoCount(); }

    public LiveData<List<Comment>> getAllComments() {
        return allComments;
    }
    public LiveData<List<Comment>> getCommentsWhereId(String id) { return commentDao.getCommentsWhereId(id); }
    public LiveData<List<Comment>> getCommentsWhereIdPhoto(String id) { return commentDao.getCommentsWhereIdPhoto(id); }

    public LiveData<List<Photo>> getAllPhotos() {
        return allPhotos;
    }
    public LiveData<List<Photo>> getPhotosWhereId(String id) {
        return photoDao.getPhotosWhereId(id);
    }
    public LiveData<List<Photo>> getPhotosWhereAlbumId(String id) {
        return photoDao.getPhotosWhereAlbumId(id);
    }

    public LiveData<List<Photo>> getPhotosOrderTitle() { return photoDao.getPhotosOrderTitle(); }
    public LiveData<List<Photo>> getPhotosWhereIdOrderTitle(String id) {
        return photoDao.getPhotosWhereIdOrderTitle(id);
    }
    public LiveData<List<Photo>> getPhotosWhereAlbumIdOrderTitle(String idAlbum){
        return photoDao.getPhotosWhereAlbumIdOrderTitle(idAlbum);
    }

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
