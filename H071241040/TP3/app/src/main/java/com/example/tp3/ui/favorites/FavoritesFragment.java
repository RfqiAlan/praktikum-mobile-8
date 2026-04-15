package com.example.tp3.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp3.R;
import com.example.tp3.data.BookRepository;
import com.example.tp3.ui.adapter.BookAdapter;
import com.example.tp3.ui.detail.DetailActivity;

import java.util.List;

public class FavoritesFragment extends Fragment {
    private BookAdapter adapter;
    private TextView emptyState;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerFavorites);
        emptyState = view.findViewById(R.id.textEmptyFavorites);

        adapter = new BookAdapter(book -> {
            Intent intent = new Intent(requireContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_BOOK_ID, book.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        loadFavorites();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        if (adapter == null || emptyState == null) {
            return;
        }
        List<com.example.tp3.data.Book> favorites = BookRepository.getFavoriteBooks();
        adapter.submitList(favorites);
        emptyState.setVisibility(favorites.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
