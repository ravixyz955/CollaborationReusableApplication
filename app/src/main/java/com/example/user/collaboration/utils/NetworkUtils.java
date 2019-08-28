package com.example.user.collaboration.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.example.user.collaboration.network.RemoteServerAPI;
import com.example.user.collaboration.network.TokenAuthenticator;
import com.example.user.collaboration.network.service.CollaborationAPIService;
import com.example.user.collaboration.network.service.CollaborationAPIServiceImpl;
import com.example.user.collaboration.network.service.CommentAPIService;
import com.example.user.collaboration.network.service.CommentAPIServiceImpl;
import com.example.user.collaboration.network.service.ProjectAPIService;
import com.example.user.collaboration.network.service.ProjectAPIServiceImpl;
import com.example.user.collaboration.network.service.ReplyAPIService;
import com.example.user.collaboration.network.service.ReplyAPIServiceImpl;
import com.example.user.collaboration.network.service.UserAPIService;
import com.example.user.collaboration.network.service.UserAPIServiceImpl;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.gson.BoundingBoxDeserializer;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.geojson.gson.GeometryDeserializer;
import com.mapbox.geojson.gson.PointDeserializer;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private static final String BASE_URL = "api.staging.xyzinnotech.com";
    private static final String BASE_CONTEXT = "/api";

    private static GsonBuilder gsonBuilder;
    private static OkHttpClient.Builder okHttpClient;
    private static Retrofit retrofit;
    private static RemoteServerAPI remoteServerAPI;
    private static UserAPIService userAPIService;
    private static CollaborationAPIService collaborationAPIService;
    private static CommentAPIService commentAPIService;
    private static ReplyAPIService replyAPIService;
    private static ProjectAPIService projectAPIService;

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    private static Cache provideOkHttpCache(Context mContext) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(mContext.getCacheDir(), cacheSize);
    }

    private static Gson provideGson() {
        if (gsonBuilder == null)
            gsonBuilder = new GsonBuilder()
                    .registerTypeAdapterFactory(GeoJsonAdapterFactory.create())
                    .registerTypeAdapter(Point.class, new PointDeserializer())
                    .registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer())
                    .registerTypeAdapter(Geometry.class, new GeometryDeserializer())
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return gsonBuilder.create();
    }

    private static Interceptor provideOkHttpClientLogging() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    private static OkHttpClient provideOkHttpClient(Context mContext, boolean enableLogging) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder();
            okHttpClient.cache(provideOkHttpCache(mContext));
            okHttpClient.connectTimeout(45, TimeUnit.SECONDS);
            okHttpClient.readTimeout(45, TimeUnit.SECONDS);
            okHttpClient.protocols(Collections.singletonList(Protocol.HTTP_1_1));
            okHttpClient.authenticator(new TokenAuthenticator(mContext));
            if (enableLogging) {
                okHttpClient.interceptors().add(provideOkHttpClientLogging());
            }
        }
        return okHttpClient.build();
    }

    private static Retrofit provideRetrofit(Context mContext, String baseUrl, boolean enableLogging) {
        if (retrofit != null)
            retrofit = null;
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .baseUrl(baseUrl)
                .client(provideOkHttpClient(mContext, enableLogging))
                .build();
        return retrofit;
    }

    private static RemoteServerAPI provideServerAPI(Context mContext, String serverUrl) {
        if (remoteServerAPI != null)
            remoteServerAPI = null;
        remoteServerAPI = provideRetrofit(mContext, serverUrl + BASE_URL, true).create(RemoteServerAPI.class);
        return remoteServerAPI;
    }

    public static UserAPIService provideUserAPIService(Context mContext, String serverUrl) {
        if (userAPIService != null)
            userAPIService = null;
        userAPIService = new UserAPIServiceImpl(provideServerAPI(mContext, serverUrl));
        return userAPIService;
    }

    public static CollaborationAPIService provideCollaborationAPIService(Context mContext, String serverUrl) {
        if (collaborationAPIService != null)
            collaborationAPIService = null;
        collaborationAPIService = new CollaborationAPIServiceImpl(provideServerAPI(mContext, serverUrl));
        return collaborationAPIService;
    }

    public static ProjectAPIService provideProjectAPIService(Context mContext, String serverUrl) {
        if (projectAPIService != null)
            projectAPIService = null;
        projectAPIService = new ProjectAPIServiceImpl(provideServerAPI(mContext, serverUrl));
        return projectAPIService;
    }

    public static CommentAPIService provideCommentsAPIService(Context mContext, String serverUrl) {
        if (commentAPIService != null)
            commentAPIService = null;
        commentAPIService = new CommentAPIServiceImpl(provideServerAPI(mContext, serverUrl));
        return commentAPIService;
    }

    public static ReplyAPIService provideReplyAPIService(Context mContext, String serverUrl) {
        if (replyAPIService != null)
            replyAPIService = null;
        replyAPIService = new ReplyAPIServiceImpl(provideServerAPI(mContext, serverUrl));
        return replyAPIService;
    }

    public static String provideAvatarUrl(String mobile) {
        return BASE_URL + BASE_CONTEXT + "/image/" + mobile;
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return null; //not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }
}