package com.jrmartinez.reigndesigntest.data.source;

import android.support.annotation.NonNull;

import com.jrmartinez.reigndesigntest.data.Post;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by junior on 9/15/17.
 */

public class PostsRepository implements PostDataSource {

    private static PostsRepository INSTANCE = null;

    private final PostDataSource mPostsRemoteDataSource;

    private final PostDataSource mPostsLocalDataSource;

    Map<Integer, Post> mCachedPosts;
    boolean mCacheIsDirty = false;

    private PostsRepository(@NonNull PostDataSource postsRemoteDataSource,
                            @NonNull PostDataSource postsLocalDataSource) {
        mPostsRemoteDataSource = checkNotNull(postsRemoteDataSource);
        mPostsLocalDataSource = checkNotNull(postsLocalDataSource);
    }

    public static PostsRepository getInstance(PostDataSource postsRemoteDataSource,
                                              PostDataSource postsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PostsRepository(postsRemoteDataSource, postsLocalDataSource);
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getPosts(@NonNull final LoadPostsCallback callback) {
        if (mCachedPosts != null && !mCacheIsDirty) {
            callback.onPostsLoaded(new ArrayList<>(mCachedPosts.values()));
            return;
        }

        if (mCacheIsDirty) {
            getPostsFromRemoteDataSource(new LoadPostsCallback() {
                @Override
                public void onPostsLoaded(List<Post> posts) {
                    getPostFromLocalDataSource(callback);
                }

                @Override
                public void onDataNotAvailable() {
                    getPostFromLocalDataSource(callback);
                }
            });
        } else {
            getPostFromLocalDataSource(callback);
        }
    }

    @Override
    public void savePost(@NonNull Post post) {

    }

    @Override
    public void deletePost(@NonNull Post post) {
        mPostsLocalDataSource.deletePost(post);
        mCachedPosts.remove(post.getStoryId());
    }

    @Override
    public void activePost(@NonNull Post post) {
        mPostsLocalDataSource.activePost(post);
        mCachedPosts.put(post.getStoryId(), post);
    }

    @Override
    public void refreshPosts() {
        mCacheIsDirty = true;
    }

    private void getPostsFromRemoteDataSource(@NonNull final LoadPostsCallback callback) {
        mPostsRemoteDataSource.getPosts(new LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                posts = validatePosts(posts);
                refreshCache(posts);
                refreshLocalDataSource(posts);
                callback.onPostsLoaded(new ArrayList<>(mCachedPosts.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
    private void getPostFromLocalDataSource(@NonNull final  LoadPostsCallback callback){

        mPostsLocalDataSource.getPosts(new LoadPostsCallback() {
            @Override
            public void onPostsLoaded(List<Post> posts) {
                refreshCache(posts);
                callback.onPostsLoaded(new ArrayList<>(mCachedPosts.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
    private void refreshCache(List<Post> posts) {
        if (mCachedPosts == null) {
            mCachedPosts = new LinkedHashMap<>();
        }
        mCachedPosts.clear();
        for (Post post : posts) {
            if(!post.isDeleted())
                mCachedPosts.put(post.getStoryId(), post);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Post> posts) {
        for (Post post : posts) {
            mPostsLocalDataSource.savePost(post);
        }
    }
    private List<Post> validatePosts(List<Post> posts){
        List<Post> validated = new ArrayList<>();
        for (Post post : posts) {
            if(post.getStoryTitle() != null && !post.getStoryTitle().isEmpty()){
                validated.add(post);
            }
        }
        return validated;
    }

}
