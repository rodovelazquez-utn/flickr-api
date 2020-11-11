package com.example.flickr.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "photo_table")
public class Photo {

    @SerializedName(value = "id")
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_photo")
    private String photoID;

    @ColumnInfo(name = "title")
    @SerializedName(value = "title")
    private String title;

    @ForeignKey(entity = Album.class, parentColumns = "id_album", childColumns = "id_album")
    @ColumnInfo(name = "id_album")
    private String albumID;

    @ColumnInfo(name = "secret")
    @SerializedName(value = "secret")
    private String secret;

    @ColumnInfo(name = "server")
    @SerializedName(value = "server")
    private String server;

    @Ignore
    private Bitmap thumbnail;

    public Photo(){

    }

    @NonNull
    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(@NonNull String photoID) {
        this.photoID = photoID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
