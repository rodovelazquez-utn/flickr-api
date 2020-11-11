package com.example.flickr.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.flickr.activities.FlickrApplication;
import com.example.flickr.utils.AdapterAlbums;
import com.google.android.material.snackbar.Snackbar;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private Snackbar snackbar;
    private AdapterAlbums adapterAlbums;

    public void setSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if (FlickrApplication.getDataProvider().isNetworkConnection()) {
                String text = "Online!";
                this.makeSnackbar(snackbar, text, Snackbar.LENGTH_SHORT);
                Log.e("NetworkChange", "Online Connect Intenet ");
                FlickrApplication.getDataProvider().loadFlickrAlbums(adapterAlbums);
            } else {
                String text = "Couldn't stablish connection";
                this.makeSnackbar(snackbar, text, Snackbar.LENGTH_INDEFINITE);
                Log.e("NetworkChange", "Conectivity Failure !!! ");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void makeSnackbar(Snackbar snack, String texto, int duration){
        snack.setText(texto);
        snack.setDuration(duration);
        View snackbarLayout = snack.getView();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        //lp.gravity = Gravity.CENTER;
        lp.setMargins(0, 0, 0, 0);
        snackbarLayout.setLayoutParams(lp);
        snack.show();
    }

    public void setAdapterAlbums(AdapterAlbums adapterAlbums) {
        this.adapterAlbums = adapterAlbums;
    }

    /*private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }*/
}
