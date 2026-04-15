package com.example.tp2.ui.detail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tp2.databinding.ActivityStoryDetailBinding;

public class StoryDetailActivity extends AppCompatActivity {

    private ActivityStoryDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String storyImage = getIntent().getStringExtra("STORY_IMAGE");
        if (storyImage != null) {
            Glide.with(this).load(storyImage).into(binding.ivStoryImage);
        }

        binding.pbStory.setMax(100);
        binding.pbStory.setProgress(0);

        // Simple mock progress running
        final Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            int progress = 0;
            @Override
            public void run() {
                progress += 2;
                binding.pbStory.setProgress(progress);
                if (progress < 100) {
                    handler.postDelayed(this, 50);
                } else {
                    finish();
                }
            }
        };
        handler.postDelayed(runnable, 50);
        
        binding.getRoot().setOnClickListener(v -> finish());
    }
}
