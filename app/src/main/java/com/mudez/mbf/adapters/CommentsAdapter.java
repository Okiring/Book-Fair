package com.mudez.mbf.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mudez.mbf.R;
import com.mudez.mbf.models.AgendaObject;
import com.mudez.mbf.models.CommentObject;
import com.mudez.mbf.ui.pages.AgendaViewActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CommentsAdapter  extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private Context context;
    private List<CommentObject> commentObjectList;

    public CommentsAdapter(Context context, List<CommentObject> commentObjectList){
        this.context = context;
        this.commentObjectList = commentObjectList;
    }


    public void setCommentObjectList(List<CommentObject> commentObjectList) {
        this.commentObjectList = commentObjectList;
    }

    public List<CommentObject> getCommentObjectList() {
        return commentObjectList;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.comment_item,parent,false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, final int position) {

        holder.getComment().setText(commentObjectList.get(position).comment);


    }

    @Override
    public int getItemCount() {
        return commentObjectList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView comment;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.commentText);

        }

        public TextView getComment() {
            return comment;
        }
    }
}

