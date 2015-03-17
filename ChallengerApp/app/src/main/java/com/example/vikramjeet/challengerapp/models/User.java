package com.example.vikramjeet.challengerapp.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by Vikramjeet on 3/7/15.
 */
@ParseClassName("_User")
public class User extends ParseUser {

    // Constants for fields
    private static final String FIELD_NAME = "name";
    private static final String FIELD_PROVIDER = "provider"; // read-only
    private static final String FIELD_PROVIDER_ID = "providerId"; // read-only
    private static final String FIELD_LOCATION = "location";
    private static final String FIELD_POINTS_EARNED = "pointsEarned"; // read-only
    private static final String FIELD_CHALLENGES_COMPLETED = "challengesCompleted"; // read-only
    private static final String FIELD_CHALLENGES_BACKED = "challengesBacked"; // read-only
    private static final String FIELD_LEADERBOARD_RANK = "leaderBoardRank"; // read-only

    private static final String FACEBOOK_PROFILE_PICTURE = "https://graph.facebook.com/v2.2/%s/picture?type=large";

//    private List<Challenge> currentlyAcceptedChallenges;        // challenges that he needs to complete

    public User() {
        // A default constructor is required.
    }

    public String getName() {
        return getString(FIELD_NAME);
    }

    public void setName(String name) {
        put(FIELD_NAME, name);
    }

    public String getPhotoURL() {
        String photoURL = null;
        switch (getProvider()) {
            case FACEBOOK:
                photoURL = String.format(FACEBOOK_PROFILE_PICTURE, getProviderId());
                break;
            case TWITTER:
                break;
            case PARSE:
                break;
        }

        return photoURL;
    }

    public AuthProvider getProvider() {
        return AuthProvider.valueOf(getString(FIELD_PROVIDER));
    }

    public String getProviderId() {
        return getString(FIELD_PROVIDER_ID);
    }

    public String getLocation() {
        return getString(FIELD_LOCATION);
    }

    public void setLocation(String location) {
        put(FIELD_LOCATION, location);
    }

    public int getPointsEarned() {
        return getInt(FIELD_POINTS_EARNED);
    }

    public int getChallengesCompleted() {
        return getInt(FIELD_CHALLENGES_COMPLETED);
    }

    public int getChallengesBacked() {
        return getInt(FIELD_CHALLENGES_BACKED);
    }

    public int getLeaderBoardRank() {
        return getInt(FIELD_LEADERBOARD_RANK);
    }
}
