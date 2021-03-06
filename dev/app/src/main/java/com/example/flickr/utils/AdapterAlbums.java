package com.example.flickr.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flickr.R;
import com.example.flickr.activities.FlickrApplication;
import com.example.flickr.fragments.FragmentHome;
import com.example.flickr.model.Album;

import java.util.List;

public class AdapterAlbums extends RecyclerView.Adapter<AdapterAlbums.ViewHolder> {

    private static final String TAG = "AdapterAlbums";
    private LayoutInflater inflater;

    public List<Album> getAlbums() {
        return albums;
    }

    private List<Album> albums;
    private List<Bitmap> thumbnails;

    public void setHasImagesToShow(boolean hasImagesToShow) {
        this.hasImagesToShow = hasImagesToShow;
    }

    public boolean getHasImagesToShow() {
        return hasImagesToShow;
    }

    private boolean hasImagesToShow;
    private boolean thumbnailsReceived;

    public void setThumbnailsReceived(boolean thumbnailsReceived) {
        this.thumbnailsReceived = thumbnailsReceived;
    }

    public boolean getThumbnailsReceived() {
        return thumbnailsReceived;
    }

    //private List<Album> dataSet;
    FragmentHome.AlbumSelectedListener albumSelectedListener;

    public void setOnAlbumSelectedListener(FragmentHome.AlbumSelectedListener listener){
        albumSelectedListener = listener;
    }

    public AdapterAlbums(Context context){
        inflater = LayoutInflater.from(context);
        hasImagesToShow = false;
        thumbnailsReceived = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = inflater.inflate(R.layout.row_album_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d(TAG, "Element " + position + " set.");
        if (albums != null){
            viewHolder.getTextViewTitle().setText(albums.get(position).getTitulo());
            viewHolder.getTextViewDescription().setText(albums.get(position).getAlbumID());
        }
        if (!hasImagesToShow) {
            viewHolder.getImageViewAlbumThumbnail().setImageResource(R.mipmap.loading_image);
        }
        else {
            viewHolder.getImageViewAlbumThumbnail().setImageBitmap(albums.get(position).getThumbnail());
        }
    }

    @Override
    public int getItemCount() {
        if (albums == null){
            Log.d(TAG, "getItemCount: ALBUMS IS NULL");
            return 0;
        }
        return albums.size();
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    public List<Bitmap> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<Bitmap> thumbnails) {
        this.thumbnails = thumbnails;
        notifyDataSetChanged();
    }

    public void searchAlbumsThumbnails() {
        for (int i = 0; i < albums.size(); i++) {
            Bitmap image = FlickrApplication.getBitmapProvider()
                    .loadImageFromInternalStorage(albums.get(i).getFirstPhotoID()+"_q");
            albums.get(i).setThumbnail(image);
        }
        hasImagesToShow = true;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewAlbumThumbnail;
        private final TextView textViewTitle;
        private final TextView textViewDescription;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    albumSelectedListener.onAlbumSelected(albums.get(getAdapterPosition()));
                }
            });

            imageViewAlbumThumbnail = (ImageView) v.findViewById(R.id.imageViewAlbumThumbnail);
            textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
            textViewDescription = (TextView) v.findViewById(R.id.textViewDescription);
        }

        public ImageView getImageViewAlbumThumbnail() { return imageViewAlbumThumbnail; }

        public TextView getTextViewTitle() {
            return textViewTitle;
        }

        public TextView getTextViewDescription() {
            return textViewDescription;
        }
    }
}
