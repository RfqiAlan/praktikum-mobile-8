package com.example.tp3.ui.detail;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tp3.R;
import com.example.tp3.data.Book;
import com.example.tp3.data.BookRepository;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_BOOK_ID = "extra_book_id";

    private int bookId = -1;
    private ImageView imageCover;
    private TextView textTitle;
    private TextView textAuthor;
    private TextView textYear;
    private TextView textGenre;
    private TextView textRating;
    private TextView textReview;
    private TextView textBlurb;
    private MaterialButton buttonLike;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.book_detail);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        imageCover = findViewById(R.id.detailCover);
        textTitle = findViewById(R.id.detailTitle);
        textAuthor = findViewById(R.id.detailAuthor);
        textYear = findViewById(R.id.detailYear);
        textGenre = findViewById(R.id.detailGenre);
        textRating = findViewById(R.id.detailRating);
        textReview = findViewById(R.id.detailReview);
        textBlurb = findViewById(R.id.detailBlurb);
        buttonLike = findViewById(R.id.buttonLike);

        bookId = getIntent().getIntExtra(EXTRA_BOOK_ID, -1);
        Book book = BookRepository.getBookById(bookId);
        if (book == null) {
            finish();
            return;
        }

        buttonLike.setOnClickListener(v -> {
            BookRepository.toggleLike(bookId);
            renderBook();
        });

        renderBook();
    }

    private void renderBook() {
        Book book = BookRepository.getBookById(bookId);
        if (book == null) {
            finish();
            return;
        }

        textTitle.setText(book.getTitle());
        textAuthor.setText(getString(R.string.by_author, book.getAuthor()));
        textYear.setText(getString(R.string.detail_year, book.getYear()));
        textGenre.setText(getString(R.string.detail_genre, book.getGenre()));
        textRating.setText(getString(
                R.string.detail_rating,
                String.format(Locale.getDefault(), "%.1f", book.getRating())
        ));
        textReview.setText(getString(R.string.detail_review, book.getReview()));
        textBlurb.setText(book.getBlurb());
        buttonLike.setText(book.isLiked() ? R.string.unlike : R.string.like);

        String coverUri = book.getCoverUri();
        if (coverUri != null && !coverUri.trim().isEmpty()) {
            imageCover.setImageURI(Uri.parse(coverUri));
            if (imageCover.getDrawable() == null) {
                imageCover.setImageResource(R.drawable.bg_cover_placeholder);
            }
        } else if (book.getCoverResId() != null) {
            imageCover.setImageResource(book.getCoverResId());
        } else {
            imageCover.setImageResource(R.drawable.bg_cover_placeholder);
        }
    }
}
