package com.example.flickr.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.flickr.model.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comment_table")
    LiveData<List<Comment>> getComments();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment comment);

    @Query("DELETE FROM comment_table")
    void deleteAll();
}

