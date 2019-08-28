package com.example.user.collaboration.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CollaborationData implements Parcelable {

    public static final Creator<CollaborationData> CREATOR = new Creator<CollaborationData>() {
        @Override
        public CollaborationData createFromParcel(Parcel source) {
            return new CollaborationData(source);
        }

        @Override
        public CollaborationData[] newArray(int size) {
            return new CollaborationData[size];
        }
    };
    @SerializedName("likes")
    private int likes;
    @SerializedName("unlikes")
    private int unlikes;
    @SerializedName("_id")
    private String id;
    @SerializedName("project")
    private String project;
    @SerializedName("post_type")
    private String postType;
    @SerializedName("title")
    private String title;
    @SerializedName("text_content")
    private String textContent;
    @SerializedName("visibility")
    private String visibility;
    @SerializedName("owner")
    private Owner owner;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("__v")
    private int v;
    @SerializedName("followers")
    private List<Followers> followers;
    @SerializedName("selectedUsers")
    private List<String> selectedUsers;
    @SerializedName("attachments")
    private ArrayList<String> attachments;
    @SerializedName("tags")
    private List<Tags> tags;
    @SerializedName("replies")
    private List<CollaborationData> replies;

    public CollaborationData() {
    }

    protected CollaborationData(Parcel in) {
        this.likes = in.readInt();
        this.unlikes = in.readInt();
        this.id = in.readString();
        this.project = in.readString();
        this.postType = in.readString();
        this.title = in.readString();
        this.textContent = in.readString();
        this.visibility = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.v = in.readInt();
        this.followers = in.createTypedArrayList(Followers.CREATOR);
        this.selectedUsers = in.createStringArrayList();
        this.attachments = in.createStringArrayList();
        this.tags = new ArrayList<>();
        in.readList(this.tags, Object.class.getClassLoader());
        this.replies = in.createTypedArrayList(CollaborationData.CREATOR);
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getUnlikes() {
        return unlikes;
    }

    public void setUnlikes(int unlikes) {
        this.unlikes = unlikes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
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

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public List<Followers> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Followers> followers) {
        this.followers = followers;
    }

    public List<String> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<String> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public ArrayList<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<String> attachments) {
        this.attachments = attachments;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public List<CollaborationData> getReplies() {
        return replies;
    }

    public void setReplies(List<CollaborationData> replies) {
        this.replies = replies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.likes);
        dest.writeInt(this.unlikes);
        dest.writeString(this.id);
        dest.writeString(this.project);
        dest.writeString(this.postType);
        dest.writeString(this.title);
        dest.writeString(this.textContent);
        dest.writeString(this.visibility);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeInt(this.v);
        dest.writeTypedList(this.followers);
        dest.writeStringList(this.selectedUsers);
        dest.writeStringList(this.attachments);
        dest.writeList(this.tags);
        dest.writeTypedList(this.replies);
    }

    public static class Owner implements Parcelable {

        public static final Creator<Owner> CREATOR = new Creator<Owner>() {
            @Override
            public Owner createFromParcel(Parcel source) {
                return new Owner(source);
            }

            @Override
            public Owner[] newArray(int size) {
                return new Owner[size];
            }
        };
        @SerializedName("countryCode")
        private String countryCode;
        @SerializedName("is_active")
        private boolean isActive;
        @SerializedName("emailValidated")
        private boolean emailValidated;
        @SerializedName("is_admin")
        private boolean isAdmin;
        @SerializedName("permissions")
        private Owner_Permissions permissions;
        @SerializedName("_id")
        private String id;
        @SerializedName("updatedAt")
        private String updatedAt;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("email")
        private String email;
        @SerializedName("fullName")
        private String fullName;
        @SerializedName("mobile")
        private long mobile;
        @SerializedName("__v")
        private int v;

        public Owner() {
        }

        protected Owner(Parcel in) {
            this.countryCode = in.readString();
            this.isActive = in.readByte() != 0;
            this.emailValidated = in.readByte() != 0;
            this.isAdmin = in.readByte() != 0;
            this.permissions = in.readParcelable(Owner_Permissions.class.getClassLoader());
            this.id = in.readString();
            this.updatedAt = in.readString();
            this.createdAt = in.readString();
            this.email = in.readString();
            this.fullName = in.readString();
            this.mobile = in.readLong();
            this.v = in.readInt();
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public boolean isIsActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }

        public boolean isEmailValidated() {
            return emailValidated;
        }

        public void setEmailValidated(boolean emailValidated) {
            this.emailValidated = emailValidated;
        }

        public boolean isIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public Owner_Permissions getPermissions() {
            return permissions;
        }

        public void setPermissions(Owner_Permissions permissions) {
            this.permissions = permissions;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

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

        public long getMobile() {
            return mobile;
        }

        public void setMobile(long mobile) {
            this.mobile = mobile;
        }

        public int getV() {
            return v;
        }

        public void setV(int v) {
            this.v = v;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.countryCode);
            dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
            dest.writeByte(this.emailValidated ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isAdmin ? (byte) 1 : (byte) 0);
            dest.writeParcelable(this.permissions, flags);
            dest.writeString(this.id);
            dest.writeString(this.updatedAt);
            dest.writeString(this.createdAt);
            dest.writeString(this.email);
            dest.writeString(this.fullName);
            dest.writeLong(this.mobile);
            dest.writeInt(this.v);
        }

        public static class Owner_Permissions implements Parcelable {

            public static final Creator<Owner_Permissions> CREATOR = new Creator<Owner_Permissions>() {
                @Override
                public Owner_Permissions createFromParcel(Parcel source) {
                    return new Owner_Permissions(source);
                }

                @Override
                public Owner_Permissions[] newArray(int size) {
                    return new Owner_Permissions[size];
                }
            };
            @SerializedName("is_admin")
            private boolean isAdmin;
            @SerializedName("can_make_admin")
            private boolean canMakeAdmin;

            public Owner_Permissions() {
            }

            protected Owner_Permissions(Parcel in) {
                this.isAdmin = in.readByte() != 0;
                this.canMakeAdmin = in.readByte() != 0;
            }

            public boolean isIsAdmin() {
                return isAdmin;
            }

            public void setIsAdmin(boolean isAdmin) {
                this.isAdmin = isAdmin;
            }

            public boolean isCanMakeAdmin() {
                return canMakeAdmin;
            }

            public void setCanMakeAdmin(boolean canMakeAdmin) {
                this.canMakeAdmin = canMakeAdmin;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeByte(this.isAdmin ? (byte) 1 : (byte) 0);
                dest.writeByte(this.canMakeAdmin ? (byte) 1 : (byte) 0);
            }
        }
    }

    public static class Followers implements Parcelable {

        public static final Creator<Followers> CREATOR = new Creator<Followers>() {
            @Override
            public Followers createFromParcel(Parcel source) {
                return new Followers(source);
            }

            @Override
            public Followers[] newArray(int size) {
                return new Followers[size];
            }
        };
        @SerializedName("countryCode")
        private String countryCode;
        @SerializedName("is_active")
        private boolean isActive;
        @SerializedName("emailValidated")
        private boolean emailValidated;
        @SerializedName("is_admin")
        private boolean isAdmin;
        @SerializedName("permissions")
        private Permissions permissions;
        @SerializedName("_id")
        private String id;
        @SerializedName("updatedAt")
        private String updatedAt;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("email")
        private String email;
        @SerializedName("password")
        private String password;
        @SerializedName("fullName")
        private String fullName;
        @SerializedName("mobile")
        private long mobile;
        @SerializedName("__v")
        private int v;

        public Followers() {
        }

        protected Followers(Parcel in) {
            this.countryCode = in.readString();
            this.isActive = in.readByte() != 0;
            this.emailValidated = in.readByte() != 0;
            this.isAdmin = in.readByte() != 0;
            this.permissions = in.readParcelable(Permissions.class.getClassLoader());
            this.id = in.readString();
            this.updatedAt = in.readString();
            this.createdAt = in.readString();
            this.email = in.readString();
            this.password = in.readString();
            this.fullName = in.readString();
            this.mobile = in.readLong();
            this.v = in.readInt();
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public boolean isIsActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }

        public boolean isEmailValidated() {
            return emailValidated;
        }

        public void setEmailValidated(boolean emailValidated) {
            this.emailValidated = emailValidated;
        }

        public boolean isIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public Permissions getPermissions() {
            return permissions;
        }

        public void setPermissions(Permissions permissions) {
            this.permissions = permissions;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public long getMobile() {
            return mobile;
        }

        public void setMobile(long mobile) {
            this.mobile = mobile;
        }

        public int getV() {
            return v;
        }

        public void setV(int v) {
            this.v = v;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.countryCode);
            dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
            dest.writeByte(this.emailValidated ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isAdmin ? (byte) 1 : (byte) 0);
            dest.writeParcelable(this.permissions, flags);
            dest.writeString(this.id);
            dest.writeString(this.updatedAt);
            dest.writeString(this.createdAt);
            dest.writeString(this.email);
            dest.writeString(this.password);
            dest.writeString(this.fullName);
            dest.writeLong(this.mobile);
            dest.writeInt(this.v);
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
            private boolean isAdmin;
            @SerializedName("can_make_admin")
            private boolean canMakeAdmin;

            public Permissions() {
            }

            protected Permissions(Parcel in) {
                this.isAdmin = in.readByte() != 0;
                this.canMakeAdmin = in.readByte() != 0;
            }

            public boolean isIsAdmin() {
                return isAdmin;
            }

            public void setIsAdmin(boolean isAdmin) {
                this.isAdmin = isAdmin;
            }

            public boolean isCanMakeAdmin() {
                return canMakeAdmin;
            }

            public void setCanMakeAdmin(boolean canMakeAdmin) {
                this.canMakeAdmin = canMakeAdmin;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeByte(this.isAdmin ? (byte) 1 : (byte) 0);
                dest.writeByte(this.canMakeAdmin ? (byte) 1 : (byte) 0);
            }
        }
    }

    public static class Tags implements Parcelable {

        public static final Creator<Tags> CREATOR = new Creator<Tags>() {
            @Override
            public Tags createFromParcel(Parcel source) {
                return new Tags(source);
            }

            @Override
            public Tags[] newArray(int size) {
                return new Tags[size];
            }
        };
        @SerializedName("countryCode")
        private String countryCode;
        @SerializedName("is_active")
        private boolean isActive;
        @SerializedName("emailValidated")
        private boolean emailValidated;
        @SerializedName("is_admin")
        private boolean isAdmin;
        @SerializedName("permissions")
        private Tags_Permissions permissions;
        @SerializedName("_id")
        private String id;
        @SerializedName("updatedAt")
        private String updatedAt;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("email")
        private String email;
        @SerializedName("password")
        private String password;
        @SerializedName("fullName")
        private String fullName;
        @SerializedName("mobile")
        private long mobile;
        @SerializedName("__v")
        private int v;

        public Tags() {
        }

        protected Tags(Parcel in) {
            this.countryCode = in.readString();
            this.isActive = in.readByte() != 0;
            this.emailValidated = in.readByte() != 0;
            this.isAdmin = in.readByte() != 0;
            this.permissions = in.readParcelable(Tags_Permissions.class.getClassLoader());
            this.id = in.readString();
            this.updatedAt = in.readString();
            this.createdAt = in.readString();
            this.email = in.readString();
            this.password = in.readString();
            this.fullName = in.readString();
            this.mobile = in.readLong();
            this.v = in.readInt();
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public boolean isIsActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }

        public boolean isEmailValidated() {
            return emailValidated;
        }

        public void setEmailValidated(boolean emailValidated) {
            this.emailValidated = emailValidated;
        }

        public boolean isIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public Tags_Permissions getPermissions() {
            return permissions;
        }

        public void setPermissions(Tags_Permissions permissions) {
            this.permissions = permissions;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public long getMobile() {
            return mobile;
        }

        public void setMobile(long mobile) {
            this.mobile = mobile;
        }

        public int getV() {
            return v;
        }

        public void setV(int v) {
            this.v = v;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.countryCode);
            dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
            dest.writeByte(this.emailValidated ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isAdmin ? (byte) 1 : (byte) 0);
            dest.writeParcelable(this.permissions, flags);
            dest.writeString(this.id);
            dest.writeString(this.updatedAt);
            dest.writeString(this.createdAt);
            dest.writeString(this.email);
            dest.writeString(this.password);
            dest.writeString(this.fullName);
            dest.writeLong(this.mobile);
            dest.writeInt(this.v);
        }

        public static class Tags_Permissions implements Parcelable {

            public static final Creator<Tags_Permissions> CREATOR = new Creator<Tags_Permissions>() {
                @Override
                public Tags_Permissions createFromParcel(Parcel source) {
                    return new Tags_Permissions(source);
                }

                @Override
                public Tags_Permissions[] newArray(int size) {
                    return new Tags_Permissions[size];
                }
            };
            @SerializedName("is_admin")
            private boolean isAdmin;
            @SerializedName("can_make_admin")
            private boolean canMakeAdmin;

            public Tags_Permissions() {
            }

            protected Tags_Permissions(Parcel in) {
                this.isAdmin = in.readByte() != 0;
                this.canMakeAdmin = in.readByte() != 0;
            }

            public boolean isIsAdmin() {
                return isAdmin;
            }

            public void setIsAdmin(boolean isAdmin) {
                this.isAdmin = isAdmin;
            }

            public boolean isCanMakeAdmin() {
                return canMakeAdmin;
            }

            public void setCanMakeAdmin(boolean canMakeAdmin) {
                this.canMakeAdmin = canMakeAdmin;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeByte(this.isAdmin ? (byte) 1 : (byte) 0);
                dest.writeByte(this.canMakeAdmin ? (byte) 1 : (byte) 0);
            }
        }
    }
}
