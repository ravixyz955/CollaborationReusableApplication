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

import com.example.user.collaboration.MockData;
import com.example.user.collaboration.R;
import com.example.user.collaboration.adapter.CollaborationListAdapter;
import com.example.user.collaboration.network.model.CollaborationData;
import com.example.user.collaboration.network.model.User;
import com.example.user.collaboration.network.service.CollaborationAPIService;
import com.example.user.collaboration.utils.DataUtils;
import com.example.user.collaboration.utils.NetworkUtils;
import com.example.user.collaboration.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class MyPostsFragment extends Fragment {
    private RecyclerView mMyPostsRecyclerview;
    private FrameLayout mProgressBar;
    private CollaborationAPIService collaborationAPIService;
    private Context mContext;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = inflater.getContext();
        userId = new Gson().fromJson(DataUtils.getUser(mContext), User.class).getId();
        return inflater.inflate(R.layout.frag_myposts_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMyPostsRecyclerview = view.findViewById(R.id.mywall_recycler);
        mProgressBar = view.findViewById(R.id.progress_bar);
        collaborationAPIService = NetworkUtils.provideCollaborationAPIService(mContext, "https://collab.");
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.scrollToPosition(0);
        mMyPostsRecyclerview.setLayoutManager(llm);
        mMyPostsRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mMyPostsRecyclerview.setHasFixedSize(true);

        updateMyPosts();
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
    private void updateMyPosts() {
        List<CollaborationData> collaborationData = MockData.buildCommentsData();
        mProgressBar.setVisibility(View.GONE);
        if (!collaborationData.isEmpty()) {
            Collections.reverse(collaborationData);
            mMyPostsRecyclerview.setAdapter(new CollaborationListAdapter(mContext,
                    new ArrayList<>(collaborationData), MyWallFragment.class.getName()));
        } else {
            Utils.showToast(mContext, "No posts available!");
        }
/*
        collaborationAPIService.myPostsCollaboration(DataUtils.getToken(mContext), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMyPostsObserver());
*/
    }

    private SingleObserver<? super List<CollaborationData>> getMyPostsObserver() {
        return new DisposableSingleObserver<List<CollaborationData>>() {
            @Override
            public void onSuccess(List<CollaborationData> collaborationData) {
                mProgressBar.setVisibility(View.GONE);
                if (!collaborationData.isEmpty()) {
                    Collections.reverse(collaborationData);
                    mMyPostsRecyclerview.setAdapter(new CollaborationListAdapter(mContext, new ArrayList<>(collaborationData), MyPostsFragment.class.getName()));
                } else {
                    Utils.showToast(mContext, "No posts available!");
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