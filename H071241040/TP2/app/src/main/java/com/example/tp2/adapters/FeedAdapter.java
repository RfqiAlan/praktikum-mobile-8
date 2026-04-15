package com.example.tp2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tp2.R;
import com.example.tp2.models.FeedModel;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private List<FeedModel> feeds;
    private Context context;
    private OnProfileClickListener listener;

    public interface OnProfileClickListener {
        void onProfileClicked(String userId);
    }

    public FeedAdapter(Context context, List<FeedModel> feeds, OnProfileClickListener listener) {
        this.context = context;
        this.feeds = feeds;
        this.listener = listener;
    }

    public void updateData(List<FeedModel> newFeeds) {
        this.feeds = newFeeds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        FeedModel feed = feeds.get(position);
        
        holder.tvUsername.setText(feed.getUsername());
        holder.tvCaption.setText(feed.getCaption());
        
        Glide.with(context).load(feed.getUserProfilePicUrl()).into(holder.ivProfilePic);
        Glide.with(context).load(feed.getPostImageUrl()).into(holder.ivPostImage);
        
        View.OnClickListener profileClick = v -> {
            if (listener != null) {
                listener.onProfileClicked(feed.getUserId());
            }
        };
        holder.layoutHeader.setOnClickListener(profileClick);
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    static class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfilePic, ivPostImage;
        TextView tvUsername, tvCaption;
        View layoutHeader;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutHeader = itemView.findViewById(R.id.layout_header);
            ivProfilePic = itemView.findViewById(R.id.iv_profile_pic);
            ivPostImage = itemView.findViewById(R.id.iv_post_image);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvCaption = itemView.findViewById(R.id.tv_caption);
        }
    }
}
