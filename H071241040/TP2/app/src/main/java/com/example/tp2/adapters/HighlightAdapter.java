package com.example.tp2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tp2.R;
import com.example.tp2.models.HighlightModel;
import com.example.tp2.ui.detail.StoryDetailActivity;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class HighlightAdapter extends RecyclerView.Adapter<HighlightAdapter.HighlightViewHolder> {
    
    private List<HighlightModel> highlights;
    private Context context;

    public HighlightAdapter(Context context, List<HighlightModel> highlights) {
        this.context = context;
        this.highlights = highlights;
    }

    @NonNull
    @Override
    public HighlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_highlight, parent, false);
        return new HighlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightViewHolder holder, int position) {
        HighlightModel hl = highlights.get(position);
        holder.tvTitle.setText(hl.getTitle());
        Glide.with(context).load(hl.getCoverImageUrl()).into(holder.ivCover);
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoryDetailActivity.class);
            intent.putExtra("STORY_IMAGE", hl.getCoverImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return highlights.size();
    }

    static class HighlightViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivCover;
        TextView tvTitle;

        public HighlightViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_highlight_cover);
            tvTitle = itemView.findViewById(R.id.tv_highlight_title);
        }
    }
}
