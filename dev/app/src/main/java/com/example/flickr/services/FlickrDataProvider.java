package com.example.flickr.services;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.flickr.activities.FlickrApplication;
import com.example.flickr.model.Album;
import com.example.flickr.utils.AdapterAlbums;
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

    public void setConnectivityManager(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public void loadFlickrAlbums(AdapterAlbums adapter) {
        boolean contentOnDB = this.isContentOnDB();
        boolean internet = this.isNetworkConnection();

        if (!internet){
            if (contentOnDB) {
                List<Album> albums = this.searchAlbumsInDataBase();
                if (albums == null) {
                    // TODO: throw new NotFoundException("Albums NOT FOUND");
                }
                adapter.setAlbums(albums);
            }
            else {
                // TODO: ActivityMain.alertDialogNoInternet();
            }
        }
        else {
            if (contentOnDB) {
                List<Album> albums = this.searchAlbumsInDataBase();
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

    private boolean isContentOnDB() {
        if (FlickrApplication.getViewModel().getAlbumCount() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean isNetworkConnection() {
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
                            saveInDataBase(albumsWithTitles);
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

    private void saveInDataBase(List<Album> albums) {
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

    private List<Album> searchAlbumsInDataBase() {
        List<Album> albums;
        try {
            albums = FlickrApplication.getViewModel().getAllAlbums().getValue();
            return albums;
        }
        catch (Exception e) {
            Log.d(TAG, "searchAlbumsInDataBase: EXCEPTION --> " + e.getMessage());
        }
        return albums = null;
        // throw new Resources.NotFoundException("ALBUMES NO ENCONTRADOS EN LA BD");
    }

}
