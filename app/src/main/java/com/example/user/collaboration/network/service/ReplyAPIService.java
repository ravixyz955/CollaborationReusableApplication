package com.example.user.collaboration.network.service;

import com.squareup.okhttp.RequestBody;

import retrofit2.Call;

public interface ReplyAPIService {
    Call<Object> postReplies(String token, String comment_id, RequestBody requestBody);
}
