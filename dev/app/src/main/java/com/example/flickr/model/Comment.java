package com.example.flickr.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "comment_table")
public class Comment {

    @SerializedName(value = "id")
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_comment")
    private String commentID;

    @ColumnInfo(name = "authorname")
    @SerializedName(value = "authorname")
    private String authorName;

    @ColumnInfo(name = "comment")
    @SerializedName(value = "_content")
    private String commentText;

    @ForeignKey(entity = Photo.class, parentColumns = "id_photo", childColumns = "id_photo")
    @ColumnInfo(name = "id_photo")
    private String photoID;

    public Comment(){

    }

    @NonNull
    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(@NonNull String commentID) {
        this.commentID = commentID;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
