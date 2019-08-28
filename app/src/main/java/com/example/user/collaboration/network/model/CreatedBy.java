package com.example.user.collaboration.network.model;

import com.google.gson.annotations.SerializedName;

public class CreatedBy {
    @SerializedName("email")
    private String email;

    @SerializedName("fullName")
    private String fullName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
