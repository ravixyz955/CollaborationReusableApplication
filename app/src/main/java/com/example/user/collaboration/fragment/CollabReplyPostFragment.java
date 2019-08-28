package com.example.user.collaboration.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.collaboration.R;
import com.example.user.collaboration.network.model.NamesList;
import com.example.user.collaboration.network.model.User;
import com.example.user.collaboration.network.service.CollaborationAPIService;
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

public class CollabReplyPostFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private ChipsInput chipsinput_tag;
    private View tag_line;
    private EditText ed_createpost;
    private Button btnSend;
    private ImageView tag, attachment, camera;
    private boolean isTagVisible;
    private int CAMERA_IMAGE_REQUEST = 100;
    private int ATTACH_FILE_REQUEST = 101;
    private Uri uri;
    private LinearLayout container_attachments;
    private BottomSheetBehavior behavior;
    private ArrayList<Uri> uris;
    private int fileCount = 0;
    private TextView title;
    private ProgressDialog progressDialog;
    private CollaborationAPIService collaborationAPIService;
    private LinearLayout chipsinput_container;
    private ImageView close_post;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View view, int newState) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                dismiss();
                Intent intent = Objects.requireNonNull(getActivity()).getIntent();
                getActivity().finish();
                startActivity(intent);
            }
        }

        @Override
        public void onSlide(@NonNull View view, float v) {

        }
    };

    public static CollabReplyPostFragment newInstance(List<User> usersList, String parent) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("users", new ArrayList<>(usersList));
        args.putString("parent", parent);

        CollabReplyPostFragment fragment = new CollabReplyPostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod(getString(R.string.disablefileuriexposure));
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        collaborationAPIService = NetworkUtils.provideCollaborationAPIService(getActivity(), "https://collab.");
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
//                behavior.setSkipCollapsed(true);
            }
        });
        return inflater.inflate(R.layout.layout_replypost, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tag_line = view.findViewById(R.id.tag_line);
        ed_createpost = view.findViewById(R.id.ed_createpost);
        btnSend = view.findViewById(R.id.btnSend);
        tag = view.findViewById(R.id.tag);
        attachment = view.findViewById(R.id.attachment);
        close_post = view.findViewById(R.id.close_post);
        title = view.findViewById(R.id.title);
        camera = view.findViewById(R.id.camera);
        chipsinput_tag = view.findViewById(R.id.chipsinput_tag);
        container_attachments = view.findViewById(R.id.container_attachments);
        chipsinput_container = view.findViewById(R.id.chipsinput_container);
        progressDialog = Utils.createProgressDialog(getActivity());
        uris = new ArrayList<>();

        btnSend.setOnClickListener(this);
        close_post.setOnClickListener(this);
        tag.setOnClickListener(this);
        attachment.setOnClickListener(this);
        container_attachments.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        camera.setOnClickListener(this);

        chipsinput_tag.setFilterableList(getList());
    }

    private List<? extends ChipInterface> getList() {
        ArrayList<NamesList> namesLists = new ArrayList<>();
        Bundle arguments = getArguments();
        assert arguments != null;
        title.setText(R.string.reply_txt);
        ArrayList<User> usersList = arguments.getParcelableArrayList("users");
        assert usersList != null;
        if (!usersList.isEmpty()) {
            for (User user : usersList) {
                namesLists.add(new NamesList(user.getId(), null, user.getFullName(), null));
            }
        }
        return namesLists;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.close_post) {
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
                chipsinput_tag.setVisibility(View.GONE);
                tag_line.setVisibility(View.GONE);
                isTagVisible = false;
            } else {
                chipsinput_tag.setVisibility(View.VISIBLE);
                tag_line.setVisibility(View.VISIBLE);
                isTagVisible = true;
            }
        }
    }

    private boolean validatePostFields() {
        int count = 0;
        if (TextUtils.isEmpty(ed_createpost.getText().toString().trim())) {
            ed_createpost.requestFocus();
            ed_createpost.setError("Text content cannot be empty");
            count++;
        }

        if (count > 0)
            return false;
        else
            return true;
    }

    private void createCollaborationRequest() {
        if (progressDialog != null)
            progressDialog.show();

        List<MultipartBody.Part> files = new ArrayList<>();
        Map<String, RequestBody> map = new HashMap<>();

        map.put("title", createPartFromString(title.getText().toString()));
        map.put("text_content", createPartFromString(ed_createpost.getText().toString()));
        map.put("project", createPartFromString("5bdbe97149fd0846e3099264"));
        map.put("post_type", createPartFromString("reply"));
        assert getArguments() != null;
        map.put("parent", createPartFromString(getArguments().getString("parent")));

        if (chipsinput_tag.getSelectedChipList().size() > 0) {
            map.put("tags", createPartFromString(getTagIds(chipsinput_tag.getSelectedChipList())));
            map.put("tag_length", createPartFromString(String.valueOf(chipsinput_tag.getSelectedChipList().size())));
        }

        if (uris.size() > 0) {
            for (Uri uri : uris) {
                files.add(prepareFilePart(uri));
            }
        }
        collaborationPostRequest(map, files);
    }

    @SuppressLint("LogNotTimber")
    private void collaborationPostRequest(Map<String, RequestBody> map, List<MultipartBody.Part> files) {
        collaborationAPIService.collaborationParamsPost(DataUtils.getToken(getActivity()), map, files)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getReplyPostObserver());
    }

    private SingleObserver<? super Object> getReplyPostObserver() {
        return new DisposableSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Utils.showToast(getActivity(), "Reply created successfully!");
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
                Utils.showToast(getActivity(), "Unable to process request!");
            }
        };
    }

    private String getTagIds(List<? extends ChipInterface> selectedChipList) {
        StringBuilder tags = new StringBuilder();
        int count = 0;
        for (ChipInterface chipInterface : selectedChipList) {
            tags.append(chipInterface.getId());
            if (++count < selectedChipList.size())
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
            ChipView chipView = new ChipView(getActivity());
            chipView.setTag(fileCount);
            chipView.setDeletable(true);
            chipView.setHasAvatarIcon(true);
            chipView.setOnDeleteClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < container_attachments.getChildCount(); i++) {
                        int tag = (int) container_attachments.getChildAt(i).getTag();
                        if (tag == ((int) chipView.getTag()))
                            uris.remove(i);
                    }
                    container_attachments.removeView(chipView);
                    if (container_attachments.getChildCount() == 0)
                        chipsinput_container.setVisibility(View.GONE);
                }
            });
            chipView.setLabel(new File(uri.toString()).getName());
            uris.add(uri);
            chipsinput_container.setVisibility(View.VISIBLE);
            container_attachments.addView(chipView);
            fileCount++;
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_CANCELED) {
            Utils.showToast(getActivity(), "Take Picture Failed or canceled");
        }

        if (requestCode == ATTACH_FILE_REQUEST && resultCode == Activity.RESULT_OK) {

            if (data != null && data.getClipData() != null) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    String filePath = Utils.getFilePath(data.getClipData().getItemAt(i).getUri(), getActivity());
                    addChip(filePath);
                }
            } else if (data != null && data.getData() != null) {
                String path = Utils.getFilePath(data.getData(), getActivity());
                addChip(path);
            }
        } else if (requestCode == ATTACH_FILE_REQUEST && resultCode == Activity.RESULT_CANCELED) {
            Utils.showToast(getActivity(), "Attach file Failed or canceled");
        }
    }

    private void addChip(String filePath) {
        Uri uri = Uri.fromFile(new File(filePath));
        uris.add(uri);
        ChipView chipView = new ChipView(getActivity());
        chipView.setTag(fileCount);
        chipView.setDeletable(true);
        chipView.setHasAvatarIcon(true);

        chipView.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < container_attachments.getChildCount(); i++) {
                    int tag = (int) container_attachments.getChildAt(i).getTag();
                    if (tag == ((int) chipView.getTag()))
                        uris.remove(i);
                }
                container_attachments.removeView(chipView);
                if (container_attachments.getChildCount() == 0)
                    chipsinput_container.setVisibility(View.GONE);
            }
        });
        chipView.setLabel(new File(uri.toString()).getName());
        chipsinput_container.setVisibility(View.VISIBLE);
        container_attachments.addView(chipView);
        fileCount++;
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Attachments");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs())
                mediaStorageDir.mkdirs();
        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".png");
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(Uri fileUri) {
        File file = new File(Objects.requireNonNull(Utils.getFilePath(fileUri, getActivity())));
        RequestBody requestFile = RequestBody.create(MultipartBody.FORM, file);
        return MultipartBody.Part.createFormData("any", file.getName(), requestFile);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }
}