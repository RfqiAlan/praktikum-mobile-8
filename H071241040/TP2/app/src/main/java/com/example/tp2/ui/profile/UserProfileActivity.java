package com.example.tp2.ui.profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.tp2.adapters.HighlightAdapter;
import com.example.tp2.adapters.ProfileFeedAdapter;
import com.example.tp2.data.DataManager;
import com.example.tp2.databinding.ActivityUserProfileBinding;
import com.example.tp2.models.UserModel;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            finish();
            return;
        }

        UserModel user = DataManager.getInstance().getUser(userId);
        if (user == null) {
            finish();
            return;
        }

        binding.tvTopUsername.setText(user.getUsername());
        binding.btnBack.setOnClickListener(v -> finish());
        
        binding.profileContent.tvProfileName.setText(user.getUsername());
        Glide.with(this).load(user.getProfilePicUrl()).into(binding.profileContent.ivProfileMyPic);
        binding.profileContent.tvPostCount.setText(String.valueOf(DataManager.getInstance().getUserFeeds(userId).size()));
        binding.profileContent.tvFollowersCount.setText(String.valueOf(user.getFollowersCount()));
        binding.profileContent.tvFollowingCount.setText(String.valueOf(user.getFollowingCount()));

        HighlightAdapter highlightAdapter = new HighlightAdapter(this, DataManager.getInstance().getUserHighlights(userId));
        binding.profileContent.rvHighlights.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.profileContent.rvHighlights.setAdapter(highlightAdapter);

        ProfileFeedAdapter feedAdapter = new ProfileFeedAdapter(this, DataManager.getInstance().getUserFeeds(userId));
        binding.profileContent.rvProfileFeed.setLayoutManager(new GridLayoutManager(this, 3));
        binding.profileContent.rvProfileFeed.setAdapter(feedAdapter);
    }
}
