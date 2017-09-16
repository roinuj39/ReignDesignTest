package com.jrmartinez.reigndesigntest.posts;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.jrmartinez.reigndesigntest.Injection;
import com.jrmartinez.reigndesigntest.R;
import com.jrmartinez.reigndesigntest.data.Post;

import java.util.List;

import io.huannguyen.swipeablerv.SWItemRemovalListener;
import io.huannguyen.swipeablerv.view.SWRecyclerView;

import static com.google.common.base.Preconditions.checkNotNull;

public class PostsActivity extends AppCompatActivity implements PostsContract.View, SWItemRemovalListener,
        PostsAdapter.PostItemClickListener {

    private PostsPresenter mPostsPresenter;
    private PostsContract.Presenter mPresenter;

    private SWRecyclerView mList;
    private View mEmptyView;
    private PostsAdapter mAdapter;

    private static final String SERVICE_ACTION = "android.support.customtabs.action.CustomTabsService";
    private static final String CHROME_PACKAGE = "com.android.chrome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.activity_posts_refresh_layout);

        mList = findViewById(R.id.activity_posts_list);
        mList.getSwipeMessageBuilder()
                .withSwipeDirection(SWRecyclerView.SwipeMessageBuilder.BOTH)
                .build();
        mEmptyView = findViewById(R.id.activity_posts_empty_view);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setScrollUpChild(mList);

        swipeRefreshLayout.setOnRefreshListener(() -> mPresenter.loadPosts(true));


        mPostsPresenter = new PostsPresenter(Injection.providePostsRepository(getApplicationContext()), this);
    }

    @Override
    public void setPresenter(@NonNull PostsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
        mPresenter.start();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        final SwipeRefreshLayout srl = findViewById(R.id.activity_posts_refresh_layout);
        srl.post(() -> srl.setRefreshing(active));
    }

    @Override
    public void showPosts(List<Post> posts) {
        runOnUiThread(() -> {
            toggleViews(true);
            if(mAdapter == null) {
                mAdapter = new PostsAdapter(this, posts, this);
                mAdapter.setItemRemovalListener(this);
                mList.setAdapter(mAdapter);
                mList.setupSwipeToDismiss(mAdapter, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
            }
            else
                mAdapter.updateValues(posts);
        });
    }

    @Override
    public void goToPostURL(String url) {
        if(isChromeCustomTabsSupported(this)){
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder
                    .addDefaultShareMenuItem()
                    .setShowTitle(true)
                    .enableUrlBarHiding()
                    .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .build()
                    .launchUrl(this, Uri.parse(url));
        } else {
            PostsWebViewActivity.startWebView(this, url);
        }

    }
    private static boolean isChromeCustomTabsSupported(@NonNull final Context context) {
        Intent serviceIntent = new Intent(SERVICE_ACTION);
        serviceIntent.setPackage(CHROME_PACKAGE);
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentServices(serviceIntent, 0);
        return !(resolveInfos == null || resolveInfos.isEmpty());
    }

    @Override
    public void showLoadingPostsError() {

    }

    @Override
    public void showNoPosts() {
        toggleViews(false);
    }

    @Override
    public void onItemTemporarilyRemoved(Object item, int position) {
        mPresenter.deletePost((Post) item);
    }

    @Override
    public void onItemPermanentlyRemoved(Object item) {
        mPresenter.deletePost((Post) item);
    }

    @Override
    public void onItemAddedBack(Object item, int position) {
        mPresenter.activePost((Post) item);
    }

    private void toggleViews(boolean showList){
        runOnUiThread(() -> {
            mList.setVisibility(showList ? View.VISIBLE : View.GONE);
            mEmptyView.setVisibility(!showList ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onPostClicked(Post clickedPost) {
        mPresenter.openPostURL(clickedPost);
    }
}
