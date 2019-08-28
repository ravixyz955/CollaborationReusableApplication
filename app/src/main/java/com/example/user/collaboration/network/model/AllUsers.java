package com.example.user.collaboration.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AllUsers implements Parcelable {
    public static final Creator<AllUsers> CREATOR = new Creator<AllUsers>() {
        @Override
        public AllUsers createFromParcel(Parcel source) {
            return new AllUsers(source);
        }

        @Override
        public AllUsers[] newArray(int size) {
            return new AllUsers[size];
        }
    };
    @SerializedName("users")
    private ArrayList<Users> users;
    @SerializedName("status")
    private String status;

    public AllUsers() {
    }

    protected AllUsers(Parcel in) {
        this.users = in.readParcelable(Users[].class.getClassLoader());
        this.status = in.readString();
    }

    public ArrayList<Users> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Users> users) {
        this.users = users;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(users);
        dest.writeString(this.status);
    }

    public static class Users implements Parcelable {
        public static final Creator<Users> CREATOR = new Creator<Users>() {
            @Override
            public Users createFromParcel(Parcel source) {
                return new Users(source);
            }

            @Override
            public Users[] newArray(int size) {
                return new Users[size];
            }
        };
        @SerializedName("is_admin")
        private String is_admin;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("is_active")
        private String is_active;
        @SerializedName("countryCode")
        private String countryCode;
        @SerializedName("permissions")
        private Permissions permissions;
        @SerializedName("__v")
        private String __v;
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("fullName")
        private String fullName;
        @SerializedName("_id")
        private String _id;
        @SerializedName("emailValidated")
        private String emailValidated;
        @SerializedName("email")
        private String email;
        @SerializedName("updatedAt")
        private String updatedAt;

        public Users() {
        }

        protected Users(Parcel in) {
            this.is_admin = in.readString();
            this.createdAt = in.readString();
            this.is_active = in.readString();
            this.countryCode = in.readString();
            this.permissions = in.readParcelable(Permissions.class.getClassLoader());
            this.__v = in.readString();
            this.mobile = in.readString();
            this.fullName = in.readString();
            this._id = in.readString();
            this.emailValidated = in.readString();
            this.email = in.readString();
            this.updatedAt = in.readString();
        }

        public String getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getIs_active() {
            return is_active;
        }

        public void setIs_active(String is_active) {
            this.is_active = is_active;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public Permissions getPermissions() {
            return permissions;
        }

        public void setPermissions(Permissions permissions) {
            this.permissions = permissions;
        }

        public String get__v() {
            return __v;
        }

        public void set__v(String __v) {
            this.__v = __v;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getEmailValidated() {
            return emailValidated;
        }

        public void setEmailValidated(String emailValidated) {
            this.emailValidated = emailValidated;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.is_admin);
            dest.writeString(this.createdAt);
            dest.writeString(this.is_active);
            dest.writeString(this.countryCode);
            dest.writeParcelable(this.permissions, flags);
            dest.writeString(this.__v);
            dest.writeString(this.mobile);
            dest.writeString(this.fullName);
            dest.writeString(this._id);
            dest.writeString(this.emailValidated);
            dest.writeString(this.email);
            dest.writeString(this.updatedAt);
        }
    }

    public static class Permissions implements Parcelable {
        public static final Creator<Permissions> CREATOR = new Creator<Permissions>() {
            @Override
            public Permissions createFromParcel(Parcel source) {
                return new Permissions(source);
            }

            @Override
            public Permissions[] newArray(int size) {
                return new Permissions[size];
            }
        };
        @SerializedName("is_admin")
        private String is_admin;
        @SerializedName("can_make_admin")
        private String can_make_admin;

        public Permissions() {
        }

        protected Permissions(Parcel in) {
            this.is_admin = in.readString();
            this.can_make_admin = in.readString();
        }

        public String getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
        }

        public String getCan_make_admin() {
            return can_make_admin;
        }

        public void setCan_make_admin(String can_make_admin) {
            this.can_make_admin = can_make_admin;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.is_admin);
            dest.writeString(this.can_make_admin);
        }
    }
}