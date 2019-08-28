package com.example.user.collaboration.network.service;

import com.example.user.collaboration.network.model.ActivateUserRequest;
import com.example.user.collaboration.network.model.AuthenticateUserRequest;
import com.example.user.collaboration.network.model.ProjectPlan;
import com.example.user.collaboration.network.model.RegisterUserRequest;
import com.example.user.collaboration.network.model.User;

import io.reactivex.Single;
import retrofit2.Call;

public interface UserAPIService {

    Single<User> registerUser(RegisterUserRequest request);

    Single<User> authenticate(AuthenticateUserRequest request);

    Single<Void> activateUser(ActivateUserRequest request);

    Call<ProjectPlan> getProjectPlan(String token, String projectID);
}