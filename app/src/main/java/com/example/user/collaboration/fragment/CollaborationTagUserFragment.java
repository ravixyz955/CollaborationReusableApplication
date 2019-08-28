package com.example.user.collaboration.fragment;

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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.collaboration.R;
import com.example.user.collaboration.adapter.UsersSuggestionsAdapter;
import com.example.user.collaboration.network.model.User;
import com.example.user.collaboration.utils.Utils;
import com.pchmn.materialchips.ChipView;

import java.util.ArrayList;
import java.util.List;

public class CollaborationTagUserFragment extends BottomSheetDialogFragment {
    private ArrayList<User> users;
    private UsersSuggestionsAdapter suggestionsAdapter;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0)
                searchUsers(s.toString());
            else
                suggestionsAdapter.setUsers(users);
        }
    };
    private LinearLayout ll_tagged_users;
    private TextView tv_empty_users;
    private ArrayList<ChipView> chipViews;
    UsersSuggestionsAdapter.OnItemClickListener onItemClickListener = new UsersSuggestionsAdapter.OnItemClickListener() {
        @Override
        public void onItemClickListener(User user) {
            ChipView chipView = new ChipView(getActivity());
            chipView.setLabel(user.getFullName());
            chipView.setTag(user.getId());
            chipView.setHasAvatarIcon(true);
            chipView.setDeletable(true);
            chipView.setOnDeleteClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ll_tagged_users.removeView(chipView);
                    if (chipViews.contains(chipView.getId()))
                        chipViews.remove(chipView);

                }
            });
            tv_empty_users.setVisibility(View.GONE);
            chipViews.add(chipView);
            ll_tagged_users.addView(chipView);
        }
    };
    private BottomSheetBehavior behavior;
    private TagUserInterface tagUserInterface;
    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!chipViews.isEmpty()) {
                tagUserInterface.sendUsers(chipViews);
            }
            getDialog().dismiss();
        }
    };

    public static CollaborationTagUserFragment newInstance(ArrayList<User> users) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("users", users);

        CollaborationTagUserFragment fragment = new CollaborationTagUserFragment();
        fragment.setArguments(args);
        return fragment;
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
                behavior.setPeekHeight(Utils.getScreenHeight());
            }
        });
        return inflater.inflate(R.layout.activity_search_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView resultsRecycler = view.findViewById(R.id.users_search_results);
        AutoCompleteTextView user_search_term = view.findViewById(R.id.user_search_term);
        ll_tagged_users = view.findViewById(R.id.ll_tagged_users);
        tv_empty_users = view.findViewById(R.id.tv_empty_users);
        FloatingActionButton fab_done = view.findViewById(R.id.fab_done);
        chipViews = new ArrayList<>();

        assert getArguments() != null;
        users = getArguments().getParcelableArrayList("users");
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        resultsRecycler.setLayoutManager(llm);
        suggestionsAdapter = new UsersSuggestionsAdapter(getActivity(), users);
        resultsRecycler.setAdapter(suggestionsAdapter);

        suggestionsAdapter.setOnItemClickListener(onItemClickListener);

        user_search_term.addTextChangedListener(textWatcher);

        fab_done.setOnClickListener(fabClickListener);
    }

    private void searchUsers(String searchText) {
        suggestionsAdapter.setUsers(filterResults(searchText));

    }

    private List<User> filterResults(String searchText) {
        ArrayList<User> usersList = new ArrayList<>();

        for (User user : users) {
            if (user.getFullName().contains(searchText))
                usersList.add(user);
        }
        return usersList;
    }

    public void setTagUserFragment(CollabCreatePostFragment collabCreatePostFragment) {
        this.tagUserInterface = collabCreatePostFragment;
    }

    public interface TagUserInterface {
        void sendUsers(List<ChipView> chipViews);
    }
}
