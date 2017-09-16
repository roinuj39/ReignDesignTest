package com.jrmartinez.reigndesigntest.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jrmartinez.reigndesigntest.data.Post;
import com.jrmartinez.reigndesigntest.data.source.PostDataSource;
import com.jrmartinez.reigndesigntest.data.source.local.PostsPersistenceContract.PostEntry;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by junior on 9/15/17.
 */

public class PostLocalDataSource implements PostDataSource {

    private static PostLocalDataSource INSTANCE;

    private PostsDbHelper mDbHelper;

    private PostLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new PostsDbHelper(context);
    }

    public static PostLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PostLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getPosts(@NonNull LoadPostsCallback callback) {
        List<Post> posts = new ArrayList<Post>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                PostEntry.COLUMN_NAME_ENTRY_ID,
                PostEntry.COLUMN_NAME_TITLE,
                PostEntry.COLUMN_NAME_AUTHOR,
                PostEntry.COLUMN_NAME_URL,
                PostEntry.COLUMN_NAME_DATE,
                PostEntry.COLUMN_NAME_DELETED,
                PostEntry.COLUMN_NAME_DATE_LONG
        };
        Cursor c = db.query(
                PostEntry.TABLE_NAME, projection, null, null, null,
                null, PostEntry.COLUMN_NAME_DATE_LONG+" DESC");


        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int itemId = c.getInt(c.getColumnIndexOrThrow(PostEntry.COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow(PostEntry.COLUMN_NAME_TITLE));
                String author = c.getString(c.getColumnIndexOrThrow(PostEntry.COLUMN_NAME_AUTHOR));
                String date = c.getString(c.getColumnIndexOrThrow(PostEntry.COLUMN_NAME_DATE));
                String url = c.getString(c.getColumnIndexOrThrow(PostEntry.COLUMN_NAME_URL));
                int deleted = c.getInt(c.getColumnIndexOrThrow(PostEntry.COLUMN_NAME_DELETED));
                Post post = new Post(itemId, title, author, date, url, deleted);
                posts.add(post);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        Log.e("Database", posts.size()+" asdasd");
        if (posts.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            Log.e("Database", posts.size()+" leng");
            callback.onPostsLoaded(posts);
        }
    }

    @Override
    public void savePost(@NonNull Post post) {
        checkNotNull(post);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PostEntry.COLUMN_NAME_ENTRY_ID, post.getStoryId());
        values.put(PostEntry.COLUMN_NAME_TITLE, post.getStoryTitle());
        values.put(PostEntry.COLUMN_NAME_AUTHOR, post.getAuthor());
        values.put(PostEntry.COLUMN_NAME_URL, post.getStoryUrl());
        values.put(PostEntry.COLUMN_NAME_DATE_LONG, post.getTime());
        values.put(PostEntry.COLUMN_NAME_DELETED, post.getDeleted());
        values.put(PostEntry.COLUMN_NAME_DATE, post.getCreatedAt());

        db.insertWithOnConflict(PostEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        db.close();
    }

    @Override
    public void deletePost(@NonNull Post post) {
       changePostStatus(post, true);
    }

    @Override
    public void activePost(@NonNull Post post) {
        changePostStatus(post, false);
    }

    @Override
    public void refreshPosts() {

    }

    private void changePostStatus(Post post, boolean val){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PostEntry.COLUMN_NAME_DELETED, val);

        String selection = PostEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(post.getStoryId()) };

        db.update(PostEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

}
