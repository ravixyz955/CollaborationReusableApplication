package com.example.user.collaboration.network.service;

import com.example.user.collaboration.network.RemoteServerAPI;
import com.example.user.collaboration.network.model.AllUsers;
import com.example.user.collaboration.network.model.Project;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;

public class ProjectAPIServiceImpl implements ProjectAPIService {

    private final RemoteServerAPI remoteServerAPI;

    public ProjectAPIServiceImpl(RemoteServerAPI remoteServerAPI) {
        this.remoteServerAPI = remoteServerAPI;
    }

    @Override
    public Call<List<Project>> getProjects(String token, String parent) {
        return remoteServerAPI.getProjects(token, parent);
    }

    @Override
    public Call<Project> getProject(String token, String projectId) {
        return remoteServerAPI.getProject(token, projectId);
    }

    @Override
    public Single<AllUsers> getUsers(String token) {
        return remoteServerAPI.getUsers(token);
    }
}