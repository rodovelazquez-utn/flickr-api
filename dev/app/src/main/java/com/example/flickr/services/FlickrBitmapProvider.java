package com.example.flickr.services;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.example.flickr.activities.FlickrApplication;
import com.example.flickr.model.Photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class FlickrBitmapProvider {

    private static final String TAG = "FlickrBitmapProvider";
    private Context context;
    private Glide glide;

    public void setContext(Context context) {
        this.context = context;
    }

    public FlickrBitmapProvider(Context con) {
        this.context = con;
    }

    public void getBitmap(Context context, ImageView imageView) {
        String url = "https://live.staticflickr.com/1846/29828429307_53193e91e1.jpg";

        Glide.with(context).load(url).into(imageView);
    }

    public Bitmap getBitmapFromUrl(Photo photo){
        ImageLoader imageLoader = FlickrApplication.getImageLoader();
        //String url = "https://live.staticflickr.com/" + photo.getServer() + "/"
        //        + photo.getPhotoID() + "_" + photo.getSecret() + "_" + "b" + ".jpg";

        String url = "https://live.staticflickr.com/" + photo.getServer() + "/"
                + photo.getPhotoID() + "_" + photo.getSecret() + ".jpg";

        final Bitmap[] bit = {null};

        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    Log.d(TAG, "onResponse: Funciona");
                    Bitmap b = response.getBitmap();
                    bit[0] = b;
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ERROR: " + error.getMessage());
            }
        });

        return bit[0];
    }

    public void saveBitmapInternalStorage(Bitmap bitmap, String photoID) {
        ContextWrapper cw = new ContextWrapper(this.context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory,photoID.toString() + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                fos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadImageInternalStorage(String photoID, ImageView imageView) {
        try {
            File f = new File(photoID);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}


/*

//URLbase/server/id_secret_size.jpg
https://live.staticflickr.com
https://www.flickr.com/services/api/misc.urls.html

*/