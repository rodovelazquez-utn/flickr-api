package com.example.flickr.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.flickr.model.Photo;

import java.util.List;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM photo_table ORDER BY id_photo")
    LiveData<List<Photo>> getPhotos();

    @Query("SELECT * FROM photo_table WHERE id_photo = :id ORDER BY id_photo")
    LiveData<List<Photo>> getPhotosWhereId(String id);

    @Query("SELECT * FROM photo_table WHERE id_album = :idAlbum ORDER BY id_photo")
    LiveData<List<Photo>> getPhotosWhereAlbumId(String idAlbum);

    @Query("SELECT * FROM photo_table ORDER BY title ASC")
    LiveData<List<Photo>> getPhotosOrderTitle();

    @Query("SELECT * FROM photo_table WHERE id_photo = :id ORDER BY title ASC")
    LiveData<List<Photo>> getPhotosWhereIdOrderTitle(String id);

    @Query("SELECT * FROM photo_table WHERE id_album = :idAlbum ORDER BY title ASC")
    LiveData<List<Photo>> getPhotosWhereAlbumIdOrderTitle(String idAlbum);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Photo photo);

    @Query("DELETE FROM photo_table")
    void deleteAll();
}
