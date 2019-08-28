package com.example.user.collaboration.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ProgressSnapshot {

    @SerializedName("actual")
    private double actual;

    @SerializedName("planned")
    private double planned;

    @SerializedName("actual_progress")
    private String actual_progress;

    @SerializedName("planned_progress")
    private String planned_progress;

    @SerializedName("date")
    private Date date;

    public double getActual() {
        return actual;
    }

    public void setActual(double actual) {
        this.actual = actual;
    }

    public double getPlanned() {
        return planned;
    }

    public void setPlanned(double planned) {
        this.planned = planned;
    }

    public String getActual_progress() {
        return actual_progress;
    }

    public void setActual_progress(String actual_progress) {
        this.actual_progress = actual_progress;
    }

    public String getPlanned_progress() {
        return planned_progress;
    }

    public void setPlanned_progress(String planned_progress) {
        this.planned_progress = planned_progress;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
