package com.example.user.collaboration.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.user.collaboration.MockData;
import com.example.user.collaboration.R;
import com.example.user.collaboration.adapter.CollaborationListAdapter;
import com.example.user.collaboration.network.model.CollaborationData;
import com.example.user.collaboration.network.model.FollowersData;
import com.example.user.collaboration.network.model.User;
import com.example.user.collaboration.network.service.CollaborationAPIService;
import com.example.user.collaboration.utils.DataUtils;
import com.example.user.collaboration.utils.NetworkUtils;
import com.example.user.collaboration.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class FollowersFragment extends Fragment {
    private RecyclerView followersRecyclerView;
    private FrameLayout mProgressBar;
    private CollaborationAPIService collaborationAPIService;
    private Context mContext;
    private String userId;
    private TextView nofollowers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = inflater.getContext();
        userId = new Gson().fromJson(DataUtils.getUser(mContext), User.class).getId();
        return inflater.inflate(R.layout.frag_followers_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        followersRecyclerView = view.findViewById(R.id.followers_recycler);
        mProgressBar = view.findViewById(R.id.progress_bar);
        nofollowers = view.findViewById(R.id.nofollowers);
        collaborationAPIService = NetworkUtils.provideCollaborationAPIService(mContext, "https://collab.");
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.scrollToPosition(0);
        followersRecyclerView.setLayoutManager(llm);
        followersRecyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL));
        followersRecyclerView.setHasFixedSize(true);

        updateFollowers();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @SuppressLint("LogNotTimber")
    private void updateFollowers() {
        List<FollowersData> followersData = MockData.buildFollowersData();
        mProgressBar.setVisibility(View.GONE);
        if (!followersData.isEmpty()) {
            Collections.reverse(followersData);
            followersRecyclerView.setAdapter(new CollaborationListAdapter(mContext, followersData, FollowersFragment.class.getName()));
        } else {
            followersRecyclerView.setVisibility(View.GONE);
            nofollowers.setVisibility(View.VISIBLE);
        }
/*
        collaborationAPIService.followersCollaboration(DataUtils.getToken(mContext), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getFollowersObservers());
*/
    }

    private SingleObserver<? super List<CollaborationData>> getFollowersObservers() {
        return new DisposableSingleObserver<List<CollaborationData>>() {
            @Override
            public void onSuccess(List<CollaborationData> collaborationData) {
                mProgressBar.setVisibility(View.GONE);
                if (!collaborationData.isEmpty()) {
                    Collections.reverse(collaborationData);
                    followersRecyclerView.setAdapter(new CollaborationListAdapter(mContext, new ArrayList<>(collaborationData), FollowersFragment.class.getName()));
                } else {
                    followersRecyclerView.setVisibility(View.GONE);
                    nofollowers.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable e) {
                mProgressBar.setVisibility(View.GONE);
                Utils.showToast(mContext, "Unable to process request!");
            }
        };
    }
}