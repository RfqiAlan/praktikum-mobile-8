package com.example.tp2.ui.detail;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tp2.databinding.ActivityFeedDetailBinding;
import com.example.tp2.models.FeedModel;

public class FeedDetailActivity extends AppCompatActivity {

    private ActivityFeedDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FeedModel feed = (FeedModel) getIntent().getSerializableExtra("FEED_DATA");
        if (feed != null) {
            binding.detailFeedView.tvUsername.setText(feed.getUsername());
            binding.detailFeedView.tvCaption.setText(feed.getCaption());
            Glide.with(this).load(feed.getUserProfilePicUrl()).into(binding.detailFeedView.ivProfilePic);
            Glide.with(this).load(feed.getPostImageUrl()).into(binding.detailFeedView.ivPostImage);
            
            binding.detailFeedView.layoutHeader.setOnClickListener(v -> finish());
            binding.btnBackFeed.setOnClickListener(v -> finish());
        }
    }
}
