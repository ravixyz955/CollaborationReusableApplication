package com.example.user.collaboration.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.user.collaboration.R;
import com.example.user.collaboration.adapter.UsersSuggestionsAdapter;
import com.example.user.collaboration.constants.Constants;
import com.example.user.collaboration.network.model.NamesList;
import com.example.user.collaboration.network.model.User;
import com.example.user.collaboration.network.service.CollaborationAPIService;
import com.example.user.collaboration.ui.CollaborationActivity;
import com.example.user.collaboration.utils.DataUtils;
import com.example.user.collaboration.utils.NetworkUtils;
import com.example.user.collaboration.utils.Utils;
import com.pchmn.materialchips.ChipView;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.example.user.collaboration.constants.Constants.ATTACH_FILE_REQUEST;
import static com.example.user.collaboration.constants.Constants.CAMERA_IMAGE_REQUEST;

public class CollabCreatePostFragment extends BottomSheetDialogFragment implements CollaborationTagUserFragment.TagUserInterface, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ChipsInput chipsInputVisibility;
    private Spinner spinnerUsersVisibility;
    private View lineview;
    private EditText edCreatePost;
    private boolean isTagVisible;
    private Uri uri;
    private LinearLayout containerAttachments;
    private BottomSheetBehavior behavior;
    private ArrayList<Uri> uris;
    private int fileCount = 0;
    private TextView title;
    private ProgressDialog progressDialog;
    private CollaborationAPIService collaborationAPIService;
    private LinearLayout chipsInputContainer;
    private LinearLayout chipsInputUserTags;
    private LinearLayout taggerUsersContainer;
    private CollaborationActivity context;
    private UsersSuggestionsAdapter suggestionsAdapter;
    private AutoCompleteTextView userSearchTerm;
    private ArrayList<User> users;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View view, int newState) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                dismiss();
                Intent intent = Objects.requireNonNull(((Activity) context)).getIntent();
                context.finish();
                startActivityForResult(intent, 101);
            }
        }

        @Override
        public void onSlide(@NonNull View view, float v) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    };

    public static CollabCreatePostFragment newInstance(List<User> usersList) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.USERS, (new ArrayList<>(usersList)));

        CollabCreatePostFragment fragment = new CollabCreatePostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (CollaborationActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod(getString(R.string.disablefileuriexposure));
                m.invoke(null);
            } catch (Exception e) {
                Log.e("FileUriExposure", e.getMessage());
            }
        }
        collaborationAPIService = NetworkUtils.provideCollaborationAPIService(context, "https://collab.");
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
                behavior.setPeekHeight(Utils.getScreenHeight() / 2);
            }
        });
        return inflater.inflate(R.layout.layout_createpost, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnSend = view.findViewById(R.id.btnSend);
        ImageView tag = view.findViewById(R.id.tag);
        ImageView attachment = view.findViewById(R.id.attachment);
        ImageView camera = view.findViewById(R.id.camera);
        TextView tvAddTags = view.findViewById(R.id.tv_add_tags);
        spinnerUsersVisibility = view.findViewById(R.id.spinner_users_visibility);
        lineview = view.findViewById(R.id.visibility_line);
        edCreatePost = view.findViewById(R.id.ed_createpost);
        title = view.findViewById(R.id.title);
        ImageView closePost = view.findViewById(R.id.close_post);
        chipsInputVisibility = view.findViewById(R.id.chipsinput_visibility);
        containerAttachments = view.findViewById(R.id.container_attachments);
        chipsInputContainer = view.findViewById(R.id.chipsinput_container);
        chipsInputUserTags = view.findViewById(R.id.chipsinput_user_tags);
        taggerUsersContainer = view.findViewById(R.id.container_user_tags);
        progressDialog = Utils.createProgressDialog(context);
        uris = new ArrayList<>();

        spinnerUsersVisibility.setOnItemSelectedListener(this);
        btnSend.setOnClickListener(this);
        closePost.setOnClickListener(this);
        tag.setOnClickListener(this);
        attachment.setOnClickListener(this);
        tvAddTags.setOnClickListener(this);
        containerAttachments.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        camera.setOnClickListener(this);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_search_users, null);
        RecyclerView resultsRecycler = dialogView.findViewById(R.id.users_search_results);
        userSearchTerm = dialogView.findViewById(R.id.user_search_term);
        assert getArguments() != null;
        users = getArguments().getParcelableArrayList(Constants.USERS);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        resultsRecycler.setLayoutManager(llm);
        suggestionsAdapter = new UsersSuggestionsAdapter(context, users);
        resultsRecycler.setAdapter(suggestionsAdapter);
        dialogBuilder.setView(dialogView);

        chipsInputVisibility.setFilterableList(getList());

        userSearchTerm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing to be done
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing to be done
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    searchUsers(s.toString());
                else
                    suggestionsAdapter.setUsers(users);
            }
        });

        suggestionsAdapter.setOnItemClickListener(new UsersSuggestionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(User user) {
                ChipView chipView = new ChipView(context);
                chipView.setLabel(user.getFullName());
                chipView.setHasAvatarIcon(true);
                chipView.setDeletable(true);
                taggerUsersContainer.addView(chipView);
                userSearchTerm.setText("");
            }
        });
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

    private List<? extends ChipInterface> getList() {
        ArrayList<NamesList> namesLists = new ArrayList<>();
        Bundle arguments = getArguments();
        assert arguments != null;
        title.setText(R.string.create_txt);
        ArrayList<User> usersList = arguments.getParcelableArrayList("users");
        assert usersList != null;
        if (!usersList.isEmpty()) {
            for (User user : usersList) {
                namesLists.add(new NamesList(user.getId(), null, user.getFullName(), null));
            }
        }
        return namesLists;
    }

    private List<? extends ChipInterface> getUsersSubList() {
        ArrayList<NamesList> namesLists = new ArrayList<>();
        Bundle arguments = getArguments();
        assert arguments != null;
        title.setText(R.string.create_txt);
        ArrayList<User> usersList = arguments.getParcelableArrayList("users");
        assert usersList != null;
        if (!usersList.isEmpty()) {
            for (int i = 0; i < 4; i++) {
                if (i == 3) {
                    int moreUsersCount = usersList.size() - i;
                    namesLists.add(new NamesList(usersList.get(i).getId(), null, "+ " + moreUsersCount +
                            " users", null));
                } else
                    namesLists.add(new NamesList(usersList.get(i).getId(), null, usersList.get(i).getFullName(),
                            null));
            }
        }
        return namesLists;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_add_tags) {
            CollaborationTagUserFragment collaborationTagUserFragment = CollaborationTagUserFragment.newInstance(users);
            collaborationTagUserFragment.setTagUserFragment(this);
            collaborationTagUserFragment.show(context.getSupportFragmentManager(), "Tag Users");
        } else if (view.getId() == R.id.close_post) {
            if (behavior != null) {
                behavior.setHideable(true);
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        } else if (view.getId() == R.id.btnSend) {
            if (validatePostFields())
                createCollaborationRequest();
        } else if (view.getId() == R.id.camera) {
            launchCamera();
        } else if (view.getId() == R.id.attachment) {
            launchFileChooser();
        } else if (view.getId() == R.id.tag) {
            if (isTagVisible) {
                chipsInputUserTags.setVisibility(View.GONE);
                isTagVisible = false;
            } else {
                chipsInputUserTags.setVisibility(View.VISIBLE);
                isTagVisible = true;
            }
        }
    }

    private boolean validatePostFields() {
        int count = 0;
        if (spinnerUsersVisibility.getSelectedItemPosition() == 0) {
            count++;
            spinnerUsersVisibility.requestFocus();
            TextView errorText = (TextView) spinnerUsersVisibility.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.dark_red));
            errorText.setText(R.string.visibility);
        } else if (spinnerUsersVisibility.getSelectedItem().toString().contains("Selected users")) {
            if (chipsInputVisibility.getSelectedChipList().isEmpty()) {
                count++;
                Utils.showToast(context, "Visibility users cannot be empty!");
            } else if (TextUtils.isEmpty(edCreatePost.getText().toString().trim())) {
                edCreatePost.requestFocus();
                edCreatePost.setError("Text content cannot be empty");
                count++;
            }
        } else if (TextUtils.isEmpty(edCreatePost.getText().toString().trim())) {
            edCreatePost.requestFocus();
            edCreatePost.setError("Text content cannot be empty");
            count++;
        }

        return count <= 0;
    }

    private void createCollaborationRequest() {
        if (progressDialog != null)
            progressDialog.show();

        List<MultipartBody.Part> files = new ArrayList<>();
        Map<String, RequestBody> map = new HashMap<>();

        map.put("title", createPartFromString(title.getText().toString()));
        map.put("text_content", createPartFromString(edCreatePost.getText().toString()));
        map.put("project", createPartFromString("5bdbe97149fd0846e3099264"));
        map.put("post_type", createPartFromString("post"));

        if (taggerUsersContainer.getChildCount() > 0) {
            map.put("tags", createPartFromString(getTagUserIds(taggerUsersContainer)));
            map.put("tag_length", createPartFromString(String.valueOf(taggerUsersContainer.getChildCount())));
        }

        if (spinnerUsersVisibility.getSelectedItemPosition() == 3) {
            if (chipsInputVisibility.getSelectedChipList().isEmpty()) {
                map.put("visibility", createPartFromString("selected-users"));
                map.put("selectedUsers", createPartFromString(getVisibilityTagIds(chipsInputVisibility.getSelectedChipList())));
            }
        } else {
            String visibility = null;
            if (spinnerUsersVisibility.getSelectedItemPosition() == 1)
                visibility = "all-org-users";
            else if (spinnerUsersVisibility.getSelectedItemPosition() == 2)
                visibility = "all-proj-users";
            map.put("visibility", createPartFromString(visibility));
        }

        if (!uris.isEmpty()) {
            for (Uri fileUri : uris) {
                files.add(prepareFilePart(fileUri));
            }
        }
        collaborationPostRequest(map, files);
    }

    @SuppressLint("LogNotTimber")
    private void collaborationPostRequest(Map<String, RequestBody> map, List<MultipartBody.Part> files) {
        collaborationAPIService.collaborationParamsPost(DataUtils.getToken(context), map, files)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getPostRequestsObserver());
    }

    private SingleObserver<? super Object> getPostRequestsObserver() {
        return new DisposableSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Utils.showToast(context, "Post created successfully!");
                if (behavior != null) {
                    behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.d("Failure:", "onFailure: " + e.getMessage());
                Utils.showToast(context, "Unable to process request!");
            }
        };
    }

    private String getVisibilityTagIds(List<? extends ChipInterface> selectedChipList) {
        StringBuilder tags = new StringBuilder();
        int count = 0;
        for (ChipInterface chipInterface : selectedChipList) {
            tags.append(chipInterface.getId());
            if (++count < selectedChipList.size())
                tags.append(",");
        }
        return tags.toString();
    }

    private String getTagUserIds(LinearLayout taggerUsersContainer) {
        StringBuilder tags = new StringBuilder();
        int count = 0;
        for (int i = 0; i < taggerUsersContainer.getChildCount(); i++) {
            String id = ((ChipView) taggerUsersContainer.getChildAt(i)).getTag().toString();
            tags.append(id);
            if (++count < taggerUsersContainer.getChildCount())
                tags.append(",");
        }
        return tags.toString();
    }

    private void launchFileChooser() {
        Intent fileIntent = new Intent();
        fileIntent.setAction(Intent.ACTION_GET_CONTENT);
        fileIntent.setType("*/*");
        fileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(fileIntent, "Select file"), ATTACH_FILE_REQUEST);
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        uri = Uri.fromFile(getOutputMediaFile());
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(takePictureIntent, CAMERA_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            ChipView chipView = new ChipView(context);
            chipView.setTag(fileCount);
            chipView.setDeletable(true);
            chipView.setHasAvatarIcon(true);
            chipView.setOnDeleteClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < containerAttachments.getChildCount(); i++) {
                        int attachmentTag = (int) containerAttachments.getChildAt(i).getTag();
                        if (attachmentTag == ((int) chipView.getTag()))
                            uris.remove(i);
                    }
                    containerAttachments.removeView(chipView);
                    if (containerAttachments.getChildCount() == 0)
                        chipsInputContainer.setVisibility(View.GONE);
                }
            });
            chipView.setLabel(new File(uri.toString()).getName());
            uris.add(uri);
            chipsInputContainer.setVisibility(View.VISIBLE);
            containerAttachments.addView(chipView);
            fileCount++;
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_CANCELED) {
            Utils.showToast(context, "Take Picture Failed or canceled");
        }

        if (requestCode == ATTACH_FILE_REQUEST && resultCode == Activity.RESULT_OK) {

            if (data != null && data.getClipData() != null) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    String filePath = Utils.getFilePath(data.getClipData().getItemAt(i).getUri(), context);
                    addChip(filePath);
                }
            } else if (data != null && data.getData() != null) {
                String path = Utils.getFilePath(data.getData(), context);
                addChip(path);
            }
        } else if (requestCode == ATTACH_FILE_REQUEST && resultCode == Activity.RESULT_CANCELED) {
            Utils.showToast(context, "Attach file Failed or canceled");
        }
    }

    private void addChip(String filePath) {
        Uri fileUri = Uri.fromFile(new File(filePath));
        uris.add(fileUri);
        ChipView chipView = new ChipView(context);
        chipView.setTag(fileCount);
        chipView.setDeletable(true);
        chipView.setHasAvatarIcon(true);

        chipView.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < containerAttachments.getChildCount(); i++) {
                    int chipViewTag = (int) containerAttachments.getChildAt(i).getTag();
                    if (chipViewTag == ((int) chipView.getTag()))
                        uris.remove(i);
                }
                containerAttachments.removeView(chipView);
                if (containerAttachments.getChildCount() == 0)
                    chipsInputContainer.setVisibility(View.GONE);
            }
        });
        chipView.setLabel(new File(fileUri.toString()).getName());
        chipsInputContainer.setVisibility(View.VISIBLE);
        containerAttachments.addView(chipView);
        fileCount++;
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Attachments");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            mediaStorageDir.mkdirs();
        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".png");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spinner_users_visibility) {
            if (spinnerUsersVisibility.getSelectedItemPosition() == 3) {
                chipsInputVisibility.setVisibility(View.VISIBLE);
                lineview.setVisibility(View.VISIBLE);
            } else {
                chipsInputVisibility.setVisibility(View.GONE);
                lineview.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // nothing to be done
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(Uri fileUri) {
        File file = new File(Objects.requireNonNull(Utils.getFilePath(fileUri, context)));
        RequestBody requestFile = RequestBody.create(MultipartBody.FORM, file);
        return MultipartBody.Part.createFormData("any", file.getName(), requestFile);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    @Override
    public void sendUsers(List<ChipView> chipViews) {
        if (!chipViews.isEmpty()) {
            for (ChipView chipView : chipViews) {
                if (chipView != null) {
                    ViewGroup parent = (ViewGroup) chipView.getParent();
                    if (parent != null) {
                        parent.removeView(chipView);
                    }
                }
                assert chipView != null;
                chipView.setDeletable(false);
                taggerUsersContainer.addView(chipView);
            }
        }
    }
}