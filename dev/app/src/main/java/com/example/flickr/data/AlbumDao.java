package com.example.flickr.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.flickr.model.Album;

import java.util.List;

@Dao
public interface AlbumDao {
    @Query("SELECT * FROM album_table ORDER BY id_album")
    LiveData<List<Album>> getAlbums();

    @Query("SELECT * FROM album_table WHERE id_album = :id ORDER BY id_album")
    LiveData<List<Album>> getAlbumsWhereId(String id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Album album);

    @Query("DELETE FROM album_table")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM album_table")
    int countAlbums();

    @Query("SELECT size FROM album_table")
    String getAlbumPhotoCount();
}
