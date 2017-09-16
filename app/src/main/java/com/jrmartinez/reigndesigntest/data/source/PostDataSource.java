package com.jrmartinez.reigndesigntest.data.source;

import android.support.annotation.NonNull;

import com.jrmartinez.reigndesigntest.data.Post;

import java.util.List;

/**
 * Created by junior on 9/15/17.
 */

public interface PostDataSource {

    interface LoadPostsCallback {

        void onPostsLoaded(List<Post> posts);

        void onDataNotAvailable();
    }

    void getPosts(@NonNull LoadPostsCallback callback);

    void savePost(@NonNull Post post);

    void deletePost(@NonNull Post post);

    void activePost(@NonNull Post post);

    void refreshPosts();
}
