package com.jrmartinez.reigndesigntest.posts;

import android.support.annotation.NonNull;

import com.jrmartinez.reigndesigntest.BasePresenter;
import com.jrmartinez.reigndesigntest.BaseView;
import com.jrmartinez.reigndesigntest.data.Post;

import java.util.List;


public interface PostsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showPosts(List<Post> posts);

        void goToPostURL(String url);

        void showLoadingPostsError();

        void showNoPosts();

    }

    interface Presenter extends BasePresenter {

        void loadPosts(boolean forceUpdate);

        void openPostURL(@NonNull Post requestedPost);

        void deletePost(@NonNull Post post);

        void activePost(@NonNull Post post);


    }
}
