package com.jrmartinez.reigndesigntest.data;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import com.google.common.base.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by junior on 9/15/17.
 */

public class Post {
    @NonNull
    private final int story_id;

    @NonNull
    private final String story_title;

    @NonNull
    private final String author;

    @NonNull
    private final String created_at;

    @NonNull
    private final String story_url;

    @NonNull
    private final long time;

    private final int deleted;

    public Post(@NonNull int id, @NonNull String title,
                @NonNull String author, @NonNull String date, @NonNull String url, int deleted) {
        story_id = id;
        story_title = title;
        this.author = author;
        created_at = date;
        story_url = url;
        time = getTimeFromTimestamp();
        this.deleted = deleted;
    }

    @NonNull
    public int getStoryId() {
        return story_id;
    }

    @NonNull
    public String getStoryTitle() {
        return story_title;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    @NonNull
    public String getCreatedAt() {
        return created_at;
    }

    @NonNull
    public String getStoryUrl() {
        return story_url;
    }

    @NonNull
    public long getTime() {
        return getTimeFromTimestamp();
    }

    @NonNull
    public String getFormattedDate(){
        return DateUtils.getRelativeTimeSpanString(getTimeFromTimestamp(),
                new Date().getTime(), DateUtils.SECOND_IN_MILLIS).toString();
    }

    public int getDeleted() {
        return deleted;
    }
    public boolean isDeleted(){
        return deleted == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equal(story_id, post.story_id) &&
                Objects.equal(story_title, post.story_title) &&
                Objects.equal(author, post.author);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(story_id, story_title, author);
    }

    @Override
    public String toString() {
        return "Post created by " + author + " with title " + story_title+" status "+ getTimeFromTimestamp();
    }

    private long getTimeFromTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(created_at).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date().getTime();
    }

}
