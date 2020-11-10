package com.example.flickr.services;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.bumptech.glide.Glide;
import com.example.flickr.activities.FlickrApplication;
import com.example.flickr.fragments.FragmentPhoto;
import com.example.flickr.model.Album;
import com.example.flickr.model.Photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
        // 25785688184;

        Glide.with(context).load(url).into(imageView);
    }

    public void getBitmapFromUrl(Photo photo, FragmentPhoto fragmentPhoto){
        ImageLoader imageLoader = FlickrApplication.getImageLoader();
        String url = "https://live.staticflickr.com/" + photo.getServer() + "/"
                + photo.getPhotoID() + "_" + photo.getSecret() + ".jpg";

        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // guardar imagen en storage
                String cad = saveBitmapInternalStorage(response, photo.getPhotoID());
                Log.d(TAG, "onResponse: RESPUESTA");
                fragmentPhoto.setBitmap(response);
                //Bitmap bitmap = loadImageInternalStorage("24729651518");
                //int s = 0;
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ERROR: " + error.getMessage());
            }
        });
        FlickrApplication.getSharedQueue().add(request);

    }


    public String saveBitmapInternalStorage(Bitmap bitmap, String photoID) {
        ContextWrapper cw = new ContextWrapper(this.context);
        File directory = cw.getDir("Pictures", Context.MODE_PRIVATE);
        File mypath = new File(directory,photoID.toString() + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
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
        return directory.getAbsolutePath();
    }

    public Bitmap loadImageInternalStorage(String photoID) {
        try {
            File f = new File("/data/user/0/com.example.flickr/app_Pictures/" + photoID + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //imageView.setImageBitmap(b);
            return  b;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}


/*

//URLbase/server/id_secret_size.jpg
https://live.staticflickr.com
https://www.flickr.com/services/api/misc.urls.html

*/