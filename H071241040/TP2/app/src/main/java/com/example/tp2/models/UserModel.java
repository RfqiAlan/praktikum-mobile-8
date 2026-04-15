package com.example.tp2.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String id;
    private String username;
    private String profilePicUrl;
    private int followersCount;
    private int followingCount;

    public UserModel(String id, String username, String profilePicUrl) {
        this.id = id;
        this.username = username;
        this.profilePicUrl = profilePicUrl;
        this.followersCount = (int) (Math.random() * 9900) + 100;
        this.followingCount = (int) (Math.random() * 900) + 50;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getProfilePicUrl() { return profilePicUrl; }
    public int getFollowersCount() { return followersCount; }
    public int getFollowingCount() { return followingCount; }
}
