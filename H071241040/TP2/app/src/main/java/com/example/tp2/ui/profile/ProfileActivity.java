package com.example.tp2.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.tp2.MainActivity;
import com.example.tp2.R;
import com.example.tp2.adapters.HighlightAdapter;
import com.example.tp2.adapters.ProfileFeedAdapter;
import com.example.tp2.data.DataManager;
import com.example.tp2.databinding.ActivityProfileBinding;
import com.example.tp2.models.UserModel;
import com.example.tp2.ui.post.PostActivity;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private ProfileFeedAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        HighlightAdapter highlightAdapter = new HighlightAdapter(this, DataManager.getInstance().getUserHighlights(DataManager.MY_USER_ID));
        binding.profileContent.rvHighlights.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.profileContent.rvHighlights.setAdapter(highlightAdapter);

        feedAdapter = new ProfileFeedAdapter(this, DataManager.getInstance().getUserFeeds(DataManager.MY_USER_ID));
        binding.profileContent.rvProfileFeed.setLayoutManager(new GridLayoutManager(this, 3));
        binding.profileContent.rvProfileFeed.setAdapter(feedAdapter);

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                navigateTo(MainActivity.class);
                return true;
            } else if (id == R.id.nav_post) {
                navigateTo(PostActivity.class);
                return true;
            } else if (id == R.id.nav_profile) {
                refreshData();
                return true;
            }
            return false;
        });

        binding.bottomNav.setSelectedItemId(R.id.nav_profile);
        refreshData();
    }

    private void refreshData() {
        UserModel myUser = DataManager.getInstance().getUser(DataManager.MY_USER_ID);
        if (myUser != null) {
            binding.profileContent.tvProfileName.setText(myUser.getUsername());
            Glide.with(this).load(myUser.getProfilePicUrl()).into(binding.profileContent.ivProfileMyPic);
            binding.profileContent.tvFollowersCount.setText(String.valueOf(myUser.getFollowersCount()));
            binding.profileContent.tvFollowingCount.setText(String.valueOf(myUser.getFollowingCount()));
        }

        if (feedAdapter != null) {
            feedAdapter.updateData(DataManager.getInstance().getUserFeeds(DataManager.MY_USER_ID));
            binding.profileContent.tvPostCount.setText(String.valueOf(DataManager.getInstance().getUserFeeds(DataManager.MY_USER_ID).size()));
        }
    }

    private void navigateTo(Class<?> targetActivity) {
        if (getClass().equals(targetActivity)) {
            return;
        }
        Intent intent = new Intent(this, targetActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }
}
