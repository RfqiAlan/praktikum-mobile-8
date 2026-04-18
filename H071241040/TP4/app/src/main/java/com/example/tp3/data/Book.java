package com.example.tp3.data;

public class Book {
    private final int id;
    private final String title;
    private final String author;
    private final int year;
    private final String blurb;
    private final Integer coverResId;
    private final String coverUri;
    private final String genre;
    private final float rating;
    private final String review;
    private boolean liked;

    public Book(
            int id,
            String title,
            String author,
            int year,
            String blurb,
            Integer coverResId,
            String coverUri,
            String genre,
            float rating,
            String review,
            boolean liked
    ) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.blurb = blurb;
        this.coverResId = coverResId;
        this.coverUri = coverUri;
        this.genre = genre;
        this.rating = rating;
        this.review = review;
        this.liked = liked;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public String getBlurb() {
        return blurb;
    }

    public Integer getCoverResId() {
        return coverResId;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public String getGenre() {
        return genre;
    }

    public float getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
