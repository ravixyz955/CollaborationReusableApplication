package com.example.user.collaboration.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.user.collaboration.R;
import com.pchmn.materialchips.ChipView;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class RecyclerViewAttachmentAdapter extends RecyclerView.Adapter<RecyclerViewAttachmentAdapter.AttachmentViewHolder> {
    private ArrayList<String> attachments;
    private Context mContext;

    public RecyclerViewAttachmentAdapter(Context context, ArrayList<String> attachments) {
        this.mContext = context;
        this.attachments = attachments;

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public RecyclerViewAttachmentAdapter(Context context, ArrayList<String> attachments, String name) {
        this.mContext = context;
        this.attachments = attachments;

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_attachement_item, null);
        return new AttachmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentViewHolder holder, int i) {
        String attachmentName = attachments.get(i);
        holder.chipView.setLabel(new File(attachmentName).getName());
        holder.chipView.setTag(attachmentName);
        if (attachmentName.contains("png") ||
                attachmentName.contains("PNG") ||
                attachmentName.contains("jpeg") ||
                attachmentName.contains("jpg"))
            holder.chipView.setAvatarIcon(mContext.getResources().getDrawable(R.drawable.ic_image_24dp));
        else if (attachmentName.contains("txt"))
            holder.chipView.setAvatarIcon(mContext.getResources().getDrawable(R.drawable.ic_text_24dp));
        else if (attachmentName.contains("pdf"))
            holder.chipView.setAvatarIcon(mContext.getResources().getDrawable(R.drawable.ic_pdf_24dp));
        else
            holder.chipView.setAvatarIcon(mContext.getResources().getDrawable(R.drawable.ic_others_24dp));
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    private void openFile(String path) {
        File tempFile = new File(path);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), getMimeType(tempFile.getAbsolutePath()));
        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No Application found to open this type of file.", Toast.LENGTH_LONG).show();
        }
    }

    private String getMimeType(String url) {
        String[] parts;
        parts = url.split("\\.");
        String extension = parts[parts.length - 1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public class AttachmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ChipView chipView;

        AttachmentViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            chipView = itemView.findViewById(R.id.attachment_chip);
            chipView.setOnChipClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFile(String.valueOf(chipView.getTag()));
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}