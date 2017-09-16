package com.jrmartinez.reigndesigntest.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Posts.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PostsPersistenceContract.PostEntry.TABLE_NAME + " (" +
                    PostsPersistenceContract.PostEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + " PRIMARY KEY," +
                    PostsPersistenceContract.PostEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PostsPersistenceContract.PostEntry.COLUMN_NAME_AUTHOR + TEXT_TYPE + COMMA_SEP +
                    PostsPersistenceContract.PostEntry.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    PostsPersistenceContract.PostEntry.COLUMN_NAME_DATE_LONG + BOOLEAN_TYPE + COMMA_SEP +
                    PostsPersistenceContract.PostEntry.COLUMN_NAME_DELETED + BOOLEAN_TYPE + COMMA_SEP +
                    PostsPersistenceContract.PostEntry.COLUMN_NAME_DATE + TEXT_TYPE +
            " )";

    public PostsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
