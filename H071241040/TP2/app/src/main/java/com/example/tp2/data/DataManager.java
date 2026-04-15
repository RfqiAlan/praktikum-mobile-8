package com.example.tp2.data;

import com.example.tp2.models.FeedModel;
import com.example.tp2.models.HighlightModel;
import com.example.tp2.models.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static DataManager instance;

    // Users
    private Map<String, UserModel> usersMap;
    public static final String MY_USER_ID = "my_user_id";

    // Feeds by UserId
    private Map<String, List<FeedModel>> userFeedsMap;
    // Highlights by UserId
    private Map<String, List<HighlightModel>> userHighlightsMap;

    // Single unified list for Home Feed
    private List<FeedModel> homeFeeds;

    private DataManager() {
        usersMap = new HashMap<>();
        userFeedsMap = new HashMap<>();
        userHighlightsMap = new HashMap<>();
        homeFeeds = new ArrayList<>();
        initDummyData();
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void initDummyData() {
        // 1. Init My Account (User)
        UserModel myUser = new UserModel(MY_USER_ID, "rifqialan", "https://picsum.photos/seed/" + MY_USER_ID + "/150/150");
        usersMap.put(myUser.getId(), myUser);
        userFeedsMap.put(myUser.getId(), new ArrayList<>());
        userHighlightsMap.put(myUser.getId(), new ArrayList<>());

        // Init My Profile Highlights (7 items minimum)
        for (int i = 1; i <= 7; i++) {
            userHighlightsMap.get(myUser.getId()).add(new HighlightModel(
                    "my_hl_" + i,
                    "Story " + i,
                    "https://picsum.photos/seed/my_hl_" + i + "/150/150"
            ));
        }

        // Init My Feeds (5 items minimum)
        for (int i = 1; i <= 6; i++) {
            FeedModel myPost = new FeedModel(
                    "my_post_" + i,
                    myUser.getId(),
                    myUser.getUsername(),
                    myUser.getProfilePicUrl(),
                    "https://picsum.photos/seed/my_post_" + i + "/800/800",
                    "This is my post number " + i + " #beautiful",
                    true
            );
            userFeedsMap.get(myUser.getId()).add(myPost);
            homeFeeds.add(myPost);
        }

        // 2. Init 10 Other Dummy Users
        for (int userIdx = 1; userIdx <= 10; userIdx++) {
            String uid = "other_user_" + userIdx;
            UserModel otherUser = new UserModel(uid, "user_cool_" + userIdx, "https://picsum.photos/seed/" + uid + "/150/150");
            usersMap.put(otherUser.getId(), otherUser);
            
            userFeedsMap.put(otherUser.getId(), new ArrayList<>());
            userHighlightsMap.put(otherUser.getId(), new ArrayList<>());

            // Add highlights to other users (Optional, let's just give 4 highlights each so their profiles look good)
            for (int i = 1; i <= 4; i++) {
                userHighlightsMap.get(otherUser.getId()).add(new HighlightModel(
                        uid + "_hl_" + i,
                        "Memories " + i,
                        "https://picsum.photos/seed/" + uid + "_hl_" + i + "/150/150"
                ));
            }

            // Each other user has ~2 posts to meet the minimum 10 dummy items in Home overall
            for (int i = 1; i <= 2; i++) {
                FeedModel otherPost = new FeedModel(
                        uid + "_post_" + i,
                        otherUser.getId(),
                        otherUser.getUsername(),
                        otherUser.getProfilePicUrl(),
                        "https://picsum.photos/seed/" + uid + "_post_" + i + "/800/800",
                        "Amazing view from me! " + i,
                        false
                );
                userFeedsMap.get(otherUser.getId()).add(otherPost);
                homeFeeds.add(otherPost);
            }
        }

        // Shuffle home feeds so they appear realistic
        Collections.shuffle(homeFeeds);
    }

    public List<FeedModel> getHomeFeeds() {
        return homeFeeds;
    }

    public UserModel getUser(String userId) {
        return usersMap.get(userId);
    }

    public List<FeedModel> getUserFeeds(String userId) {
        return userFeedsMap.getOrDefault(userId, new ArrayList<>());
    }

    public List<HighlightModel> getUserHighlights(String userId) {
        return userHighlightsMap.getOrDefault(userId, new ArrayList<>());
    }

    public void addNewPost(String description, String imageUrl) {
        UserModel myUser = getUser(MY_USER_ID);
        String newId = "my_post_new_" + System.currentTimeMillis();
        FeedModel newPost = new FeedModel(
                newId,
                myUser.getId(),
                myUser.getUsername(),
                myUser.getProfilePicUrl(),
                imageUrl,
                description,
                true
        );
        // Add to top of personal feed
        userFeedsMap.get(myUser.getId()).add(0, newPost);
        // Add to top of home feed
        homeFeeds.add(0, newPost);
    }
}
