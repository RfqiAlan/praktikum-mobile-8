package com.example.tp3.ui.home;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp3.R;
import com.example.tp3.data.Book;
import com.example.tp3.data.BookRepository;
import com.example.tp3.ui.adapter.BookAdapter;
import com.example.tp3.ui.detail.DetailActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeFragment extends Fragment {
    private BookAdapter adapter;
    private String currentQuery = "";
    private String currentGenre = null;
    private ChipGroup chipGroupGenre;
    private ProgressBar progressLoading;
    private ExecutorService backgroundExecutor;
    private Handler mainHandler;
    private final AtomicInteger latestRequestId = new AtomicInteger(0);

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerHome);
        SearchView searchView = view.findViewById(R.id.searchBooks);
        chipGroupGenre = view.findViewById(R.id.chipGroupGenre);
        progressLoading = view.findViewById(R.id.progressHomeLoading);

        if (backgroundExecutor == null || backgroundExecutor.isShutdown()) {
            backgroundExecutor = Executors.newSingleThreadExecutor();
        }
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }

        adapter = new BookAdapter(book -> {
            Intent intent = new Intent(requireContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_BOOK_ID, book.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentQuery = query == null ? "" : query;
                loadBooks();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentQuery = newText == null ? "" : newText;
                loadBooks();
                return true;
            }
        });

        setupGenreChips();
        loadBooks();
        return view;
    }

    private void setupGenreChips() {
        if (chipGroupGenre == null) return;

        List<String> genres = BookRepository.getGenres();
        
        // Clear existing dynamic chips except the "All" chip
        for (int i = chipGroupGenre.getChildCount() - 1; i >= 0; i--) {
            View child = chipGroupGenre.getChildAt(i);
            if (child.getId() != R.id.chipAll) {
                chipGroupGenre.removeView(child);
            }
        }

        for (String genre : genres) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.layout_genre_chip, chipGroupGenre, false);
            chip.setText(genre);
            chip.setCheckable(true);
            chipGroupGenre.addView(chip);
        }

        chipGroupGenre.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentGenre = null;
            } else {
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chipAll) {
                    currentGenre = null;
                } else {
                    Chip chip = group.findViewById(checkedId);
                    currentGenre = chip.getText().toString();
                }
            }
            loadBooks();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBooks();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        latestRequestId.incrementAndGet();
        if (backgroundExecutor != null) {
            backgroundExecutor.shutdownNow();
            backgroundExecutor = null;
        }
        adapter = null;
        chipGroupGenre = null;
        progressLoading = null;
    }

    private void loadBooks() {
        if (adapter == null || backgroundExecutor == null || mainHandler == null) {
            return;
        }
        final int requestId = latestRequestId.incrementAndGet();
        final String query = currentQuery;
        final String genre = currentGenre;
        final boolean debugDelayEnabled = isDebuggable();
        setLoading(true);

        backgroundExecutor.execute(() -> {
            if (debugDelayEnabled) SystemClock.sleep(700);
            List<Book> results = BookRepository.searchBooks(query, genre);
            mainHandler.post(() -> {
                if (!isAdded() || adapter == null) {
                    return;
                }
                if (requestId != latestRequestId.get()) {
                    return;
                }
                adapter.submitList(results);
                setLoading(false);
            });
        });
    }

    private void setLoading(boolean isLoading) {
        if (progressLoading == null) {
            return;
        }
        progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private boolean isDebuggable() {
        return (requireContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
}
