package com.example.user.collaboration.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.collaboration.R;
import com.example.user.collaboration.adapter.RecyclerViewAttachmentAdapter;
import com.example.user.collaboration.network.model.CollaborationData;
import com.example.user.collaboration.network.model.FollowersData;
import com.example.user.collaboration.utils.Utils;

import java.util.ArrayList;

public class CollabFilesDialogFragment extends BottomSheetDialogFragment {

    private BottomSheetBehavior behavior;

    public static CollabFilesDialogFragment newInstance(CollaborationData collaborationData) {

        Bundle args = new Bundle();
        args.putParcelable("collab_files", collaborationData);

        CollabFilesDialogFragment fragment = new CollabFilesDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CollabFilesDialogFragment newInstance(FollowersData followersData) {
        Bundle args = new Bundle();
        args.putParcelable("collab_files", followersData);

        CollabFilesDialogFragment fragment = new CollabFilesDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                View bottomSheetInternal = d.findViewById(android.support.design.R.id.design_bottom_sheet);
                assert bottomSheetInternal != null;
                behavior = BottomSheetBehavior.from(bottomSheetInternal);
                behavior.setPeekHeight(Utils.getScreenHeight() / 2);
                behavior.setSkipCollapsed(true);
            }
        });

        return inflater.inflate(R.layout.layout_files, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Resources resources = getResources();

        if (resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            assert getView() != null;
            View parent = (View) getView().getParent();
            parent.setBackground(getResources().getDrawable(R.drawable.bottom_sheet_corner));
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
            layoutParams.height = Utils.getScreenHeight();
            layoutParams.setMargins(resources.getDimensionPixelSize(R.dimen.bottom_sheet_margin_left), 0, resources.getDimensionPixelSize(R.dimen.bottom_sheet_margin_right), 0);
            parent.setLayoutParams(layoutParams);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog);
        return super.onCreateDialog(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewAttachments = view.findViewById(R.id.recyclerview_attachments);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        Bundle arguments = getArguments();
        assert arguments != null;
        /*CollaborationData collabTags = arguments.getParcelable("collab_files");

        recyclerViewAttachments.setLayoutManager(staggeredGridLayoutManager);
        assert collabTags != null;
        collabTags.getAttachments().remove(0);
        recyclerViewAttachments.setAdapter(new RecyclerViewAttachmentAdapter(getActivity(), collabTags.getAttachments()));*/
        if (arguments.getParcelable("collab_files") instanceof CollaborationData) {
            CollaborationData collabTags = arguments.getParcelable("collab_files");
            recyclerViewAttachments.setLayoutManager(staggeredGridLayoutManager);
            assert collabTags != null;
//            collabTags.getAttachments().remove(0);
            recyclerViewAttachments.setAdapter(new RecyclerViewAttachmentAdapter(getActivity(), collabTags.getAttachments()));
        } else if (arguments.getParcelable("collab_files") instanceof FollowersData) {
            FollowersData followersTags = arguments.getParcelable("collab_files");
            recyclerViewAttachments.setLayoutManager(staggeredGridLayoutManager);
            assert followersTags != null;
//            followersTags.getAttachments().remove(0);
            recyclerViewAttachments.setAdapter(new RecyclerViewAttachmentAdapter(getActivity(), new ArrayList<>(followersTags.getAttachments()), CollabFilesDialogFragment.class.getName()));
        }
    }
}