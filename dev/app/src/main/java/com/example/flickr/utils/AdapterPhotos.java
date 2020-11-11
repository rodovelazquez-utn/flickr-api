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
import com.example.flickr.fragments.FragmentAlbum;
import com.example.flickr.model.Photo;

import java.util.List;

public class AdapterPhotos extends RecyclerView.Adapter<AdapterPhotos.ViewHolder> {
    private static final String TAG = "AdapterPhotos";
    private LayoutInflater inflater;
    private List<Photo> photos;
    private List<Bitmap> bitmaps;
    FragmentAlbum.PhotoSelectedListener photoSelectedListener;

    public void setHasImagesToShow(boolean hasImagesToShow) {
        this.hasImagesToShow = hasImagesToShow;
    }

    private boolean hasImagesToShow;
    private boolean thumbnailsReceived;

    public void setThumbnailsReceived(boolean thumbnailsReceived) {
        this.thumbnailsReceived = thumbnailsReceived;
    }

    public boolean getHasImagesToShow() {
        return hasImagesToShow;
    }

    public boolean getThumbnailsReceived() {
        return thumbnailsReceived;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public void setOnPhotoSelectedListener(FragmentAlbum.PhotoSelectedListener listener) {
        photoSelectedListener = listener;
    }

    public AdapterPhotos(Context context) {
        inflater = LayoutInflater.from(context);
        hasImagesToShow = false;
        thumbnailsReceived = false;
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
            viewHolder.getTextViewNumber().setText(photos.get(position).getTitle());
            viewHolder.getTextViewIdPhoto().setText(photos.get(position).getPhotoID());
        }
        if (!hasImagesToShow) {
            viewHolder.getImageViewThumbnail().setImageResource(R.mipmap.loading_image);
        }
        else {
            viewHolder.getImageViewThumbnail().setImageBitmap(photos.get(position).getThumbnail());
        }
        /*if (bitmaps != null) {
            if (bitmaps.get(position) != null) {
                searchPhotosThumbnails();
                viewHolder.getImageViewThumbnail().setImageBitmap(bitmaps.get(position));
            }
        }*/
    }

    @Override
    public int getItemCount() {
        if (photos == null){
            Log.d(TAG, "getItemCount: PHOTOS IS NULL");
            return 0;
        }
        return photos.size();
    }


    public void searchPhotosThumbnails() {
        for (int i = 0; i < photos.size(); i++) {
            Bitmap image = FlickrApplication.getBitmapProvider()
                    .loadImageFromInternalStorage(photos.get(i).getPhotoID()+"_q");
            photos.get(i).setThumbnail(image);
        }
        hasImagesToShow = true;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewNumber;
        private final ImageView imageViewThumbnail;
        private final TextView textViewIdPhoto;

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
            textViewIdPhoto = (TextView) v.findViewById(R.id.textViewIdPhoto);
        }

        public ImageView getImageViewThumbnail() { return imageViewThumbnail; }

        public TextView getTextViewNumber() {
            return textViewNumber;
        }

        public TextView getTextViewIdPhoto() {
            return textViewIdPhoto;
        }
    }

}
