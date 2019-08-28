package com.example.user.collaboration.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user.collaboration.MockData;
import com.example.user.collaboration.R;
import com.example.user.collaboration.adapter.CollaborationAdapter;
import com.example.user.collaboration.fragment.CollabCreatePostFragment;
import com.example.user.collaboration.network.model.AllUsers;
import com.example.user.collaboration.network.model.User;
import com.example.user.collaboration.network.service.ProjectAPIService;
import com.example.user.collaboration.utils.DataUtils;
import com.example.user.collaboration.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class CollaborationActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private static final int PERMISSION_ALL = 1;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CollaborationAdapter adapter;
    private ProjectAPIService projectAPIService;
    private FrameLayout mProgressBar;
    private List<AllUsers.Users> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaboration);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout.TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);

        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        FloatingActionButton fabAddComment = findViewById(R.id.show_add_comment_fab);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(tabLayoutOnPageChangeListener);
        tabLayout.addOnTabSelectedListener(CollaborationActivity.this);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new CollaborationAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        mProgressBar = findViewById(R.id.progress_bar);
        projectAPIService = NetworkUtils.provideProjectAPIService(this, "https://auth.");

        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_ALL);
        } else {
            setUsersList();
        }

        if (getSupportActionBar() != null) {
            setTabDivider();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Collaboration");
        }
        fabAddComment.setOnClickListener(this);
    }

    private void setTabDivider() {
        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                drawable.setColor(getResources().getColor(R.color.white, getTheme()));
            else
                drawable.setColor(getResources().getColor(R.color.white));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
    }

    private void setUsersList() {

/*
        projectAPIService.getUsers(DataUtils.getToken(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getUsersDisposableObserver());

*/
        List<AllUsers.Users> usersList = MockData.buildUsersData();
        mProgressBar.setVisibility(View.GONE);
        if (!usersList.isEmpty()) {
            setUsers(usersList);
            setCollaborationAdapter();
        }
    }

    private SingleObserver<? super AllUsers> getUsersDisposableObserver() {
        return new DisposableSingleObserver<AllUsers>() {

            @Override
            public void onSuccess(AllUsers allUsers) {
                mProgressBar.setVisibility(View.GONE);
                ArrayList<AllUsers.Users> collabUsers = allUsers.getUsers();
                if (!collabUsers.isEmpty())
                    setUsers(users);
                setCollaborationAdapter();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(CollaborationActivity.this, "Unable to process request!", Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
            }
        };
    }

    private void setCollaborationAdapter() {
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_ALL) {
            if (grantResults.length > 0) {
                setUsersList();
            } else {
                Toast.makeText(CollaborationActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.show_add_comment_fab) {
            CollabCreatePostFragment collabCreatePostFragment = CollabCreatePostFragment.newInstance(getUsersList());
            collabCreatePostFragment.show(getSupportFragmentManager(), "createpost");
        }
    }

    public List<User> getUsersList() {
        if (getUsers() != null) {
            List<AllUsers.Users> collabUsers = getUsers();
            ArrayList<User> usersList;
            if (!collabUsers.isEmpty()) {
                usersList = new ArrayList<>();
                for (int i = 0; i < collabUsers.size(); i++) {
                    User user = new User();
                    if (!DataUtils.getEmail(this).equalsIgnoreCase(collabUsers.get(i).getEmail()) &&
                            collabUsers.get(i).getEmail() != null) {
                        user.setId(collabUsers.get(i).get_id());
                        user.setFullName(collabUsers.get(i).getEmail().replaceAll("@.*", ""));
                        usersList.add(user);
                    }
                }
                return usersList;
            }
        }
        return Collections.emptyList();
    }

    public List<AllUsers.Users> getUsers() {
        return users;
    }

    public void setUsers(List<AllUsers.Users> users) {
        this.users = users;
    }
}