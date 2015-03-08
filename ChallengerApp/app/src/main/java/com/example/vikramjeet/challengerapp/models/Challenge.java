package com.example.vikramjeet.challengerapp.models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

/**
 * Created by Vikramjeet on 3/7/15.
 */
@ParseClassName("Challenge")
public class Challenge extends ParseObject {

    // Constants for fields
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_EXPIRY_DATE = "expiryDate";
    private static final String FIELD_CREATED_MEDIA_URL = "createdMediaURL";
    private static final String FIELD_COMPLETED_MEDIA_URL = "completedMediaURL";
    private static final String FIELD_NUMBER_OF_LIKES = "numberOfLikes";
    private static final String FIELD_NUMBER_OF_VIEWS = "numberOfViews";
    private static final String FIELD_NUMBER_OF_COMMENTS = "numberOfComments";
    private static final String FIELD_LOCATION = "location";
    private static final String FIELD_PRIZE = "prize";
    private static final String FIELD_CATEGORY = "category";
    private static final String FIELD_IS_COMPLETED = "isCompleted";

    public Challenge() {
        // A default constructor is required.
    }

    public String getTitle() {
        return getString(FIELD_TITLE);
    }

    public void setTitle(String title) {
        put(FIELD_TITLE, title);
    }

    public String getDescription() {
        return getString(FIELD_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(FIELD_DESCRIPTION, description);
    }

    public Date getExpiryDate() {
        return getDate(FIELD_EXPIRY_DATE);
    }

    public void setExpiryDate(Date expiryDate) {
        put(FIELD_EXPIRY_DATE, expiryDate);
    }

    public ParseFile getCreatedMedia() {
        return getParseFile(FIELD_CREATED_MEDIA_URL);
    }

    public void setCreatedMedia(ParseFile createdMedia) {
        put(FIELD_CREATED_MEDIA_URL, createdMedia);
    }

    public ParseFile getCompletedMedia() {
        return getParseFile(FIELD_COMPLETED_MEDIA_URL);
    }

    public void setCompletedMedia(ParseFile completedMedia) {
        put(FIELD_COMPLETED_MEDIA_URL, completedMedia);
    }

    public int getNumberOfLikes() {
        return getInt(FIELD_NUMBER_OF_LIKES);
    }

    public void setNumberOfLikes(int numberOfLikes) {
        put(FIELD_NUMBER_OF_LIKES, numberOfLikes);
    }

    public int getNumberOfViews() {
        return getInt(FIELD_NUMBER_OF_VIEWS);
    }

    public void setNumberOfViews(int numberOfViews) {
        put(FIELD_NUMBER_OF_VIEWS, numberOfViews);
    }

    public int getNumberOfComments() {
        return getInt(FIELD_NUMBER_OF_COMMENTS);
    }

    public void setNumberOfComments(int numberOfComments) {
        put(FIELD_NUMBER_OF_COMMENTS, numberOfComments);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(FIELD_LOCATION);
    }

    public void setLocation(ParseGeoPoint location) {
        put(FIELD_LOCATION, location);
    }

    public String getPrize() {
        return getString(FIELD_PRIZE);
    }

    public void setPrize(String prize) {
        put(FIELD_PRIZE, prize);
    }

    public String getCategory() {
        return getString(FIELD_CATEGORY);
    }

    public void setCategory(String category) {
        put(FIELD_CATEGORY, category);
    }

    public Boolean isCompleted() {
        return getBoolean(FIELD_IS_COMPLETED);
    }

    public void setCompleted(Boolean isCompleted) {
        put(FIELD_IS_COMPLETED, isCompleted);
    }

    public static void getOpenChallenges(FindCallback<Challenge> findCallback) {
        ParseQuery<Challenge> query = getChallengeParseQuery();
        query.whereEqualTo(FIELD_IS_COMPLETED, false);
        query.findInBackground(findCallback);
    }

    public static void getFinishedChallenges(FindCallback<Challenge> findCallback) {
        ParseQuery<Challenge> query = getChallengeParseQuery();
        query.whereEqualTo(FIELD_IS_COMPLETED, true);
        query.findInBackground(findCallback);
    }

    private static ParseQuery<Challenge> getChallengeParseQuery() {
        return ParseQuery.getQuery(Challenge.class);
    }

}
