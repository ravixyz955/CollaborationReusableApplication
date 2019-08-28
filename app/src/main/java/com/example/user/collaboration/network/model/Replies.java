package com.example.user.collaboration.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Replies {
    @SerializedName("_id")
    private String id;

    @SerializedName("created_by")
    private CreatedBy createdBy;

    @SerializedName("body")
    private String body;

    @SerializedName("createdAt")
    private String createdAt;

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