package com.example.tp2.ui.post;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tp2.MainActivity;
import com.example.tp2.R;
import com.example.tp2.data.DataManager;
import com.example.tp2.databinding.ActivityPostBinding;
import com.example.tp2.ui.profile.ProfileActivity;

public class PostActivity extends AppCompatActivity {

    private ActivityPostBinding binding;
    private Uri selectedImageUri = null;

    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    Glide.with(this).load(uri).into(binding.ivPreview);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnPickPhoto.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        binding.btnPost.setOnClickListener(v -> {
            String caption = binding.etCaption.getText().toString().trim();

            if (caption.isEmpty()) {
                Toast.makeText(this, "Caption cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedImageUri == null) {
                Toast.makeText(this, "Please select a photo", Toast.LENGTH_SHORT).show();
                return;
            }

            DataManager.getInstance().addNewPost(caption, selectedImageUri.toString());
            Toast.makeText(this, "Post added!", Toast.LENGTH_SHORT).show();

            binding.etCaption.setText("");
            binding.ivPreview.setImageDrawable(null);
            selectedImageUri = null;

            navigateTo(ProfileActivity.class);
        });

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                navigateTo(MainActivity.class);
                return true;
            } else if (id == R.id.nav_post) {
                return true;
            } else if (id == R.id.nav_profile) {
                navigateTo(ProfileActivity.class);
                return true;
            }
            return false;
        });

        binding.bottomNav.setSelectedItemId(R.id.nav_post);
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
}
