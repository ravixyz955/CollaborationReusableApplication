package com.example.user.collaboration;

import com.example.user.collaboration.network.model.AllUsers;
import com.example.user.collaboration.network.model.CollaborationData;
import com.example.user.collaboration.network.model.FollowersData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockData {
    private MockData() {
    }

    public static List<CollaborationData> buildCommentsData() {
        ArrayList<CollaborationData> comments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            CollaborationData comment = new CollaborationData();
            CollaborationData.Owner owner = new CollaborationData.Owner();
            comment.setTitle("Comment title" + 1);
            comment.setTextContent("This is a sample Comment Data");
            comment.setReplies(buildRepliesData());
            comment.setOwner(owner);
            comment.setTags(buildTags());
            comment.getOwner().setFullName("xyz" + i);
            comment.getOwner().setEmail("xyz" + i + "@gmail.com");
            comment.setCreatedAt(new Date().toString());
            comment.setAttachments(new ArrayList<>(buildAttachments()));
            comment.setLikes(i + 1);
            comment.setUnlikes(i + 1);
            comment.setFollowers(buildCommentsFollowers());
            comments.add(comment);
        }
        return comments;
    }

    private static List<CollaborationData.Tags> buildTags() {
        ArrayList<CollaborationData.Tags> tags = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CollaborationData.Tags tag = new CollaborationData.Tags();
            tag.setCreatedAt(new Date().toString());
            tag.setEmail("xyz" + i + "@gmail.com");
            tag.setFullName(("xyz" + (i + 1)).toUpperCase());
            tag.setId(String.valueOf(i + 1));
            tags.add(tag);
        }
        return tags;
    }

    private static List<FollowersData.Tags> buildFollowersTags() {
        ArrayList<FollowersData.Tags> tags = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FollowersData.Tags tag = new FollowersData.Tags();
            tag.setCreatedAt(new Date().toString());
            tag.setEmail("xyz" + i + "@gmail.com");
            tag.setFullName(("xyz" + (i + 1)).toUpperCase());
            tag.setId(String.valueOf(i + 1));
            tags.add(tag);
        }
        return tags;
    }

    private static List<CollaborationData> buildRepliesData() {
        List<CollaborationData> replies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CollaborationData reply = new CollaborationData();
            CollaborationData.Owner owner = new CollaborationData.Owner();

            reply.setId((String.valueOf(i + 1)));
            reply.setTitle("Reply Title " + (i + 1));
            reply.setOwner(owner);
            reply.setTags(buildTags());
            reply.setAttachments(new ArrayList<>(buildAttachments()));
            reply.setFollowers(buildCommentsFollowers());
            reply.getOwner().setFullName("xyz" + i);
            reply.getOwner().setEmail("xyz" + i + "@gmail.com");
            reply.setTextContent("This is a sample reply text content for " + (i + 1));
            reply.setCreatedAt(new Date().toString());
            reply.setLikes(i);
            reply.setUnlikes(i);
            replies.add(reply);
        }
        return replies;
    }

    private static List<FollowersData> buildFollowersRepliesData() {
        List<FollowersData> replies = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            FollowersData reply = new FollowersData();
            FollowersData.Owner owner = new FollowersData.Owner();
            reply.setId((String.valueOf(i + 1)));
            reply.setTitle("Reply Title " + (i + 1));
            reply.setOwner(owner);
            reply.getOwner().setFullName("xyz" + i);
            reply.getOwner().setEmail("xyz" + i + "@gmail.com");
            reply.setTags(buildFollowersTags());
            reply.setAttachments(buildAttachments());
            reply.setLikes(i);
            reply.setUnlikes(i);
            reply.setFollowers(buildFollowers());
            reply.setTextContent("This is a sample reply text content for " + (i + 1));
            reply.setCreatedAt(new Date().toString());
            replies.add(reply);
        }
        return replies;
    }

    public static List<AllUsers.Users> buildUsersData() {
        ArrayList<AllUsers.Users> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AllUsers.Users users1 = new AllUsers.Users();
            users1.set_id(String.valueOf(i + 1));
            users1.setCreatedAt(new Date().toString());
            users1.setEmail("xyz" + i + "@gmail.com");
            users1.setFullName("xyz" + i);
            users1.setMobile("854752456" + i);
            users.add(users1);
        }
        return users;
    }

    public static List<FollowersData> buildFollowersData() {
        ArrayList<FollowersData> followersData = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            FollowersData followersData1 = new FollowersData();
            FollowersData.Owner owner = new FollowersData.Owner();
            followersData1.setCreatedAt(new Date().toString());
            followersData1.setOwner(owner);
            followersData1.getOwner().setFullName("xyz" + i);
            followersData1.getOwner().setEmail("xyz" + i + "@gmail.com");
            followersData1.setTags(buildFollowersTags());
            followersData1.setReplies(buildFollowersRepliesData());
            followersData1.setLikes(i);
            followersData1.setUnlikes(i);
            followersData1.setTextContent("This is sample text content for" + (i + 1));
            followersData1.setFollowers(buildFollowers());
            followersData1.setAttachments(buildAttachments());
            followersData.add(followersData1);
        }
        return followersData;
    }

    private static List<String> buildAttachments() {
        ArrayList<String> attachments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            attachments.add("https://test-xyz-collaboration.s3.ap-south-1.amazonaws.com/5cff996ac8492e0eaf0115dc/attachments/xyz" + i + ".PNG");
        }
        return attachments;
    }

    private static List<FollowersData.Followers> buildFollowers() {
        ArrayList<FollowersData.Followers> followers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FollowersData.Followers followers1 = new FollowersData.Followers();
            followers1.setCreatedAt(new Date().toString());
            followers1.setEmail("xyz" + i + "gmail.com");
            followers1.setFullName("xyz" + i);
            followers1.setId(String.valueOf(i));
            followers.add(followers1);
        }
        return followers;
    }

    private static List<CollaborationData.Followers> buildCommentsFollowers() {
        ArrayList<CollaborationData.Followers> followers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CollaborationData.Followers followers1 = new CollaborationData.Followers();
            followers1.setCreatedAt(new Date().toString());
            followers1.setEmail("xyz" + i + "gmail.com");
            followers1.setFullName("xyz" + i);
            followers1.setId(String.valueOf(i));
            followers.add(followers1);
        }
        return followers;
    }
}