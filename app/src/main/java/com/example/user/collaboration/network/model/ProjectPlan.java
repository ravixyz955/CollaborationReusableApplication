package com.example.user.collaboration.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProjectPlan {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("created_by")
    private Object createdBy;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("users")
    private ArrayList<User> users;

    @SerializedName("projects")
    private ArrayList<ProjectPlan> projects;

    @SerializedName("properties")
    private Ptype_Properties properties;

    @SerializedName("parent")
    private Object parent;

    @SerializedName("projectType")
    private String projectType;

    @SerializedName("myProjects")
    private int myProjects;

    @SerializedName("orthomosaics")
    private ArrayList<Object> orthomosaics;

    @SerializedName("activities")
    private ArrayList<Activities> activities;

    @SerializedName("progress")
    private ProgressSnapshot progress;

    @SerializedName("snapshot")
    private ArrayList<ProgressSnapshot> snapshot;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Activities> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activities> activities) {
        this.activities = activities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public int getMyProjects() {
        return myProjects;
    }

    public void setMyProjects(int myProjects) {
        this.myProjects = myProjects;
    }

    public ArrayList<ProjectPlan> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<ProjectPlan> projects) {
        this.projects = projects;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public ArrayList<Object> getOrthomosaics() {
        return orthomosaics;
    }

    public void setOrthomosaics(ArrayList<Object> orthomosaics) {
        this.orthomosaics = orthomosaics;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Ptype_Properties getProperties() {
        return properties;
    }

    public void setProperties(Ptype_Properties properties) {
        this.properties = properties;
    }

    public ProgressSnapshot getProgress() {
        return progress;
    }

    public void setProgress(ProgressSnapshot progress) {
        this.progress = progress;
    }

    public ArrayList<ProgressSnapshot> getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(ArrayList<ProgressSnapshot> snapshot) {
        this.snapshot = snapshot;
    }
}
