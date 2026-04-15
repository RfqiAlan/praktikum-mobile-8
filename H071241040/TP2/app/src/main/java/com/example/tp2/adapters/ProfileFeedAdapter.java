package com.example.tp2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tp2.R;
import com.example.tp2.models.FeedModel;
import com.example.tp2.ui.detail.FeedDetailActivity;

import java.util.List;

public class ProfileFeedAdapter extends RecyclerView.Adapter<ProfileFeedAdapter.GridFeedViewHolder> {

    private List<FeedModel> feeds;
    private Context context;

    public ProfileFeedAdapter(Context context, List<FeedModel> feeds) {
        this.context = context;
        this.feeds = feeds;
    }

    public void updateData(List<FeedModel> newFeeds) {
        this.feeds = newFeeds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GridFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_feed, parent, false);
        return new GridFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridFeedViewHolder holder, int position) {
        FeedModel feed = feeds.get(position);
        Glide.with(context).load(feed.getPostImageUrl()).into(holder.ivGridPost);
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FeedDetailActivity.class);
            intent.putExtra("FEED_DATA", feed);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    static class GridFeedViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGridPost;

        public GridFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGridPost = itemView.findViewById(R.id.iv_grid_post);
        }
    }
}
