package com.example.user.collaboration.network.service;

import com.example.user.collaboration.network.model.CollaborationData;
import com.example.user.collaboration.network.model.CommentData;
import com.example.user.collaboration.network.model.Notification;
import com.squareup.okhttp.RequestBody;

import java.util.List;

import retrofit2.Call;

public interface CommentAPIService {
    Call<List<CollaborationData>> getComments(String token, String projectId);

    Call<CommentData> postComments(String token, RequestBody requestBody);

    Call<List<Notification>> getNotifications(String token);
}
