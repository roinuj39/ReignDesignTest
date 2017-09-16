package com.jrmartinez.reigndesigntest.posts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jrmartinez.reigndesigntest.R;

public class PostsWebViewActivity extends AppCompatActivity {
    private static final String EXTRA_URL = "EXTRA_URL";
    private WebView mWV;
    public static void startWebView(Context ctx, String url){
        Intent intent = new Intent(ctx, PostsWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_web_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        Intent intent = getIntent();

        String url = intent.getStringExtra(EXTRA_URL);
        mWV = findViewById(R.id.activity_posts_webview_webview);
        mWV.loadUrl(url);
        mWV.setWebViewClient(new webClient());
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWV.canGoBack()) {
            mWV.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    class webClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
    }
}
