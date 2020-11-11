package com.example.flickr.services;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.bumptech.glide.Glide;
import com.example.flickr.activities.FlickrApplication;
import com.example.flickr.fragments.FragmentPhoto;
import com.example.flickr.model.Album;
import com.example.flickr.model.Photo;
import com.example.flickr.utils.AdapterAlbums;
import com.example.flickr.utils.AdapterPhotos;
import com.google.gson.internal.$Gson$Preconditions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class FlickrBitmapProvider {

    private static final String TAG = "FlickrBitmapProvider";
    private ExecutorService executor;
    private Context context;
    private Glide glide;
    private Bitmap[] bitmapsAlbums;
    private Bitmap[] bitmapsPhotos;
    private int photoCount;

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public FlickrBitmapProvider(Context con) {
        this.context = con;
        bitmapsAlbums = new Bitmap[19];
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
                if (response != null){
                    String cad = saveBitmapToInternalStorage(response, photo.getPhotoID());
                    Log.d(TAG, "onResponse: RESPUESTA");
                    fragmentPhoto.setBitmap(response);
                    //Bitmap bitmap = loadImageInternalStorage("24729651518");
                    //int s = 0;
                }
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ERROR: " + error.getMessage());
            }
        });
        FlickrApplication.getSharedQueue().add(request);

    }


    public String saveBitmapToInternalStorage(Bitmap bitmap, String photoID) {
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

    public Bitmap loadImageFromInternalStorage(String photoID) {
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

    public void getAlbumThumbnails(List<Album> albums,
                SharedPreferences sharedPreferences, AdapterAlbums ada) {
        SearchAlbumThumbnails sat = new SearchAlbumThumbnails();
        sat.setAdapterAlbums(ada);
        sat.setAlbums(albums);
        sat.setSharedPreferences(sharedPreferences);
        executor.execute(sat);
    }

    public void getAlbumThumbnailsFromAPI(List<Album> albums,
                SharedPreferences sharedPreferences, Bitmap[] bitmaps) {
        String[] primaryIds = new String[albums.size()];
        String[] primaryServers = new String[albums.size()];
        String[] primarySecrets = new String[albums.size()];
        for (int i = 0; i < albums.size(); i++) {
            primaryIds[i] = albums.get(i).getFirstPhotoID();
            primaryServers[i] = albums.get(i).getFirstPhotoServer();
            primarySecrets[i] = albums.get(i).getFirstPhotoSecret();
        }

        List<Album> sortedAlbums;
        if (sharedPreferences.getString("order_field", "id").equals("name_asc")) {
            sortedAlbums = this.sortAlbumsByName(albums);
        }
        else {
            sortedAlbums = this.sortAlbumsById(albums);
        }

        // Albums are now sorted
        //Bitmap[] bitmaps = new Bitmap[sortedAlbums.size()];

        for (int i = 0; i < sortedAlbums.size(); i++) {
            Album a = sortedAlbums.get(i);
            ImageLoader imageLoader = FlickrApplication.getImageLoader();
            String url = "https://live.staticflickr.com/" + a.getFirstPhotoServer() + "/"
                    + a.getFirstPhotoID() + "_" + a.getFirstPhotoSecret() + "_q.jpg";
            final int indice = i;
            ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    if (response != null) {
                        bitmaps[indice] = response;
                        //bitmapsAlbums[indice] = response;
                    }
                }
            }, 0, 0, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: ERROR: " + error.getMessage());
                }
            });
            FlickrApplication.getSharedQueue().add(request);
        }

    }

    public void getPhotoThumbnails(List<Photo> photos, SharedPreferences sharedPreferences, AdapterPhotos ada, int photoCount) {
        SearchPhotosThumbnails spt = new SearchPhotosThumbnails();
        spt.setPhotoCount(photoCount);
        spt.setAdapterPhotos(ada);
        spt.setAlbums(photos);
        spt.setSharedPreferences(sharedPreferences);
        executor.execute(spt);
    }

    public void getPhotosThumbnailsFromAPI(List<Photo> photos, SharedPreferences
            sharedPreferences, Bitmap[] bitmaps) {
        String[] primaryIds = new String[photos.size()];
        String[] primaryServers = new String[photos.size()];
        String[] primarySecrets = new String[photos.size()];
        for (int i = 0; i < photos.size(); i++) {
            primaryIds[i] = photos.get(i).getPhotoID();
            primaryServers[i] = photos.get(i).getServer();
            primarySecrets[i] = photos.get(i).getSecret();
        }

        List<Photo> sortedPhotos;
        if (sharedPreferences.getString("order_field", "id").equals("name_asc")) {
            sortedPhotos = this.sortPhotosByName(photos);
        }
        else {
            sortedPhotos = this.sortPhotosById(photos);
        }

        // Photos are now sorted

        for (int i = 0; i < photos.size(); i++) {
            Photo p = photos.get(i);
            ImageLoader imageLoader = FlickrApplication.getImageLoader();
            String url = "https://live.staticflickr.com/" + p.getServer() + "/"
                    + p.getPhotoID() + "_" + p.getSecret() + "_q.jpg";
            final int indice = i;
            ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    if (response != null) {
                        bitmaps[indice] = response;
                        //bitmapsAlbums[indice] = response;
                    }
                }
            }, 0, 0, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: ERROR: " + error.getMessage());
                }
            });
            FlickrApplication.getSharedQueue().add(request);
        }

        /*if (bitmapsPhotos[photos.size() - 1] != null) {
            for (int i = 0; i < sortedPhotos.size(); i++) {
                saveThumbnailToInternalStorage(sortedPhotos.get(i).getPhotoID(), bitmapsPhotos[i]);
                //ada.getAlbums().get(i).setThumbnail(bitmapsAlbums[i]);
            }
            ada.searchPhotosThumbnails();
            ada.setThumbnailsReceived(true);
            //ada.setHasImagesToShow(true);
            //ada.notifyDataSetChanged();
        }*/

    }
    
    private List<Album> sortAlbumsById(List<Album> albums) {

        for (int i = 0; i < albums.size(); i++) {
            for (int j = i + 1; j < albums.size(); j++) {
                String a = albums.get(j).getAlbumID();
                String b = albums.get(i).getAlbumID();
                int compare = a.compareTo(b);
                if (compare <= 0) {
                    Album al = albums.get(i);
                    albums.set(i, albums.get(j));
                    albums.set(j, al);
                }

            }
        }

        return albums;
    }

    private List<Album> sortAlbumsByName(List<Album> albums) {

        for (int i = 0; i < albums.size(); i++) {
            for (int j = i + 1; j < albums.size(); j++) {
                String a = albums.get(j).getTitulo(); //.split(" ")[0]
                String b = albums.get(i).getTitulo(); ///.split(" ")[0]
                int compare = a.compareTo(b);
                if (compare <= 0) {
                    Album al = albums.get(i);
                    albums.set(i, albums.get(j));
                    albums.set(j, al);
                }
            }
        }

        return albums;
    }

    private List<Photo> sortPhotosById(List<Photo> photos) {

        for (int i = 0; i < photos.size(); i++) {
            for (int j = i + 1; j < photos.size(); j++) {
                String a = photos.get(j).getPhotoID();
                String b = photos.get(i).getPhotoID();
                int compare = a.compareTo(b);
                if (compare <= 0) {
                    Photo ph = photos.get(i);
                    photos.set(i, photos.get(j));
                    photos.set(j, ph);
                }

            }
        }

        return photos;
    }

    private List<Photo> sortPhotosByName(List<Photo> photos) {

        for (int i = 0; i < photos.size(); i++) {
            for (int j = i + 1; j < photos.size(); j++) {
                String a = photos.get(j).getTitle();
                String b = photos.get(i).getTitle();
                int compare = a.compareTo(b);
                if (compare <= 0) {
                    Photo ph = photos.get(i);
                    photos.set(i, photos.get(j));
                    photos.set(j, ph);
                }

            }
        }

        return photos;
    }

    private void saveThumbnailToInternalStorage(String photoID, Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(this.context);
        File directory = cw.getDir("Pictures", Context.MODE_PRIVATE);
        File mypath = new File(directory,photoID.toString() + "_q.jpg");

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
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
        bitmapsPhotos = new Bitmap[photoCount];
    }




    protected class SearchAlbumThumbnails implements Runnable {

        private AdapterAlbums adapterAlbums;
        private List<Album> albums;
        private SharedPreferences sharedPreferences;

        public void setAdapterAlbums(AdapterAlbums adapterAlbums) {
            this.adapterAlbums = adapterAlbums;
        }

        public void setAlbums(List<Album> albums) {
            this.albums = albums;
        }

        public void setSharedPreferences(SharedPreferences sharedPreferences) {
            this.sharedPreferences = sharedPreferences;
        }

        @Override
        public void run() {

            Bitmap[] bits = new Bitmap[albums.size()];
            getAlbumThumbnailsFromAPI(albums, sharedPreferences, bits);
            int a = 1;
            while (a == 1) {
                if (bits[albums.size() - 1] == null){
                    a = 1;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    a = 0;
                }
            }

            if (bits[albums.size() - 1] != null){
                for (int i = 0; i < albums.size(); i++) {
                    saveThumbnailToInternalStorage(albums.get(i).getFirstPhotoID(), bits[i]);
                    //ada.getAlbums().get(i).setThumbnail(bitmapsAlbums[i]);
                }
                //adapterAlbums.setThumbnails(bitmaps);
                //adapterAlbums.searchAlbumsThumbnails();
                adapterAlbums.setThumbnailsReceived(true);
                adapterAlbums.setHasImagesToShow(true);
                //adapterAlbums.notifyDataSetChanged();
            }

        }

    }


    protected class SearchPhotosThumbnails implements Runnable {

        private AdapterPhotos adapterPhotos;
        private List<Photo> photos;
        private SharedPreferences sharedPreferences;
        private int photoCount;

        public void setAdapterPhotos(AdapterPhotos adapterPhotos) {
            this.adapterPhotos = adapterPhotos;
        }

        public void setAlbums(List<Photo> photos) {
            this.photos = photos;
        }

        public void setSharedPreferences(SharedPreferences sharedPreferences) {
            this.sharedPreferences = sharedPreferences;
        }

        public void setPhotoCount(int photoCount) {
            this.photoCount = photoCount;
        }

        @Override
        public void run() {

            Bitmap[] bits = new Bitmap[photoCount];
            getPhotosThumbnailsFromAPI(photos, sharedPreferences, bits);
            int a = 1;
            while (a == 1) {
                if (bits[photoCount - 1] == null){
                    a = 1;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    a = 0;
                }
            }

            if (bits[photoCount - 1] != null){
                for (int i = 0; i < photos.size(); i++) {
                    saveThumbnailToInternalStorage(photos.get(i).getPhotoID(), bits[i]);
                    //ada.getAlbums().get(i).setThumbnail(bitmapsAlbums[i]);
                }
                //adapterAlbums.setThumbnails(bitmaps);
                //adapterAlbums.searchAlbumsThumbnails();
                adapterPhotos.setThumbnailsReceived(true);
                adapterPhotos.setHasImagesToShow(true);
                //adapterAlbums.notifyDataSetChanged();
            }

        }

    }


}


/*

//URLbase/server/id_secret_size.jpg
https://live.staticflickr.com
https://www.flickr.com/services/api/misc.urls.html

*/