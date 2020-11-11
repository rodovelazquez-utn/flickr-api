package com.example.flickr.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flickr.R;
import com.example.flickr.activities.FlickrApplication;
import com.example.flickr.model.Comment;
import com.example.flickr.model.Photo;
import com.example.flickr.utils.AdapterComments;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentComments extends Fragment {

    private RecyclerView recyclerViewComments;
    private AdapterComments adapter;
    private GridLayoutManager layoutManager;
    private List<Comment> comments;
    private Photo photo;

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public RecyclerView getRecyclerViewComments() {
        return recyclerViewComments;
    }

    public void setRecyclerViewComments(RecyclerView recyclerViewComments) {
        this.recyclerViewComments = recyclerViewComments;
    }

    public AdapterComments getAdapter() {
        return adapter;
    }

    public void setAdapter(AdapterComments adapter) {
        this.adapter = adapter;
    }

    public GridLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public FragmentComments() {
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
        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);
        recyclerViewComments = rootView.findViewById(R.id.recyclerViewComments);

        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerViewComments.setAdapter(adapter);
        this.setRecyclerViewLayoutManager();

        FlickrApplication.getViewModel().getAllComments().
                observe(getActivity(), new Observer<List<Comment>>() {
                    @Override
                    public void onChanged(List<Comment> comments) {
                        adapter.setComments(comments);
                    }
                });
        FlickrApplication.getDataProvider().loadFlickrComments(adapter, photo.getPhotoID());

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerViewComments.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerViewComments.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        layoutManager = new GridLayoutManager(getActivity(), 1);

        recyclerViewComments.setLayoutManager(layoutManager);
        recyclerViewComments.scrollToPosition(scrollPosition);
    }

}
