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
import com.example.flickr.fragments.FragmentHome;
import com.example.flickr.model.Album;

import java.util.List;

public class AdapterAlbums extends RecyclerView.Adapter<AdapterAlbums.ViewHolder> {

    private static final String TAG = "AdapterAlbums";
    private LayoutInflater inflater;
    private List<Album> albums;
    //private List<Album> dataSet;
    FragmentHome.AlbumSelectedListener albumSelectedListener;

    public void setOnAlbumSelectedListener(FragmentHome.AlbumSelectedListener listener){
        albumSelectedListener = listener;
    }

    public AdapterAlbums(Context context){
        inflater = LayoutInflater.from(context);
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
            viewHolder.getTextViewTitle().setText(albums.get(position).getAlbumID());
        }
    }

    @Override
    public int getItemCount() {
        if (albums == null){
            Log.d(TAG, "getItemCount: ALBUMS IS NULL");
            return 10000;
        }
        return albums.size();
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
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
