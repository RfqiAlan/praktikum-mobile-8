package com.example.tp1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditProfileActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_BIO = "extra_bio";

    private EditText etName;
    private EditText etUsername;
    private EditText etBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editProfileRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bind views
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etBio = findViewById(R.id.etBio);
        ImageButton btnClose = findViewById(R.id.btnClose);
        ImageButton btnSave = findViewById(R.id.btnSave);

        // Pre-fill fields with current profile data
        Intent incoming = getIntent();
        if (incoming != null) {
            etName.setText(incoming.getStringExtra(EXTRA_NAME));
            etUsername.setText(incoming.getStringExtra(EXTRA_USERNAME));
            etBio.setText(incoming.getStringExtra(EXTRA_BIO));
        }

        // Close without saving
        btnClose.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        // Save and return data
        btnSave.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_NAME, etName.getText().toString().trim());
            resultIntent.putExtra(EXTRA_USERNAME, etUsername.getText().toString().trim());
            resultIntent.putExtra(EXTRA_BIO, etBio.getText().toString().trim());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
