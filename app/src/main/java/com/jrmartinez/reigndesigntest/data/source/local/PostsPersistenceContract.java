package com.jrmartinez.reigndesigntest.data.source.local;

import android.provider.BaseColumns;


public final class PostsPersistenceContract {

    private PostsPersistenceContract() {}

    public static abstract class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "posts";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_TITLE = "story_title";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_DATE = "created_at";
        public static final String COLUMN_NAME_DATE_LONG = "time";
        public static final String COLUMN_NAME_DELETED = "deleted";
    }
}
