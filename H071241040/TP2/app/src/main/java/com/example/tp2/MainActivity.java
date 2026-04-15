package com.example.tp2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tp2.adapters.FeedAdapter;
import com.example.tp2.data.DataManager;
import com.example.tp2.databinding.ActivityMainBinding;
import com.example.tp2.ui.post.PostActivity;
import com.example.tp2.ui.profile.ProfileActivity;
import com.example.tp2.ui.profile.UserProfileActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FeedAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHomePage();

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                refreshHomeData();
                return true;
            } else if (id == R.id.nav_post) {
                navigateTo(PostActivity.class);
                return true;
            } else if (id == R.id.nav_profile) {
                navigateTo(ProfileActivity.class);
                return true;
            }
            return false;
        });

        binding.bottomNav.setSelectedItemId(R.id.nav_home);
    }

    private void setupHomePage() {
        homeAdapter = new FeedAdapter(this, DataManager.getInstance().getHomeFeeds(), userId -> {
            if (userId.equals(DataManager.MY_USER_ID)) {
                navigateTo(ProfileActivity.class);
            } else {
                Intent intent = new Intent(this, UserProfileActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        binding.rvHomeFeed.setLayoutManager(new LinearLayoutManager(this));
        binding.rvHomeFeed.setAdapter(homeAdapter);
    }

    private void refreshHomeData() {
        if (homeAdapter != null) {
            homeAdapter.updateData(DataManager.getInstance().getHomeFeeds());
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
        refreshHomeData();
    }
}
