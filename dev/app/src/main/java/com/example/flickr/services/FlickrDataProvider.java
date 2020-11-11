package com.example.flickr.services;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.flickr.activities.FlickrApplication;
import com.example.flickr.model.Album;
import com.example.flickr.model.Comment;
import com.example.flickr.model.Photo;
import com.example.flickr.utils.AdapterAlbums;
import com.example.flickr.utils.AdapterComments;
import com.example.flickr.utils.AdapterPhotos;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class FlickrDataProvider {

    private static final String TAG = "FlickrDataProvider";

    private ConnectivityManager connectivityManager;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setConnectivityManager(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public void loadFlickrAlbums(AdapterAlbums adapter) {

        boolean contentOnDB = this.areAlbumsOnDB();
        boolean internet = this.isNetworkConnection();

        if (!internet){
            if (contentOnDB) {
                String preference = "";
                if (sharedPreferences.getString("order_field", "id").equals("name_asc")){
                    preference = "name";
                }
                else {
                    preference = "id";
                }
                List<Album> albums = this.searchExistingAlbumsInDataBase(preference);
                if (albums == null) {
                    // TODO: throw new NotFoundException("Albums NOT FOUND");
                }
                adapter.setAlbums(albums);
            }
            else {
                this.dialogEmptyDB();
            }
        }
        else {
            if (contentOnDB) {
                String preference = "";
                if (sharedPreferences.getString("order_field", "id").equals("name_asc")){
                    preference = "name";
                }
                else {
                    preference = "id";
                }
                List<Album> albums = this.searchExistingAlbumsInDataBase(preference);
                if (albums == null){
                    // TODO: throw new NotFoundException("Albums NOT FOUND");
                }
                adapter.setAlbums(albums);

                this.getAlbumsFromAPI(); // This already insert the albums in the DB
                // The adapter's observer should refresh the view with the new albums saved
            }
            else {
                this.getAlbumsFromAPI();
                // The adapter's observer should refresh the view with the new albums saved
            }
        }
    }

    public void loadFlickrPhotos(AdapterPhotos adapter, String albumID) {
        Log.d(TAG, "loadFlickrPhotos: AlbumID: " + albumID);

        boolean contentOnDB = this.areAlbumsOnDB();
        boolean internet = this.isNetworkConnection();

        if (!internet){
            if (contentOnDB) {
                String preference = "";
                if (sharedPreferences.getString("order_field", "id").equals("name_asc")){
                       preference = "name";
                }
                else {
                    preference = "id";
                }
                List<Photo> photos = this.searchExistingPhotosInDataBase(albumID, preference);
                if (photos == null) {
                    // TODO: throw new NotFoundException("Albums NOT FOUND");
                }
                adapter.setPhotos(photos);
            }
            else {
                this.dialogEmptyDB();
            }
        }
        else {
            if (contentOnDB) {
                String preference = "";
                if (sharedPreferences.getString("order_field", "id").equals("name_asc")){
                    preference = "name";
                }
                else {
                    preference = "id";
                }
                List<Photo> photos = this.searchExistingPhotosInDataBase(albumID, preference);
                if (photos == null) {
                    // TODO: throw new NotFoundException("Albums NOT FOUND");
                }
                adapter.setPhotos(photos);

                this.getPhotosFromAPI(albumID); // This already insert the photos in the DB
                // The adapter's observer should refresh the view with the new photos saved
            }
            else {
                this.getPhotosFromAPI(albumID);
                // The adapter's observer should refresh the view with the new photos saved
            }
        }

    }

    public void loadFlickrComments(AdapterComments adapter, String photoID) {
        Log.d(TAG, "loadFlickrComments: COMENTARIOS POR CARGAR");

        boolean contentOnDB = this.areAlbumsOnDB();
        boolean internet = this.isNetworkConnection();

        if (!internet){
            if (contentOnDB) {
                List<Comment> comments = this.searchExistingCommentsInDataBase(photoID);
                if (comments == null) {
                    // TODO: throw new NotFoundException("Albums NOT FOUND");
                }
                adapter.setComments(comments);
            }
            else {
                this.dialogEmptyDB();
            }
        }
        else {
            if (contentOnDB) {
                List<Comment> comments = this.searchExistingCommentsInDataBase(photoID);
                if (comments == null) {
                    // TODO: throw new NotFoundException("Albums NOT FOUND");
                }
                adapter.setComments(comments);

                this.getCommentsFromAPI(photoID); // This already insert the photos in the DB
                // The adapter's observer should refresh the view with the new photos saved
            }
            else {
                this.getCommentsFromAPI(photoID);
                // The adapter's observer should refresh the view with the new photos saved
            }
        }
    }

    private void dialogEmptyDB() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error!");
        builder.setMessage("There's no data on the database to show. Try to establish a network " +
                "connection in order to gather the photos from the API");

        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private boolean areAlbumsOnDB() {
        if (FlickrApplication.getViewModel().getAlbumCount() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean arePhotosOnDB(String albumID) {
        if (FlickrApplication.getViewModel().getPhotoCountWhereId(albumID) == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean areCommentsOnDB(String photoID) {
        if (FlickrApplication.getViewModel().getCommentCountWhereId(photoID) == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean isNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        //ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public List<Album> setAlbumTitles(List<Album> albums) {
        for (int i = 0; i < albums.size(); i++) {
            String title = albums.get(i).getAlbumTitle().getTitle();
            albums.get(i).setTitulo(title);
        }
        return albums;
    }

    private void getAlbumsFromAPI() {

        String url = "https://www.flickr.com/services/rest/?method=flickr." +
                "photosets.getList&api_key=1604dce64ecf5181a526b2de04a89b9f&" +
                "user_id=138707650%40N07&format=json&nojsoncallback=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject o = new JSONObject(response);
                            JSONArray jsonA = o.getJSONObject("photosets").getJSONArray("photoset");
                            Album[] albums = gson.fromJson(String.valueOf(jsonA), Album[].class);

                            List<Album> albumsFromAPI = Arrays.asList(albums);
                            List<Album> albumsWithTitles = setAlbumTitles(albumsFromAPI);
                            saveAlbumsInDataBase(albumsWithTitles);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: EXCEPTION --> " + e.getMessage());
                        }
                        Log.d(TAG, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
            }
        });
        FlickrApplication.getSharedQueue().add(stringRequest);
    }

    private void getPhotosFromAPI(String albumId) {
        String url = "https://www.flickr.com/services/rest/?method=flickr.photosets.getPhotos&" +
                "api_key=1604dce64ecf5181a526b2de04a89b9f&photoset_id=" + albumId +
                "&user_id=138707650%40N07&format=json&nojsoncallback=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject o = new JSONObject(response);
                            JSONArray jsonA = o.getJSONObject("photoset").getJSONArray("photo");
                            Photo[] photos = gson.fromJson(String.valueOf(jsonA), Photo[].class);

                            /*JSONObject photoCount = o.getJSONObject("total");
                            int count = gson.fromJson(String.valueOf(photoCount), Integer.class);
                            LiveData<List<Album>> alb = FlickrApplication.getViewModel().getAlbumsWhereId(albumId);
                            alb.getValue().get(0).setAlbumCount(count);*/

                            List<Photo> photosFromAPI = Arrays.asList(photos);
                            for (int i = 0; i < photosFromAPI.size(); i++) {
                                photosFromAPI.get(i).setAlbumID(albumId);
                            }
                            savePhotosInDataBase(photosFromAPI);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: EXCEPTION --> " + e.getMessage());
                        }
                        Log.d(TAG, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
            }
        });
        FlickrApplication.getSharedQueue().add(stringRequest);
    }

    private void getCommentsFromAPI(String photoID) {
        String url = "https://www.flickr.com/services/rest/?method=flickr.photos.comments.getList" +
                "&api_key=1604dce64ecf5181a526b2de04a89b9f&photo_id="+ photoID +"&format=" +
                "json&nojsoncallback=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject o = new JSONObject(response);
                            JSONArray jsonA = o.getJSONObject("comments").getJSONArray("comment");
                            Comment[] comments = gson.fromJson(String.valueOf(jsonA), Comment[].class);

                            /*JSONObject photoCount = o.getJSONObject("total");
                            int count = gson.fromJson(String.valueOf(photoCount), Integer.class);
                            LiveData<List<Album>> alb = FlickrApplication.getViewModel().getAlbumsWhereId(albumId);
                            alb.getValue().get(0).setAlbumCount(count);*/

                            List<Comment> commentsFromAPI = Arrays.asList(comments);
                            for (int i = 0; i < commentsFromAPI.size(); i++) {
                                commentsFromAPI.get(i).setPhotoID(photoID);
                            }
                            saveCommentsInDataBase(commentsFromAPI);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: EXCEPTION --> " + e.getMessage());
                        }
                        Log.d(TAG, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
            }
        });
        FlickrApplication.getSharedQueue().add(stringRequest);
    }

    private void saveAlbumsInDataBase(List<Album> albums) {
        for (int i = 0; i < albums.size(); i++) {
            try {
                FlickrApplication.getViewModel().insert(albums.get(i));
                //viewModel.insert(albumsFromAPI.get(i));
                //Es bueno hacer el ViewModel único ??
            }
            catch (Exception e){
                Log.d(TAG, "saveInDataBase: ERROR AL HACER EL INSERT EN LA BASE DE DATOS");
                // TODO: new AlertDialogMessage(e.getMessage());
            }
        }
    }

    private void savePhotosInDataBase(List<Photo> photos) {
        for (int i = 0; i < photos.size(); i++) {
            try {
                FlickrApplication.getViewModel().insert(photos.get(i));
                //viewModel.insert(albumsFromAPI.get(i));
            }
            catch (Exception e){
                Log.d(TAG, "saveInDataBase: ERROR AL HACER EL INSERT EN LA BASE DE DATOS");
                // TODO: new AlertDialogMessage(e.getMessage());
            }
        }
    }

    private void saveCommentsInDataBase(List<Comment> comments) {
        for (int i = 0; i < comments.size(); i++) {
            try {
                FlickrApplication.getViewModel().insert(comments.get(i));
                //viewModel.insert(albumsFromAPI.get(i));
            }
            catch (Exception e){
                Log.d(TAG, "saveInDataBase: ERROR AL HACER EL INSERT EN LA BASE DE DATOS");
                // TODO: new AlertDialogMessage(e.getMessage());
            }
        }
    }

    private List<Album> searchExistingAlbumsInDataBase(String preference) {
        List<Album> albums;
        try {
            if (preference.equals("id")){
                albums = FlickrApplication.getViewModel().getAllAlbums().getValue();
            }
            else
            {
                albums = FlickrApplication.getViewModel().getAlbumsOrderTitle().getValue();
            }

            return albums;
        }
        catch (Exception e) {
            Log.d(TAG, "searchAlbumsInDataBase: EXCEPTION --> " + e.getMessage());
        }
        return albums = null;
        // throw new Resources.NotFoundException("ALBUMES NO ENCONTRADOS EN LA BD");
    }

    /*private List<Album> searchExistingAlbumsInDataBase() {
        return null;
    }*/

    private List<Photo> searchPhotosInDataBase(String albumId) {
        List<Photo> photos;
        try {
            photos = FlickrApplication.getViewModel().getAllPhotos().getValue();
            return photos;
        }
        catch (Exception e) {
            Log.d(TAG, "searchAlbumsInDataBase: EXCEPTION --> " + e.getMessage());
        }
        return photos = null;
        // throw new Resources.NotFoundException("ALBUMES NO ENCONTRADOS EN LA BD");
    }

    private List<Photo> searchExistingPhotosInDataBase(String albumId, String preference) {
        Photo[] array = new Photo[0];
        List<Photo> photos = Arrays.asList(array);
        //int albumCount = Integer.parseInt(album.getAlbumCount());
        try {
            /*for (int i = 0; i < albumCount; i++) {
            }*/
            if (preference.equals("id")) {
                photos = FlickrApplication.getViewModel().getPhotosWhereAlbumId(albumId).getValue();
            }
            else
            {
                photos = FlickrApplication.getViewModel().getPhotosWhereAlbumIdOrderTitle(albumId).getValue();
            }

            return photos;
        }
        catch (Exception e) {
            Log.d(TAG, "searchExistingPhotosInDataBase: EXCEPTION --> " + e.getMessage());
        }
        return photos = null;
    }

    private List<Comment> searchExistingCommentsInDataBase(String photoID) {
        Comment[] array = new Comment[0];
        List<Comment> comments = Arrays.asList(array);
        try {
            comments = FlickrApplication.getViewModel().getCommentsWherePhotoId(photoID).getValue();
            return comments;
        }
        catch (Exception e) {
            Log.d(TAG, "searchExistingCommentsInDataBase: EXCEPTION --> " + e.getMessage());
        }
        return comments = null;
        // throw new Resources.NotFoundException("ALBUMES NO ENCONTRADOS EN LA BD");
    }

}


// setPhotoBitmap:
// ImageView.setImageURI(Uri.parse(new File("/sdcard/cats.jpg").toString()));
