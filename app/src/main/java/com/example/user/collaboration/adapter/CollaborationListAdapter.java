package com.example.user.collaboration.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.collaboration.network.model.CollaborationData;
import com.example.user.collaboration.network.model.FollowersData;
import com.example.user.collaboration.widgets.CustomCommentDataView;

import java.util.ArrayList;
import java.util.List;


public class CollaborationListAdapter extends RecyclerView.Adapter<CollaborationListAdapter.CommentsViewHolder> {
    private Context mContext;
    private List<CollaborationData> collaborationData;
    private List<FollowersData> followersData;

    public CollaborationListAdapter(Context mContext, ArrayList<CollaborationData> collaborationData, String name) {
        this.mContext = mContext;
        this.collaborationData = collaborationData;
    }

    public CollaborationListAdapter(Context mContext, List<FollowersData> followersData, String name) {
        this.mContext = mContext;
        this.followersData = followersData;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomCommentDataView itemView = new CustomCommentDataView(parent.getContext());
        itemView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new CommentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        if (collaborationData != null && !collaborationData.isEmpty()) {
            holder.getCustomView().setData(this.collaborationData.get(position), position);
        } else if (followersData != null && !followersData.isEmpty()) {
            holder.getCustomView().setData(this.followersData.get(position), position);
        }
        if (((Activity) mContext).getIntent().hasExtra("commentPosition")) {
            if (position == ((Activity) mContext).getIntent().getIntExtra("commentPosition", 0)) {
                holder.getCustomView().setBackgroundColor(Color.parseColor("#FFFF00"));
            } else {
                holder.getCustomView().setBackgroundColor(Color.WHITE);
            }
        } else {
            holder.getCustomView().setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (collaborationData != null && !collaborationData.isEmpty())
            return this.collaborationData.size();
        else if (followersData != null && !followersData.isEmpty())
            return this.followersData.size();

        return 0;
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {

        private CustomCommentDataView customView;

        public CommentsViewHolder(View v) {
            super(v);
            customView = (CustomCommentDataView) v;
        }

        public CustomCommentDataView getCustomView() {
            return customView;
        }
    }
}