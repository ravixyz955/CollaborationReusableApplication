package com.example.user.collaboration.network.service;

import com.example.user.collaboration.network.RemoteServerAPI;
import com.squareup.okhttp.RequestBody;

import retrofit2.Call;

public class ReplyAPIServiceImpl implements ReplyAPIService {
    private final RemoteServerAPI remoteServerAPI;

    public ReplyAPIServiceImpl(RemoteServerAPI remoteServerAPI) {
        this.remoteServerAPI = remoteServerAPI;
    }

    @Override
    public Call<Object> postReplies(String token, String commentId, RequestBody requestBody) {
        return remoteServerAPI.postReplies(token, commentId, requestBody);
    }
}
