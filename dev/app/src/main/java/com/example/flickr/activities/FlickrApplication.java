package com.example.flickr.activities;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.flickr.services.FlickrDataProvider;

public class FlickrApplication extends Application {

    private static RequestQueue queue;
    private static ImageLoader imageLoader;

    // This will be the only FlickrDataProvider instance
    private static FlickrDataProvider dataProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        dataProvider = new FlickrDataProvider();
        queue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static RequestQueue getSharedQueue(){
        return queue;
    }

    public static ImageLoader getImageLoader(){
        return imageLoader;
    }

    public static FlickrDataProvider getDataProvider() {
        return dataProvider;
    }

}
