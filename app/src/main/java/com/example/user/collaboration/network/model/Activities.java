package com.example.user.collaboration.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Activities {

    @SerializedName("id")
    private String id;
    @SerializedName("progress")
    private Progress progress;
    @SerializedName("activities")
    private ArrayList<Activities> activities;
    @SerializedName("resources")
    private List<String> resources;
    @SerializedName("tags")
    private List<String> tags;
    @SerializedName("weightage")
    private String weightage;
    @SerializedName("scope")
    private Scope scope;
    @SerializedName("duration")
    private String duration;
    @SerializedName("manpower")
    private List<String> manpower;
    @SerializedName("predecessors")
    private List<String> predecessors;
    @SerializedName("created_by")
    private String created_by;
    @SerializedName("owner")
    private String owner;
    @SerializedName("wbs_id")
    private String wbs_id;
    @SerializedName("project")
    private String project;
    @SerializedName("properties")
    private Properties properties;
    @SerializedName("name")
    private String name;
    @SerializedName("planned_start_date")
    private String planned_start_date;
    @SerializedName("planned_finish_date")
    private String planned_finish_date;
    @SerializedName("actual_start_date")
    private String actual_start_date;
    @SerializedName("actual_finish_date")
    private String actual_finish_date;
    @SerializedName("__v")
    private String __v;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("_id")
    private String _id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public ArrayList<Activities> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activities> activities) {
        this.activities = activities;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getWeightage() {
        return weightage;
    }

    public void setWeightage(String weightage) {
        this.weightage = weightage;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<String> getManpower() {
        return manpower;
    }

    public void setManpower(List<String> manpower) {
        this.manpower = manpower;
    }

    public List<String> getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(List<String> predecessors) {
        this.predecessors = predecessors;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getWbs_id() {
        return wbs_id;
    }

    public void setWbs_id(String wbs_id) {
        this.wbs_id = wbs_id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanned_start_date() {
        return planned_start_date;
    }

    public void setPlanned_start_date(String planned_start_date) {
        this.planned_start_date = planned_start_date;
    }

    public String getPlanned_finish_date() {
        return planned_finish_date;
    }

    public void setPlanned_finish_date(String planned_finish_date) {
        this.planned_finish_date = planned_finish_date;
    }

    public String getActual_start_date() {
        return actual_start_date;
    }

    public void setActual_start_date(String actual_start_date) {
        this.actual_start_date = actual_start_date;
    }

    public String getActual_finish_date() {
        return actual_finish_date;
    }

    public void setActual_finish_date(String actual_finish_date) {
        this.actual_finish_date = actual_finish_date;
    }


    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public static class Progress {
        @SerializedName("date")
        private String date;
        @SerializedName("actual")
        private double actual;
        @SerializedName("planned")
        private double planned;
        @SerializedName("actual_progress")
        private String actual_progress;
        @SerializedName("planned_progress")
        private double planned_progress;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

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

        public double getPlanned_progress() {
            return planned_progress;
        }

        public void setPlanned_progress(double planned_progress) {
            this.planned_progress = planned_progress;
        }
    }

    public static class Scope {
        @SerializedName("quantity")
        private String quantity;
        @SerializedName("units")
        private String units;

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getUnits() {
            return units;
        }

        public void setUnits(String units) {
            this.units = units;
        }
    }

    public static class Snapshot {
        @SerializedName("_id")
        private String _id;
        @SerializedName("actual")
        private String actual;
        @SerializedName("planned")
        private String planned;
        @SerializedName("date")
        private String date;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getActual() {
            return actual;
        }

        public void setActual(String actual) {
            this.actual = actual;
        }

        public String getPlanned() {
            return planned;
        }

        public void setPlanned(String planned) {
            this.planned = planned;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public static class Properties {
        private String deliverabletype;
        private double weight;
        private String level;

        public String getDeliverabletype() {
            return deliverabletype;
        }

        public void setDeliverabletype(String deliverabletype) {
            this.deliverabletype = deliverabletype;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }
}