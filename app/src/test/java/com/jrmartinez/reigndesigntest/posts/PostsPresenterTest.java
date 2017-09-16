package com.jrmartinez.reigndesigntest.posts;

import com.google.common.collect.Lists;
import com.jrmartinez.reigndesigntest.data.Post;
import com.jrmartinez.reigndesigntest.data.source.PostDataSource;
import com.jrmartinez.reigndesigntest.data.source.PostsRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

/**
 * Created by junior on 9/16/17.
 */

public class PostsPresenterTest {
    private static List<Post> Posts;

    @Mock
    private PostsRepository mPostsRepository;

    @Mock
    private PostsContract.View mPostsView;

    @Captor
    private ArgumentCaptor<PostDataSource.LoadPostsCallback> mLoadPostsCallbackCaptor;

    private PostsPresenter mPostsPresenter;


    @Before
    public void setupPostsPresenter() {

        MockitoAnnotations.initMocks(this);

        mPostsPresenter = new PostsPresenter(mPostsRepository, mPostsView);

        Posts = Lists.newArrayList(
                new Post(1, "TEST 1", "AUTHOR 1", "2017-09-15T15:05:00.000Z", "https://github.com/roinuj39/ReignDesignTest",0),
                new Post(2, "TEST 2", "AUTHOR 2", "2017-09-15T15:05:00.000Z", "https://github.com/roinuj39/ReignDesignTest",1),
                new Post(3, "TEST 3", "AUTHOR 3", "2017-09-15T15:05:00.000Z", "https://github.com/roinuj39/ReignDesignTest",0)
        );
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        mPostsPresenter = new PostsPresenter(mPostsRepository, mPostsView);
        verify(mPostsView).setPresenter(mPostsPresenter);
    }

    @Test
    public void loadAllPostsFromRepositoryAndLoadIntoView() {
        mPostsPresenter.loadPosts(true);
        verify(mPostsRepository).getPosts(mLoadPostsCallbackCaptor.capture());
        mLoadPostsCallbackCaptor.getValue().onPostsLoaded(Posts);
        InOrder inOrder = inOrder(mPostsView);
        inOrder.verify(mPostsView).setLoadingIndicator(true);
        inOrder.verify(mPostsView).setLoadingIndicator(false);
        ArgumentCaptor<List> showPostsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mPostsView).showPosts(showPostsArgumentCaptor.capture());
        assertTrue(showPostsArgumentCaptor.getValue().size() == 3);
    }


    @Test
    public void clickOnPost_ShowsWebView() {
        Post requestedPost = Posts.get(0);
        mPostsPresenter.openPostURL(requestedPost);
        verify(mPostsView).goToPostURL(any(String.class));
    }

    @Test
    public void DeletePost() {
        Post post = Posts.get(2);
        mPostsPresenter.deletePost(post);
        verify(mPostsRepository).deletePost(post);
    }
}
