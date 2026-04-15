package com.example.tp2.models;

import java.io.Serializable;

public class FeedModel implements Serializable {
    private String id;
    private String userId; // Added userId
    private String username;
    private String userProfilePicUrl;
    private String postImageUrl;
    private String caption;
    private boolean isOwnPost;

    public FeedModel(String id, String userId, String username, String userProfilePicUrl, String postImageUrl, String caption, boolean isOwnPost) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.userProfilePicUrl = userProfilePicUrl;
        this.postImageUrl = postImageUrl;
        this.caption = caption;
        this.isOwnPost = isOwnPost;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getUserProfilePicUrl() { return userProfilePicUrl; }
    public String getPostImageUrl() { return postImageUrl; }
    public String getCaption() { return caption; }
    public boolean isOwnPost() { return isOwnPost; }
}
