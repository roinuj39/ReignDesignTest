package com.jrmartinez.reigndesigntest.posts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jrmartinez.reigndesigntest.R;
import com.jrmartinez.reigndesigntest.data.Post;

import java.util.List;

import io.huannguyen.swipeablerv.adapter.StandardSWAdapter;

/**
 * Created by junior on 9/15/17.
 */

public class PostsAdapter extends StandardSWAdapter<Post> {
    private final Context context;
    private List<Post> values;
    private PostItemClickListener callback;

    public PostsAdapter(Context ctx, List<Post> posts, PostItemClickListener listener){
        super(posts);
        this.values = posts;
        context = ctx;
        callback = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return new ViewHolder( LayoutInflater.from(context)
                    .inflate(R.layout.item_post, viewGroup, false));

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Post post = getItem(position);
        ViewHolder h = (ViewHolder) holder;
        h.mTitle.setText(post.getStoryTitle());
        h.mDate.setText(context.getResources().getString(R.string.item_post_author_date,post.getAuthor(), post.getFormattedDate()));
        h.bind(post, callback);
    }
    public void updateValues(List values){
        this.values = values;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    private Post getItem(int position) {
        return values.get(position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle, mDate;
        View mRoot;
        ViewHolder(View v) {
            super(v);
            mTitle = v.findViewById(R.id.item_post_title);
            mDate = v.findViewById(R.id.item_post_author_date);
            mRoot = v;
        }
        public void bind(final Post post, PostItemClickListener itemClickListener) {
            mRoot.setOnClickListener((View view) -> {
                itemClickListener.onPostClicked(post);
            });
        }
    }
    public interface PostItemClickListener {
        void onPostClicked(Post clickedPost);
    }

}
