package com.example.flickr.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flickr.R;
import com.example.flickr.model.Comment;

import java.util.List;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.ViewHolder> {

    private static final String TAG = "AdapterComments";
    private LayoutInflater inflater;
    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public AdapterComments(Context context) { inflater = LayoutInflater.from(context); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_comment_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "Element " + position + " set.");
        if (comments != null){
            holder.getTextViewUser().setText(comments.get(position).getAuthorName());
            holder.getTextViewCommentContent().setText(comments.get(position).getCommentText());
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewUser;
        private final TextView textViewCommentContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });

            textViewUser = itemView.findViewById(R.id.textViewUser);
            textViewCommentContent = itemView.findViewById(R.id.textViewCommentContent);
        }

        public TextView getTextViewUser() {
            return textViewUser;
        }

        public TextView getTextViewCommentContent() {
            return textViewCommentContent;
        }

    }
}
