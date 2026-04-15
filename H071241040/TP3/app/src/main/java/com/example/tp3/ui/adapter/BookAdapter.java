package com.example.tp3.ui.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp3.R;
import com.example.tp3.data.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    private final List<Book> books = new ArrayList<>();
    private final OnBookClickListener listener;

    public BookAdapter(OnBookClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Book> newBooks) {
        books.clear();
        books.addAll(newBooks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book, listener);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageCover;
        private final TextView textTitle;
        private final TextView textAuthor;
        private final TextView textMeta;
        private final TextView textRating;
        private final TextView textLiked;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCover = itemView.findViewById(R.id.imageCover);
            textTitle = itemView.findViewById(R.id.textTitle);
            textAuthor = itemView.findViewById(R.id.textAuthor);
            textMeta = itemView.findViewById(R.id.textMeta);
            textRating = itemView.findViewById(R.id.textRating);
            textLiked = itemView.findViewById(R.id.textLiked);
        }

        void bind(Book book, OnBookClickListener listener) {
            textTitle.setText(book.getTitle());
            textAuthor.setText(book.getAuthor());
            textMeta.setText(itemView.getContext().getString(R.string.book_meta, book.getYear(), book.getGenre()));
            textRating.setText(itemView.getContext().getString(
                    R.string.book_rating,
                    String.format(Locale.getDefault(), "%.1f", book.getRating())
            ));

            textLiked.setVisibility(book.isLiked() ? View.VISIBLE : View.GONE);

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

            itemView.setOnClickListener(v -> listener.onBookClick(book));
        }
    }
}
