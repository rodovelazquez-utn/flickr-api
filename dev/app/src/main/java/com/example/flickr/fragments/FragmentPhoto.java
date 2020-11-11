package com.example.flickr.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flickr.R;
import com.example.flickr.activities.FlickrApplication;
import com.example.flickr.model.Photo;
import com.example.flickr.utils.AdapterComments;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPhoto extends Fragment {

    private ImageView imageViewPhoto;
    private Bitmap bitmap;
    private ImageButton imageButtonComments;
    private ImageButton imageButtonEmail;
    private FragmentManager fragmentManager;
    private FrameLayout frameLayoutFragments;
    private Photo photo;

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setFrameLayout(FrameLayout frameLayoutFragments) {
        this.frameLayoutFragments = frameLayoutFragments;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

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

        if (bitmap != null && imageViewPhoto != null) {
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

        imageButtonComments = view.findViewById(R.id.imageButtonComments);
        imageButtonEmail = view.findViewById(R.id.imageButtonEmail);

        imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeComments();
            }
        });

        imageButtonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        registerForContextMenu(imageViewPhoto);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Opciones");
        menu.add(0, v.getId(), 0, "Ver comentarios");
        menu.add(0, v.getId(), 1, "Enviar url por mail");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // return super.onContextItemSelected(item);
        //Toast.makeText(getActivity(),item.getTitle() + " clicked", Toast.LENGTH_LONG).show();
        if (item.getTitle().toString().equals("Ver comentarios")) {
            this.seeComments();
        }
        if (item.getTitle().toString().equals("Enviar url por mail")) {
            this.sendEmail();
        }
        return true;
    }

    private void seeComments() {
        FragmentComments fragmentComments = new FragmentComments();
        fragmentComments.setPhoto(photo);
        fragmentComments.setAdapter(new AdapterComments(getActivity()));
        FragmentTransaction fragTransaction = fragmentManager.beginTransaction();
        fragTransaction.addToBackStack(null);
        fragTransaction.replace(R.id.frameLayoutFragments, fragmentComments,
                "CommentFragment").commit();
    }

    private void sendEmail() {
        String url = "https://live.staticflickr.com/" + photo.getServer() + "/"
                + photo.getPhotoID() + "_" + photo.getSecret() + ".jpg";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Foto de Flickr");
        intent.putExtra(Intent.EXTRA_TEXT, "url: " + url);
        startActivity(Intent.createChooser(intent, "Enviar Email"));
    }
}
