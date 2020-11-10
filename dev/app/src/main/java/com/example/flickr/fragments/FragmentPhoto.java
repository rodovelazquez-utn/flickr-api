package com.example.flickr.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flickr.R;
import com.example.flickr.activities.FlickrApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPhoto extends Fragment {

    private ImageView imageViewPhoto;
    private Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        if(imageViewPhoto != null) {
            imageViewPhoto.setImageBitmap(bitmap);
        }
    }

    public FragmentPhoto() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imageViewPhoto = container.findViewById(R.id.imageViewPhoto);
        if (bitmap != null) {
            imageViewPhoto.setImageBitmap(bitmap);
        }

        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //imageViewPhoto.setImageBitmap(bitmap);
        imageViewPhoto = view.findViewById(R.id.imageViewPhoto);
        if (bitmap != null) {
            imageViewPhoto.setImageBitmap(bitmap);
        }
        registerForContextMenu(imageViewPhoto);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Opciones");
        menu.add(0, v.getId(), 0, "Guardar en favoritos");
        menu.add(0, v.getId(), 0, "Enviar url por mail");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // return super.onContextItemSelected(item);
        Toast.makeText(getActivity(),item.getTitle() + " clicked", Toast.LENGTH_LONG).show();
        return true;
    }
}
