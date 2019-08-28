package com.example.user.collaboration.network.service;

import com.example.user.collaboration.network.model.CollaborationData;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public interface CollaborationAPIService {

    Single<List<CollaborationData>> myWallCollaboration(String token, String projectId);

    Single<List<CollaborationData>> myPostsCollaboration(String token, String userId);

    Single<List<CollaborationData>> followersCollaboration(String token, String userId);

    Call<Object> getFollow(String token, String postId);

    Call<Object> getUnfollow(String token, String postId);

    Call<Object> getLike(String token, String postId);

    Call<Object> getDislike(String token, String postId);

    Single<Object> collaborationParamsPost(String token, Map<String, RequestBody> partMap, List<MultipartBody.Part> files);
}