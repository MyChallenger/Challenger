package com.example.vikramjeet.challengerapp.models;

/**
 * Created by Vikramjeet on 3/7/15.
 */
public class Challenge {

    private String title;
    private String description;
    private String createdAt;
    private String expiryDate;
    // Need ot create a user class as well
    // private User acceptedBy;
    // private User createdBy
    private String createdMediaURL;
    private String completedMediaURL;
    private int numberOfLikes;
    private int numberOfViews;
    private int numberOfComments; // Need a comment object as well I believe
    private String location;
    private String prize;
    private String category;
    private Boolean completed;

    public Challenge() {
        super();
    }
}
