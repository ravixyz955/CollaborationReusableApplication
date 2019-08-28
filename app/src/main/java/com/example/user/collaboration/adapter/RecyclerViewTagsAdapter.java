package com.example.user.collaboration.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.collaboration.R;
import com.example.user.collaboration.network.model.CollaborationData;
import com.example.user.collaboration.network.model.FollowersData;
import com.pchmn.materialchips.ChipView;

import java.util.List;

public class RecyclerViewTagsAdapter extends RecyclerView.Adapter<RecyclerViewTagsAdapter.TagViewHolder> {
    private List<CollaborationData.Tags> tags;
    private List<FollowersData.Tags> followersTags;
    private Context mContext;

    public RecyclerViewTagsAdapter(Context context, List<CollaborationData.Tags> tags) {
        this.mContext = context;
        this.tags = tags;
    }

    public RecyclerViewTagsAdapter(Context context, List<FollowersData.Tags> tags, String name) {
        this.mContext = context;
        this.followersTags = tags;
    }

    @NonNull
    @Override
    public RecyclerViewTagsAdapter.TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_attachement_item, null);
        return new RecyclerViewTagsAdapter.TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTagsAdapter.TagViewHolder holder, int i) {
        if (tags != null && !tags.isEmpty()) {
            String tagName = tags.get(i).getFullName();
            holder.chipView.setLabel(tagName);
        } else if (followersTags != null && !followersTags.isEmpty()) {
            String tagName = followersTags.get(i).getFullName();
            holder.chipView.setLabel(tagName);
        }
    }

    @Override
    public int getItemCount() {
        if (tags != null)
            return tags.size();
        else if (followersTags != null)
            return followersTags.size();

        return 0;
    }


    public class TagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ChipView chipView;

        TagViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            chipView = itemView.findViewById(R.id.attachment_chip);
            chipView.setHasAvatarIcon(true);
            chipView.setAvatarIcon(mContext.getResources().getDrawable(R.drawable.ic_tag));
        }

        @Override
        public void onClick(View view) {

        }
    }
}