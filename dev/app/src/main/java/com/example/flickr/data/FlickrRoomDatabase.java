package com.example.flickr.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.flickr.model.Album;
import com.example.flickr.model.Comment;
import com.example.flickr.model.Photo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Album.class, Photo.class, Comment.class},
        version = 2, exportSchema = false)
abstract class FlickrRoomDatabase extends RoomDatabase {
    abstract AlbumDao albumDao();
    abstract CommentDao commentDao();
    abstract PhotoDao photoDao();

    private static volatile FlickrRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static FlickrRoomDatabase getDataBase(final Context context){
        if (INSTANCE == null){
            synchronized (FlickrRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FlickrRoomDatabase.class, "flickr_db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return  INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                AlbumDao albumDao = INSTANCE.albumDao();
                albumDao.deleteAll();

                CommentDao commentDao = INSTANCE.commentDao();
                commentDao.deleteAll();

                PhotoDao photoDao = INSTANCE.photoDao();
                photoDao.deleteAll();
            });
        }
    };
}
