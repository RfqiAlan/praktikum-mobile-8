package com.example.tp3.ui.add;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tp3.R;
import com.example.tp3.data.BookRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddBookFragment extends Fragment {
    private ImageView imagePreview;
    private TextInputEditText inputTitle;
    private TextInputEditText inputAuthor;
    private TextInputEditText inputYear;
    private TextInputEditText inputGenre;
    private TextInputEditText inputRating;
    private TextInputEditText inputReview;
    private TextInputEditText inputBlurb;
    private Uri selectedCoverUri;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                selectedCoverUri = uri;
                if (imagePreview == null) {
                    return;
                }
                if (uri != null) {
                    imagePreview.setImageURI(uri);
                } else {
                    imagePreview.setImageResource(R.drawable.bg_cover_placeholder);
                }
            });

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        imagePreview = view.findViewById(R.id.imagePreview);
        inputTitle = view.findViewById(R.id.inputTitle);
        inputAuthor = view.findViewById(R.id.inputAuthor);
        inputYear = view.findViewById(R.id.inputYear);
        inputGenre = view.findViewById(R.id.inputGenre);
        inputRating = view.findViewById(R.id.inputRating);
        inputReview = view.findViewById(R.id.inputReview);
        inputBlurb = view.findViewById(R.id.inputBlurb);

        MaterialButton buttonPickCover = view.findViewById(R.id.buttonPickCover);
        MaterialButton buttonSave = view.findViewById(R.id.buttonSaveBook);

        buttonPickCover.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
        buttonSave.setOnClickListener(v -> saveBook());

        return view;
    }

    private void saveBook() {
        if (inputTitle == null || inputAuthor == null || inputYear == null || inputBlurb == null
                || inputGenre == null || inputRating == null || inputReview == null) {
            return;
        }

        String title = getTrimmedText(inputTitle);
        String author = getTrimmedText(inputAuthor);
        String yearText = getTrimmedText(inputYear);
        String genre = getTrimmedText(inputGenre);
        String ratingText = getTrimmedText(inputRating);
        String review = getTrimmedText(inputReview);
        String blurb = getTrimmedText(inputBlurb);

        if (title.isEmpty()) {
            inputTitle.setError(getString(R.string.error_required));
            inputTitle.requestFocus();
            return;
        }
        if (author.isEmpty()) {
            inputAuthor.setError(getString(R.string.error_required));
            inputAuthor.requestFocus();
            return;
        }
        if (yearText.isEmpty()) {
            inputYear.setError(getString(R.string.error_required));
            inputYear.requestFocus();
            return;
        }
        if (blurb.isEmpty()) {
            inputBlurb.setError(getString(R.string.error_required));
            inputBlurb.requestFocus();
            return;
        }
        if (genre.isEmpty()) {
            inputGenre.setError(getString(R.string.error_required));
            inputGenre.requestFocus();
            return;
        }
        if (review.isEmpty()) {
            inputReview.setError(getString(R.string.error_required));
            inputReview.requestFocus();
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearText);
        } catch (NumberFormatException e) {
            inputYear.setError(getString(R.string.error_invalid_year));
            inputYear.requestFocus();
            return;
        }

        float rating = 0f;
        if (!ratingText.isEmpty()) {
            try {
                rating = Float.parseFloat(ratingText);
            } catch (NumberFormatException e) {
                inputRating.setError(getString(R.string.error_invalid_rating));
                inputRating.requestFocus();
                return;
            }
            if (rating < 0f || rating > 5f) {
                inputRating.setError(getString(R.string.error_invalid_rating_range));
                inputRating.requestFocus();
                return;
            }
        }

        BookRepository.addBook(
                title,
                author,
                year,
                blurb,
                selectedCoverUri == null ? null : selectedCoverUri.toString(),
                genre,
                rating,
                review
        );

        Toast.makeText(requireContext(), R.string.book_added, Toast.LENGTH_SHORT).show();
        clearForm();
    }

    private void clearForm() {
        selectedCoverUri = null;
        imagePreview.setImageResource(R.drawable.bg_cover_placeholder);
        inputTitle.setText("");
        inputAuthor.setText("");
        inputYear.setText("");
        inputGenre.setText("");
        inputRating.setText("");
        inputReview.setText("");
        inputBlurb.setText("");
    }

    private String getTrimmedText(TextInputEditText input) {
        if (input.getText() == null) {
            return "";
        }
        return input.getText().toString().trim();
    }
}
