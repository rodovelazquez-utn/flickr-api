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
    @Query("SELECT * FROM comment_table ORDER BY id_comment")
    LiveData<List<Comment>> getComments();

    @Query("SELECT * FROM comment_table WHERE id_comment = :id ORDER BY id_comment")
    LiveData<List<Comment>> getCommentsWhereId(String id);

    @Query("SELECT * FROM comment_table WHERE id_photo = :idPhoto")
    LiveData<List<Comment>> getCommentsWhereIdPhoto(String idPhoto);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Comment comment);

    @Query("DELETE FROM comment_table")
    void deleteAll();
}

