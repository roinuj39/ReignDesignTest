package com.jrmartinez.reigndesigntest.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.jrmartinez.reigndesigntest.data.Post;
import com.jrmartinez.reigndesigntest.data.source.PostDataSource;
import com.jrmartinez.reigndesigntest.util.NetworkConnection;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by junior on 9/15/17.
 */

public class PostRemoteDataSource implements PostDataSource {

    private static PostRemoteDataSource INSTANCE;
    private static Context mContext;
    public static final String BASE_URL = "https://hn.algolia.com/api/v1/search_by_date?query=android";

    static class PostList {
        public List<Post> hits;
    }


    private PostRemoteDataSource(@NonNull Context context) {
        mContext = context;
    }

    public static PostRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PostRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getPosts(@NonNull final LoadPostsCallback callback) {
        if(!NetworkConnection.isConnected(mContext)){
            callback.onDataNotAvailable();
        }
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onDataNotAvailable();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onDataNotAvailable();
                } else {
                    String data = response.body().string();
                    PostList p = new Gson().fromJson(data, PostList.class);
                    callback.onPostsLoaded(p.hits);
                }
            }
        });
    }

    @Override
    public void savePost(@NonNull Post post) {

    }

    @Override
    public void deletePost(@NonNull Post post) {

    }

    @Override
    public void activePost(@NonNull Post post) {

    }

    @Override
    public void refreshPosts() {

    }

}
