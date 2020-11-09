package com.example.flickr.activities;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.flickr.data.FlickrViewModel;
import com.example.flickr.services.FlickrBitmapProvider;
import com.example.flickr.services.FlickrDataProvider;

public class FlickrApplication extends Application {

    private static RequestQueue queue;
    private static ImageLoader imageLoader;

    // This will be the only FlickrDataProvider instance
    private static FlickrDataProvider dataProvider;
    private static FlickrBitmapProvider bitmapProvider;

    // This will be the only FlickrViewModel instance
    private static ViewModelStoreOwner viewModelStoreOwner;
    private static FlickrViewModel viewModel;

    public static void setFlickrViewModel(FlickrViewModel vm){
        viewModel = vm;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataProvider = new FlickrDataProvider();
        bitmapProvider = new FlickrBitmapProvider(getApplicationContext());

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

    public static void setViewModelStoreOwner(ViewModelStoreOwner storeOwner) {
        viewModelStoreOwner = storeOwner;
        createViewModel();
    }

    private static void createViewModel() {
        if (viewModel == null) {
            // viewModel = new ViewModelProvider(requireActivity()).get(FlickrViewModel.class);
            // viewModel = new ViewModelProvider(requireActivity(),
            //      getDefaultViewModelProviderFactory()).get(FlickrViewModel.class);
            viewModel = new ViewModelProvider(viewModelStoreOwner).get(FlickrViewModel.class);
        }
    }

    public static FlickrViewModel getViewModel() {
        return viewModel;
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

    public static FlickrBitmapProvider getBitmapProvider() {
        return bitmapProvider;
    }

}
