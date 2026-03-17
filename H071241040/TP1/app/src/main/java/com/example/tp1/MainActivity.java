package com.example.tp1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvUsername;
    private TextView tvDisplayName;
    private TextView tvBio;

    // Current profile data (mutable state)
    private String currentName;
    private String currentUsername;
    private String currentBio;

    private final ActivityResultLauncher<Intent> editProfileLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            currentName = data.getStringExtra(EditProfileActivity.EXTRA_NAME);
                            currentUsername = data.getStringExtra(EditProfileActivity.EXTRA_USERNAME);
                            currentBio = data.getStringExtra(EditProfileActivity.EXTRA_BIO);

                            // Update the profile page views
                            tvDisplayName.setText(currentName);
                            tvUsername.setText(currentUsername);
                            tvBio.setText(currentBio);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bind views
        tvUsername = findViewById(R.id.tvUsername);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        tvBio = findViewById(R.id.tvBio);
        Button btnEditProfile = findViewById(R.id.btnEditProfile);

        // Initialize current data from layout defaults
        currentName = tvDisplayName.getText().toString();
        currentUsername = tvUsername.getText().toString();
        currentBio = tvBio.getText().toString();

        // Edit Profile button click
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
            intent.putExtra(EditProfileActivity.EXTRA_NAME, currentName);
            intent.putExtra(EditProfileActivity.EXTRA_USERNAME, currentUsername);
            intent.putExtra(EditProfileActivity.EXTRA_BIO, currentBio);
            editProfileLauncher.launch(intent);
        });
    }
}