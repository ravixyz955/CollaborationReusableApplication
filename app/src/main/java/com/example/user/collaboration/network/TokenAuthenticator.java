package com.example.user.collaboration.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.user.collaboration.network.model.AuthenticateUserRequest;
import com.example.user.collaboration.network.model.User;
import com.example.user.collaboration.network.service.UserAPIService;
import com.example.user.collaboration.utils.DataUtils;
import com.example.user.collaboration.utils.NetworkUtils;

import java.io.IOException;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {
    private static final String AUTHORIZATION = "x-auth-token";
    private Context mContext;

    private String newAccessToken;

    private UserAPIService userAPIService;

    public TokenAuthenticator(Context mContext) {
        this.mContext = mContext;
        userAPIService = NetworkUtils.provideUserAPIService(mContext, "https://auth.");
    }

    @Nullable
    @Override
    public Request authenticate(@NonNull Route route, @NonNull Response response) throws IOException {
        if (!TextUtils.isEmpty(DataUtils.getEmail(mContext))) {
            if (!response.request().url().pathSegments().get(0).endsWith("/authenticate")) {
                AuthenticateUserRequest authenticateUserRequest = new AuthenticateUserRequest();
                authenticateUserRequest.setEmail(DataUtils.getEmail(mContext));
                authenticateUserRequest.setPassword(DataUtils.getPassword(mContext));


                userAPIService.authenticate(authenticateUserRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getTokenDisposableObserver(authenticateUserRequest));

            } else
                return null;


            // Add new header to rejected request and retry it
            return response.request().newBuilder()
                    .header(AUTHORIZATION, newAccessToken)
                    .build();
        } else
            return null;
    }

    private SingleObserver<? super User> getTokenDisposableObserver(AuthenticateUserRequest authenticateUserRequest) {
        return new DisposableSingleObserver<User>() {
            @Override
            public void onSuccess(User user) {
                DataUtils.saveUser(mContext, user.toString());
                DataUtils.saveEmail(mContext, user.getEmail());
                DataUtils.saveName(mContext, user.getFullName());
                DataUtils.saveMobile(mContext, user.getMobile());
                DataUtils.savePassword(mContext, authenticateUserRequest.getPassword());
                DataUtils.setActive(mContext, false);
                DataUtils.saveCountryCode(mContext, user.getCountryCode());
                DataUtils.saveToken(mContext, user.getToken());
                newAccessToken = user.getToken();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mContext, "Failed to signin!", Toast.LENGTH_LONG).show();
            }
        };
    }
}