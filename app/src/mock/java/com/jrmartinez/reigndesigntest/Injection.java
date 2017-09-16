package com.jrmartinez.reigndesigntest;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jrmartinez.reigndesigntest.data.source.PostsRepository;
import com.jrmartinez.reigndesigntest.data.source.local.PostLocalDataSource;
import com.jrmartinez.reigndesigntest.data.source.remote.PostRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;


public class Injection {

    public static PostsRepository providePostsRepository(@NonNull Context context) {
        checkNotNull(context);
        return PostsRepository.getInstance(PostRemoteDataSource.getInstance(context),
                PostLocalDataSource.getInstance(context));
    }
}
