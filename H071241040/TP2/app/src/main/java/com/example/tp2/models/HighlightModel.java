package com.example.tp2.models;

import java.io.Serializable;

public class HighlightModel implements Serializable {
    private String id;
    private String title;
    private String coverImageUrl;

    public HighlightModel(String id, String title, String coverImageUrl) {
        this.id = id;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getCoverImageUrl() { return coverImageUrl; }
}
