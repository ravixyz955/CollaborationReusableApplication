package com.example.user.collaboration.network.service;

import com.example.user.collaboration.network.RemoteServerAPI;
import com.example.user.collaboration.network.model.ActivateUserRequest;
import com.example.user.collaboration.network.model.AuthenticateUserRequest;
import com.example.user.collaboration.network.model.ProjectPlan;
import com.example.user.collaboration.network.model.RegisterUserRequest;
import com.example.user.collaboration.network.model.User;

import io.reactivex.Single;
import retrofit2.Call;

public class UserAPIServiceImpl implements UserAPIService {

    private final RemoteServerAPI remoteServerAPI;

    public UserAPIServiceImpl(RemoteServerAPI remoteServerAPI) {
        this.remoteServerAPI = remoteServerAPI;
    }

    @Override
    public Single<User> registerUser(RegisterUserRequest request) {
        return remoteServerAPI.registerUser(request);
    }

    @Override
    public Single<User> authenticate(AuthenticateUserRequest request) {
        return remoteServerAPI.authenticate(request);
    }

    @Override
    public Single<Void> activateUser(final ActivateUserRequest request) {
        return remoteServerAPI.activateUser(request);
    }

    @Override
    public Call<ProjectPlan> getProjectPlan(String token, String projectID) {
        return remoteServerAPI.getProjectPlan(token, projectID);
    }
}
