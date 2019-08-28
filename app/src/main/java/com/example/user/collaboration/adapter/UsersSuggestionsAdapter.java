package com.example.user.collaboration.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.collaboration.R;
import com.example.user.collaboration.network.model.User;

import java.util.List;

public class UsersSuggestionsAdapter extends RecyclerView.Adapter<UsersSuggestionsAdapter.UserViewHolder> {

    private Context mContext;

    private List<User> users;

    private OnItemClickListener onItemClickListener;

    private User user = null;


    public UsersSuggestionsAdapter(Context mContext, List<User> users) {
        this.mContext = mContext;
        this.users = users;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.user_suggestion_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        user = users.get(position);
        holder.nameView.setText(user.getFullName());
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {

        public void onItemClickListener(User user);
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View itemView;

        TextView nameView;

        UserViewHolder(View v) {
            super(v);
            itemView = v;
            nameView = itemView.findViewById(R.id.user_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Utils.showToast(mContext, users.get(getAdapterPosition()).getFullName());
            onItemClickListener.onItemClickListener(users.get(getAdapterPosition()));
        }
    }
}