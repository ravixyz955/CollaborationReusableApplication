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
import com.example.user.collaboration.adapter.RecyclerViewTagsAdapter;
import com.example.user.collaboration.network.model.CollaborationData;
import com.example.user.collaboration.network.model.FollowersData;
import com.example.user.collaboration.utils.Utils;

public class CollabTagsDialogFragment extends BottomSheetDialogFragment {
    private BottomSheetBehavior behavior;

    public static CollabTagsDialogFragment newInstance(CollaborationData collaborationData) {

        Bundle args = new Bundle();
        args.putParcelable("collab_tags", collaborationData);

        CollabTagsDialogFragment fragment = new CollabTagsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CollabTagsDialogFragment newInstance(FollowersData followersData) {
        Bundle args = new Bundle();
        args.putParcelable("collab_tags", followersData);

        CollabTagsDialogFragment fragment = new CollabTagsDialogFragment();
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

        return inflater.inflate(R.layout.layout_tags, container);
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

        RecyclerView recyclerViewTags = view.findViewById(R.id.recyclerview_tags);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        Bundle arguments = getArguments();
        assert arguments != null;
        if (arguments.getParcelable("collab_tags") instanceof CollaborationData) {
            CollaborationData collabFiles = arguments.getParcelable("collab_tags");
            recyclerViewTags.setLayoutManager(staggeredGridLayoutManager);
            assert collabFiles != null;
            collabFiles.getTags().remove(0);
            recyclerViewTags.setAdapter(new RecyclerViewTagsAdapter(getActivity(), collabFiles.getTags()));
        } else {
            FollowersData followersFiles = arguments.getParcelable("collab_tags");
            recyclerViewTags.setLayoutManager(staggeredGridLayoutManager);
            assert followersFiles != null;
            followersFiles.getTags().remove(0);
            recyclerViewTags.setAdapter(new RecyclerViewTagsAdapter(getActivity(), followersFiles.getTags(), CollabTagsDialogFragment.class.getName()));
        }
    }
}