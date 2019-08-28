package com.example.user.collaboration.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.collaboration.R;
import com.example.user.collaboration.fragment.CollabFilesDialogFragment;
import com.example.user.collaboration.fragment.CollabTagsDialogFragment;
import com.example.user.collaboration.network.model.CollaborationData;
import com.example.user.collaboration.network.model.FollowersData;
import com.example.user.collaboration.network.service.CollaborationAPIService;
import com.example.user.collaboration.ui.CollaborationActivity;
import com.example.user.collaboration.utils.DataUtils;
import com.example.user.collaboration.utils.NetworkUtils;
import com.example.user.collaboration.utils.Utils;
import com.google.gson.Gson;
import com.pchmn.materialchips.ChipView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomRepliesDataView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private String createdAt;
    private TextView name;
    private TextView likes;
    private TextView dislikes;
    private TextView email;
    private TextView repliesTxt;
    private TextView time;
    private ChipView fileChip;
    private ChipView tagChip;
    private ImageView followers;
    private CollaborationData collaborationData;
    private CollaborationAPIService collaborationAPIService;

    private FollowersData followersData;

    public CustomRepliesDataView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public CustomRepliesDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRepliesDataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.customview_replies_item, this);
        collaborationAPIService = NetworkUtils.provideCollaborationAPIService(mContext, "https://collab.");

        fileChip = view.findViewById(R.id.file_chip);
        tagChip = view.findViewById(R.id.tag_chip);
        followers = view.findViewById(R.id.followers);
        name = view.findViewById(R.id.comment_name_txt);
        email = view.findViewById(R.id.comment_email_txt);
        likes = view.findViewById(R.id.likes);
        dislikes = view.findViewById(R.id.dislikes);
        repliesTxt = view.findViewById(R.id.comment_commentText);
        time = findViewById(R.id.comment_time_txt);
    }

    public void setData(CollaborationData collaborationData) {
        this.collaborationData = collaborationData;

        createdAt = collaborationData.getCreatedAt();
        name.setText(collaborationData.getOwner().getFullName());
        email.setText(collaborationData.getOwner().getEmail());
        likes.setText((String.valueOf(collaborationData.getLikes())));
        dislikes.setText(String.valueOf(collaborationData.getUnlikes()));
        time.setText(getTime(createdAt));
        repliesTxt.setText(collaborationData.getTextContent());
        if (collaborationData.getFollowers() != null && !collaborationData.getFollowers().isEmpty())
            followers.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow));
        followers.setOnClickListener(this);
        likes.setOnClickListener(this);
        dislikes.setOnClickListener(this);

        if (collaborationData.getTags() != null && !collaborationData.getTags().isEmpty()) {
            tagChip.setOnChipClicked(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    tagsDialog();
                }
            });
            if (collaborationData.getTags().size() > 1) {
                String tags = 1 + "+ more tags";
                tagChip.setLabel(tags);
            } else
                tagChip.setLabel("Error");
        } else
            tagChip.setLabel("No tags");


        if (collaborationData.getAttachments() != null) {
            if (!collaborationData.getAttachments().isEmpty()) {
                String attachmentName = collaborationData.getAttachments().get(0);

                if (collaborationData.getAttachments().size() > 1) {
                    String attachments = 1 + "+ more attachments";

                    SpannableString content = new SpannableString(attachments);
                    content.setSpan(new UnderlineSpan(), 0, attachments.length(), 0);
                    fileChip.setLabel(content.toString());
                    fileChip.setOnChipClicked(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            filesDialog();
                        }
                    });
                } else
                    fileChip.setLabel(new File(attachmentName).getName());
            } else
                fileChip.setLabel("No attachments");
        }
    }

    private String hashTagUsers(List<CollaborationData.Tags> tags) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        if (!tags.isEmpty()) {
            for (CollaborationData.Tags tag : tags) {
                if (count == 0) {
                    sb.append("#");
                    sb.append(tag.getFullName());
                    count++;
                } else {
                    sb.append(",");
                    sb.append(" " + "#");
                    sb.append(tag.getFullName());
                    count++;
                }
            }
            return sb.toString();
        } else {
            return sb.append("No tagged users").toString();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getTime(String createdAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        long timeVal = 0;
        try {
            timeVal = sdf.parse(createdAt).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long now = System.currentTimeMillis();

        CharSequence ago =
                DateUtils.getRelativeTimeSpanString(timeVal, now, DateUtils.MINUTE_IN_MILLIS);
        return String.valueOf(ago);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_many_files) {
            filesDialog();
        } else if (view.getId() == R.id.followers) {
            if (!collaborationData.getFollowers().isEmpty())
                makeUnfollow();
            else
                makeFollow();
        } else if (view.getId() == R.id.likes)
            makeLike();
        else if (view.getId() == R.id.dislikes)
            makeDislike();
    }

    private void makeDislike() {
        collaborationAPIService.getDislike(DataUtils.getToken(mContext), collaborationData.getId()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        dislikes.setText(String.valueOf(new JSONObject(new Gson().toJson(response.body()))
                                .getInt("unlikes")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.showToast(mContext, "Dislike failed");
                }
            }

            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.d("Failure:", "onFailure: " + t.getMessage());
            }
        });

    }

    private void makeLike() {
        collaborationAPIService.getLike(DataUtils.getToken(mContext), collaborationData.getId()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        likes.setText(String.valueOf(new JSONObject(new Gson().toJson(response.body()))
                                .getInt("likes")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.showToast(mContext, "Like failed");
                }
            }

            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.d("Failure:", "onFailure: " + t.getMessage());
            }
        });
    }

    private void makeFollow() {
        collaborationAPIService.getFollow(DataUtils.getToken(mContext), collaborationData.getId()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.isSuccessful()) {
                    if (!collaborationData.getFollowers().isEmpty())
                        followers.setImageDrawable(getResources().getDrawable(R.drawable.ic_unfollow));
                    else
                        followers.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow));
                } else {
                    if (!collaborationData.getFollowers().isEmpty())
                        Utils.showToast(mContext, "Unfollow failed");
                    else
                        Utils.showToast(mContext, "Follow failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Utils.showToast(mContext, "Unable to process request!");
            }
        });
    }

    private void makeUnfollow() {
        collaborationAPIService.getUnfollow(DataUtils.getToken(mContext), collaborationData.getId()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.isSuccessful()) {
                    if (!collaborationData.getFollowers().isEmpty())
                        followers.setImageDrawable(getResources().getDrawable(R.drawable.ic_unfollow));
                    else
                        followers.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow));
                } else {
                    if (!collaborationData.getFollowers().isEmpty())
                        Utils.showToast(mContext, "Unfollow failed");
                    else
                        Utils.showToast(mContext, "Follow failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Utils.showToast(mContext, "Unable to process request!");
            }
        });
    }

    private void filesDialog() {
        if (collaborationData != null) {
            CollabFilesDialogFragment collabFilesDialogFragment = CollabFilesDialogFragment.newInstance(collaborationData);
            collabFilesDialogFragment.show(((CollaborationActivity) mContext).getSupportFragmentManager(), "Files");
        } else {
            CollabFilesDialogFragment collabTagsDialogFragment = CollabFilesDialogFragment.newInstance(followersData);
            collabTagsDialogFragment.show(((CollaborationActivity) mContext).getSupportFragmentManager(), "Files");
        }
    }

    private void tagsDialog() {
        if (collaborationData != null) {
            CollabTagsDialogFragment collabTagsDialogFragment = CollabTagsDialogFragment.newInstance(collaborationData);
            collabTagsDialogFragment.show(((CollaborationActivity) mContext).getSupportFragmentManager(), "Tags");
        } else {
            CollabTagsDialogFragment collabTagsDialogFragment = CollabTagsDialogFragment.newInstance(followersData);
            collabTagsDialogFragment.show(((CollaborationActivity) mContext).getSupportFragmentManager(), "Tags");
        }
    }

    public void setData(FollowersData followersData) {
        this.followersData = followersData;
        createdAt = followersData.getCreatedAt();
        name.setText(followersData.getOwner().getFullName());
        email.setText(followersData.getOwner().getEmail());
        likes.setText((String.valueOf(followersData.getLikes())));
        dislikes.setText(String.valueOf(followersData.getUnlikes()));
        time.setText(getTime(createdAt));
        repliesTxt.setText(followersData.getTextContent());
        if (!followersData.getFollowers().isEmpty())
            followers.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow));
        followers.setOnClickListener(this);
        likes.setOnClickListener(this);
        dislikes.setOnClickListener(this);

        if (followersData.getTags() != null && !followersData.getTags().isEmpty()) {
            tagChip.setOnChipClicked(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    tagsDialog();
                }
            });
            if (followersData.getTags().size() > 1) {
                String tags = 1 + "+ more tags";
                tagChip.setLabel(tags);
            } else
                tagChip.setLabel("Error");
        } else
            tagChip.setLabel("No tags");


        if (followersData.getAttachments() != null) {
            if (!followersData.getAttachments().isEmpty()) {
                String attachmentName = followersData.getAttachments().get(0);

                if (followersData.getAttachments().size() > 1) {
                    String attachments = 1 + "+ more attachments";

                    SpannableString content = new SpannableString(attachments);
                    content.setSpan(new UnderlineSpan(), 0, attachments.length(), 0);
                    fileChip.setLabel(content.toString());
                    fileChip.setOnChipClicked(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            filesDialog();
                        }
                    });
                } else
                    fileChip.setLabel(new File(attachmentName).getName());
            } else
                fileChip.setLabel("No attachments");
        }
    }
}