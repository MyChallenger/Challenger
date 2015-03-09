package com.example.vikramjeet.challengerapp.models;

import com.parse.ParseObject;

/**
 * Created by Vikramjeet on 3/9/15.
 */
public class Comment extends ParseObject {

    private String username;
    private String userProfileImageUrl;
    private String text;

    public Comment() {
        // default constructor
    }

    public String getUsername() {
        return username;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public String getText() {
        return text;
    }

}
