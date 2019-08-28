package com.example.user.collaboration.network;

import com.example.user.collaboration.network.model.ActivateUserRequest;
import com.example.user.collaboration.network.model.AllUsers;
import com.example.user.collaboration.network.model.AuthenticateUserRequest;
import com.example.user.collaboration.network.model.CollaborationData;
import com.example.user.collaboration.network.model.CommentData;
import com.example.user.collaboration.network.model.Notification;
import com.example.user.collaboration.network.model.Project;
import com.example.user.collaboration.network.model.ProjectPlan;
import com.example.user.collaboration.network.model.RegisterUserRequest;
import com.example.user.collaboration.network.model.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RemoteServerAPI {

    String BASE_CONTEXT = "/api";

    @Multipart
    @POST(BASE_CONTEXT + "/users/image")
    Call<Void> uploadImage(@Header("x-auth-token") String fcmId, @Part("file") RequestBody request);

    @POST(BASE_CONTEXT + "/register")
    Single<User> registerUser(@Body RegisterUserRequest request);

    @POST(BASE_CONTEXT + "/v1/auth/authenticate")
    Single<User> authenticate(@Body AuthenticateUserRequest request);

    @POST("/activate")
    Single<Void> activateUser(@Body ActivateUserRequest request);

    @GET(BASE_CONTEXT + "/v1/projects")
    Call<List<Project>> getProjects(@Header("x-auth-token") String token, @Query("parent") String parent);

    @GET(BASE_CONTEXT + "/v1/projects/{project_id}")
    Call<Project> getProject(@Header("x-auth-token") String token, @Path("project_id") String projectId);

    @GET(BASE_CONTEXT + "/v1/projects/{project_id}/project-plan")
    Call<ProjectPlan> getProjectPlan(@Header("x-auth-token") String token, @Path("project_id") String projectId);

    @GET(BASE_CONTEXT + "/comments")
    Call<List<CollaborationData>> getComments(@Header("x-auth-token") String token, @Query("project_id") String projectId);

    @POST(BASE_CONTEXT + "/comments")
    Call<CommentData> postComments(@Header("x-auth-token") String token, @Body com.squareup.okhttp.RequestBody requestBody);

    @PUT(BASE_CONTEXT + "/comments/{comment_id}/replies")
    Call<Object> postReplies(@Header("x-auth-token") String token, @Path("comment_id") String comment, @Body com.squareup.okhttp.RequestBody requestBody);

    @GET(BASE_CONTEXT + "/notifications")
    Call<List<Notification>> getNotifications(@Header("x-auth-token") String token);

    @GET(BASE_CONTEXT + "/v1/collaboration/post")
    Single<List<CollaborationData>> myWallCollaboration(@Header("x-auth-token") String token, @Query("relevantTo") String projectId);

    @GET(BASE_CONTEXT + "/v1/collaboration/post")
    Single<List<CollaborationData>> myPostsCollaboration(@Header("x-auth-token") String token, @Query("user") String userId);

    @GET(BASE_CONTEXT + "/v1/collaboration/post")
    Single<List<CollaborationData>> followersCollaboration(@Header("x-auth-token") String token, @Query("followedBy") String userId);

    @PATCH(BASE_CONTEXT + "/v1/collaboration/post/follow/{postId}")
    Call<Object> getFollow(@Header("x-auth-token") String token, @Path("postId") String postId);

    @PATCH(BASE_CONTEXT + "/v1/collaboration/post/unfollow/{postId}")
    Call<Object> getUnfollow(@Header("x-auth-token") String token, @Path("postId") String postId);

    @PATCH(BASE_CONTEXT + "/v1/collaboration/post/like/{postId}")
    Call<Object> getLike(@Header("x-auth-token") String token, @Path("postId") String postId);

    @PATCH(BASE_CONTEXT + "/v1/collaboration/post/dislike/{postId}")
    Call<Object> getDislike(@Header("x-auth-token") String token, @Path("postId") String postId);

    @Multipart
    @POST(BASE_CONTEXT + "/v1/collaboration/post")
    Single<Object> collaborationParamsPost(
            @Header("x-auth-token") String token,
            @PartMap Map<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> files
    );

    @GET(BASE_CONTEXT + "/v1/auth/user/all")
    Single<AllUsers> getUsers(@Header("x-auth-token") String token);
}