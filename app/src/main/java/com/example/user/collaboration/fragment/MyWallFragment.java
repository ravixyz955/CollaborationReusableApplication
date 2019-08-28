package com.example.user.collaboration.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.user.collaboration.MockData;
import com.example.user.collaboration.R;
import com.example.user.collaboration.adapter.CollaborationListAdapter;
import com.example.user.collaboration.network.model.CollaborationData;
import com.example.user.collaboration.network.service.CollaborationAPIService;
import com.example.user.collaboration.utils.NetworkUtils;
import com.example.user.collaboration.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class MyWallFragment extends Fragment {
    private RecyclerView mMyWallRecyclerview;
    private FrameLayout mProgressBar;
    private CollaborationAPIService collaborationAPIService;
    private Context mContext;
    private String id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = inflater.getContext();
        return inflater.inflate(R.layout.frag_mywall_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMyWallRecyclerview = view.findViewById(R.id.mywall_recycler);
        mProgressBar = view.findViewById(R.id.progress_bar);
        collaborationAPIService = NetworkUtils.provideCollaborationAPIService(mContext, "https://collab.");
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.scrollToPosition(0);
        llm.setSmoothScrollbarEnabled(true);
        mMyWallRecyclerview.setLayoutManager(llm);
        mMyWallRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mMyWallRecyclerview.setHasFixedSize(true);

        if (((Activity) mContext).getIntent().hasExtra("id")) {
            id = ((Activity) mContext).getIntent().getStringExtra("id");
        }
        updatemyWallView();
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
    private void updatemyWallView() {
        List<CollaborationData> collaborationData = MockData.buildCommentsData();
        mProgressBar.setVisibility(View.GONE);
        if (!collaborationData.isEmpty()) {
            Collections.reverse(collaborationData);
            mMyWallRecyclerview.setAdapter(new CollaborationListAdapter(mContext,
                    new ArrayList<>(collaborationData), MyWallFragment.class.getName()));
        } else {
            Utils.showToast(mContext, "No posts available!");
        }
/*
        collaborationAPIService.myWallCollaboration(DataUtils.getToken(mContext), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWallPostsObserver());
*/
    }

    private SingleObserver<? super List<CollaborationData>> getWallPostsObserver() {
        return new DisposableSingleObserver<List<CollaborationData>>() {
            @Override
            public void onSuccess(List<CollaborationData> collaborationData) {
                mProgressBar.setVisibility(View.GONE);
                if (!collaborationData.isEmpty()) {
                    Collections.reverse(collaborationData);
                    mMyWallRecyclerview.setAdapter(new CollaborationListAdapter(mContext,
                            new ArrayList<>(collaborationData), MyWallFragment.class.getName()));
                } else {
                    Utils.showToast(mContext, "No posts available!");
                }
            }

            @Override
            public void onError(Throwable e) {
                mProgressBar.setVisibility(View.GONE);
                Log.d("Error: ", "onFailure: " + e.getMessage());
                Utils.showToast(mContext, "Unable to process request!");
            }
        };
    }
}
