package com.example.flickr.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "album_table")
public class Album {

    @SerializedName(value = "id")
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_album")
    private String albumID;

    @ColumnInfo(name = "size")
    @SerializedName(value = "count_photos")
    private String albumCount;

    @ColumnInfo(name = "id_owner")
    @SerializedName(value = "owner")
    private String ownerID;

    @ColumnInfo(name = "id_primary")
    @SerializedName(value = "primary")
    private String firstPhotoID;

    @ColumnInfo(name = "title")
    private String titulo;

    @Ignore
    @SerializedName(value = "title")
    private AlbumTitle albumTitle;

    @ColumnInfo(name = "server_primary")
    @SerializedName(value = "server")
    private String firstPhotoServer;

    @ColumnInfo(name = "secret_primary")
    @SerializedName(value = "secret")
    private String firstPhotoSecret;

    public Album(){

    }

    @NonNull
    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(@NonNull String albumID) {
        this.albumID = albumID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getFirstPhotoID() {
        return firstPhotoID;
    }

    public void setFirstPhotoID(String firstPhotoID) {
        this.firstPhotoID = firstPhotoID;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String title) {
        this.titulo = title;
    }

    public AlbumTitle getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(AlbumTitle albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(String albumCount) {
        this.albumCount = albumCount;
    }

    public String getFirstPhotoServer() {
        return firstPhotoServer;
    }

    public void setFirstPhotoServer(String firstPhotoServer) {
        this.firstPhotoServer = firstPhotoServer;
    }

    public String getFirstPhotoSecret() {
        return firstPhotoSecret;
    }

    public void setFirstPhotoSecret(String firstPhotoSecret) {
        this.firstPhotoSecret = firstPhotoSecret;
    }
}
