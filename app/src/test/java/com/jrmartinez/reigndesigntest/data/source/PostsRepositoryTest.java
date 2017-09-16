package com.jrmartinez.reigndesigntest.data.source;

import android.content.Context;

import com.google.common.collect.Lists;
import com.jrmartinez.reigndesigntest.data.Post;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by junior on 9/16/17.
 */

public class PostsRepositoryTest {

    private static List<Post> Posts = Posts = Lists.newArrayList(
            new Post(1, "TEST 1", "AUTHOR 1", "2017-09-15T15:05:00.000Z", "https://github.com/roinuj39/ReignDesignTest",0),
            new Post(2, "TEST 2", "AUTHOR 2", "2017-09-15T15:05:00.000Z", "https://github.com/roinuj39/ReignDesignTest",1),
            new Post(3, "TEST 3", "AUTHOR 3", "2017-09-15T15:05:00.000Z", "https://github.com/roinuj39/ReignDesignTest",0)
    );

    private PostsRepository mPostsRepository;

    @Mock
    private PostDataSource mPostsRemoteDataSource;

    @Mock
    private PostDataSource mPostsLocalDataSource;

    @Mock
    private Context mContext;

    @Mock
    private PostDataSource.LoadPostsCallback mLoadPostsCallback;

    @Captor
    private ArgumentCaptor<PostDataSource.LoadPostsCallback> mPostsCallbackCaptor;

    

    @Before
    public void setupPostsRepository() {
        MockitoAnnotations.initMocks(this);

        mPostsRepository = PostsRepository.getInstance(
                mPostsRemoteDataSource, mPostsLocalDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        PostsRepository.destroyInstance();
    }

    @Test
    public void getPosts_repositoryCachesAfterFirstApiCall() {
        mPostsRepository.refreshPosts();
        twoPostsLoadCallsToRepository(mLoadPostsCallback);
        verify(mPostsRemoteDataSource).getPosts(any(PostDataSource.LoadPostsCallback.class));
    }

    @Test
    public void getPosts_requestsAllPostsFromLocalDataSource() {
        mPostsRepository.getPosts(mLoadPostsCallback);
        verify(mPostsLocalDataSource).getPosts(any(PostDataSource.LoadPostsCallback.class));
    }

    @Test
    public void getPostsWithDirtyCache_postsAreRetrievedFromRemote() {
        mPostsRepository.refreshPosts();
        mPostsRepository.getPosts(mLoadPostsCallback);
        setPostsAvailable(mPostsRemoteDataSource, Posts);
        verify(mPostsLocalDataSource, never()).getPosts(mLoadPostsCallback);
    }

    @Test
    public void getPostsWithBothDataSourcesUnavailable_firesOnDataUnavailable() {
        mPostsRepository.refreshPosts();
        mPostsRepository.getPosts(mLoadPostsCallback);
        setPostsNotAvailable(mPostsRemoteDataSource);
        setPostsNotAvailable(mPostsLocalDataSource);
        verify(mLoadPostsCallback).onDataNotAvailable();
    }

    @Test
    public void getPosts_refreshesLocalDataSource() {
        mPostsRepository.refreshPosts();
        mPostsRepository.getPosts(mLoadPostsCallback);
        setPostsAvailable(mPostsRemoteDataSource, Posts);
        verify(mPostsLocalDataSource, times(Posts.size())).savePost(any(Post.class));
    }

    private void twoPostsLoadCallsToRepository(PostDataSource.LoadPostsCallback callback) {
        mPostsRepository.getPosts(callback);
        verify(mPostsRemoteDataSource).getPosts(mPostsCallbackCaptor.capture());
        mPostsCallbackCaptor.getValue().onDataNotAvailable();
        verify(mPostsLocalDataSource).getPosts(mPostsCallbackCaptor.capture());
        mPostsCallbackCaptor.getValue().onPostsLoaded(Posts);
        mPostsRepository.getPosts(callback);
    }

    private void setPostsNotAvailable(PostDataSource dataSource) {
        verify(dataSource).getPosts(mPostsCallbackCaptor.capture());
        mPostsCallbackCaptor.getValue().onDataNotAvailable();
    }

    private void setPostsAvailable(PostDataSource dataSource, List<Post> posts) {
        verify(dataSource).getPosts(mPostsCallbackCaptor.capture());
        mPostsCallbackCaptor.getValue().onPostsLoaded(posts);
    }
    private Post createStubPost(int id, int deleted){
        return new Post(id, "TEST "+id, "AUTHOR "+id, "2017-09-15T15:05:00.000Z", "https://github.com/roinuj39/ReignDesignTest",deleted);
    }
    private Post createStubPost(int id){
        return createStubPost(id, 0);
    }
}
