package com.example.user.collaboration.network.service;

import com.example.user.collaboration.network.RemoteServerAPI;
import com.example.user.collaboration.network.model.CollaborationData;
import com.example.user.collaboration.network.model.CommentData;
import com.example.user.collaboration.network.model.Notification;
import com.squareup.okhttp.RequestBody;

import java.util.List;

import retrofit2.Call;

public class CommentAPIServiceImpl implements CommentAPIService {
    private final RemoteServerAPI remoteServerAPI;

    public CommentAPIServiceImpl(RemoteServerAPI remoteServerAPI) {
        this.remoteServerAPI = remoteServerAPI;
    }

    @Override
    public Call<List<CollaborationData>> getComments(String token, String projectId) {
        return remoteServerAPI.getComments(token, projectId);
    }

    @Override
    public Call<CommentData> postComments(String token, RequestBody requestBody) {
        return remoteServerAPI.postComments(token, requestBody);
    }

    @Override
    public Call<List<Notification>> getNotifications(String token) {
        return remoteServerAPI.getNotifications(token);
    }
}
