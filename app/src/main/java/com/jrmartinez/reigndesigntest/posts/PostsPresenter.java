package com.jrmartinez.reigndesigntest.posts;

import android.support.annotation.NonNull;

import com.jrmartinez.reigndesigntest.data.Post;
import com.jrmartinez.reigndesigntest.data.source.PostDataSource;
import com.jrmartinez.reigndesigntest.data.source.PostsRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class PostsPresenter implements PostsContract.Presenter {

    private final PostsRepository mPostsRepository;

    private final PostsContract.View mPostsView;

    private boolean mFirstLoad = true;

    public PostsPresenter(@NonNull PostsRepository postsRepository, @NonNull PostsContract.View postsView) {
        mPostsRepository = checkNotNull(postsRepository, "postsRepository cannot be null");
        mPostsView = checkNotNull(postsView, "postsView cannot be null!");
        mPostsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadPosts(false);
    }


    @Override
    public void loadPosts(boolean forceUpdate) {
        loadPosts(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadPosts(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mPostsView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mPostsRepository.refreshPosts();
        }


        mPostsRepository.getPosts(new PostDataSource.LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                mPostsView.setLoadingIndicator(false);
                if(!posts.isEmpty())
                    mPostsView.showPosts(posts);
                else
                    mPostsView.showNoPosts();
            }

            @Override
            public void onDataNotAvailable() {
                mPostsView.setLoadingIndicator(false);
                mPostsView.showNoPosts();
            }
        });
    }

    @Override
    public void openPostURL(@NonNull Post requestedPost) {
        if(requestedPost.getStoryUrl() != null){
            mPostsView.goToPostURL(requestedPost.getStoryUrl());
        }
    }

    @Override
    public void deletePost(@NonNull Post post) {
        mPostsRepository.deletePost(post);
        loadPosts(false);
    }

    @Override
    public void activePost(@NonNull Post post) {
        mPostsRepository.activePost(post);
        loadPosts(false);
    }
}
