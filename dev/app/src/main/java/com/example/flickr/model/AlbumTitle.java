package com.example.flickr.model;

import com.google.gson.annotations.SerializedName;

public class AlbumTitle {

    @SerializedName(value = "_content")
    private String title;

    public AlbumTitle(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String _content) {
        this.title = _content;
    }
}
