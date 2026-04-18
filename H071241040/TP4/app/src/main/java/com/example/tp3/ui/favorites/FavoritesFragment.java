package com.example.tp3.ui.favorites;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp3.R;
import com.example.tp3.data.Book;
import com.example.tp3.data.BookRepository;
import com.example.tp3.ui.adapter.BookAdapter;
import com.example.tp3.ui.detail.DetailActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class FavoritesFragment extends Fragment {
    private BookAdapter adapter;
    private TextView emptyState;
    private ProgressBar progressLoading;
    private ExecutorService backgroundExecutor;
    private Handler mainHandler;
    private final AtomicInteger latestLoadId = new AtomicInteger(0);

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
        progressLoading = view.findViewById(R.id.progressFavoritesLoading);

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

        loadFavorites();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        latestLoadId.incrementAndGet();
        if (backgroundExecutor != null) {
            backgroundExecutor.shutdownNow();
            backgroundExecutor = null;
        }
        adapter = null;
        emptyState = null;
        progressLoading = null;
    }

    private void loadFavorites() {
        if (adapter == null || emptyState == null || backgroundExecutor == null || mainHandler == null) {
            return;
        }
        final int loadId = latestLoadId.incrementAndGet();
        final boolean debugDelayEnabled = isDebuggable();
        setLoading(true);

        backgroundExecutor.execute(() -> {
            if (debugDelayEnabled) SystemClock.sleep(700);
            List<Book> favorites = BookRepository.getFavoriteBooks();
            mainHandler.post(() -> {
                if (!isAdded() || adapter == null || emptyState == null) {
                    return;
                }
                if (loadId != latestLoadId.get()) {
                    return;
                }
                adapter.submitList(favorites);
                emptyState.setVisibility(favorites.isEmpty() ? View.VISIBLE : View.GONE);
                setLoading(false);
            });
        });
    }

    private void setLoading(boolean isLoading) {
        if (progressLoading != null) {
            progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        if (isLoading && emptyState != null) {
            emptyState.setVisibility(View.GONE);
        }
    }

    private boolean isDebuggable() {
        return (requireContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
}
