package com.example.user.collaboration.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CommentData {
    @SerializedName("_id")
    private String id;

    @SerializedName("created_by")
    private CreatedBy createdBy;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("body")
    private String body;

    @SerializedName("replies")
    private ArrayList<Replies> replies;

    @SerializedName("images")
    private ArrayList<String> imgUris;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ArrayList<Replies> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<Replies> replies) {
        this.replies = replies;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ArrayList<String> getImgUris() {
        return imgUris;
    }

    public void setImgUris(ArrayList<String> imgUris) {
        this.imgUris = imgUris;
    }
}