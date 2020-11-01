package com.example.flickr.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flickr.R;
import com.example.flickr.fragments.FragmentAlbum;
import com.example.flickr.model.Photo;

import java.util.List;

public class AdapterPhotos extends RecyclerView.Adapter<AdapterPhotos.ViewHolder> {
    private static final String TAG = "AdapterPhotos";
    private LayoutInflater inflater;
    private List<Photo> photos;
    FragmentAlbum.PhotoSelectedListener photoSelectedListener;

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setOnPhotoSelectedListener(FragmentAlbum.PhotoSelectedListener listener) {
        photoSelectedListener = listener;
    }

    public AdapterPhotos(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdapterPhotos.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = inflater.inflate(R.layout.row_photo_item, viewGroup, false);
        return new AdapterPhotos.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d(TAG, "Element " + position + " set.");
        if (photos != null){
            viewHolder.getTextViewNumber().setText(photos.get(position).getPhotoID());
        }
    }

    @Override
    public int getItemCount() {
        if (photos == null){
            Log.d(TAG, "getItemCount: PHOTOS IS NULL");
            return 0;
        }
        return photos.size();
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewNumber;
        private final ImageView imageViewThumbnail;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    photoSelectedListener.onPhotoSelected(photos.get(getAdapterPosition()));
                }
            });

            imageViewThumbnail = (ImageView) v.findViewById(R.id.imageViewThumbnail);
            textViewNumber = (TextView) v.findViewById(R.id.textViewNumber);
        }

        public ImageView getImageViewThumbnail() { return imageViewThumbnail; }

        public TextView getTextViewNumber() {
            return textViewNumber;
        }
    }

}
