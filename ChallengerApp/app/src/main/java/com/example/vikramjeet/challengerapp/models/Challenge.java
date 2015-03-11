package com.example.vikramjeet.challengerapp.models;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Vikramjeet on 3/7/15.
 */
@ParseClassName("Challenge")
public class Challenge extends ParseObject {

    // Constants for fields
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_EXPIRY_DATE = "expiryDate";
    private static final String FIELD_CREATED_MEDIA = "createdMedia";
    private static final String FIELD_COMPLETED_MEDIA = "completedMedia";
    private static final String FIELD_NUMBER_OF_LIKES = "numberOfLikes";
    private static final String FIELD_NUMBER_OF_VIEWS = "numberOfViews";
    private static final String FIELD_NUMBER_OF_COMMENTS = "numberOfComments";
    private static final String FIELD_LOCATION = "location";
    private static final String FIELD_PRIZE = "prize";
    private static final String FIELD_CATEGORY = "category";
    private static final String FIELD_POSTER = "poster";
    private static final String FIELD_BACKER = "backer";
    private static final String FIELD_STATUS = "status";

    // Other constants
    private static final String CHALLENGE_ID = "challengeId";
    private static final String BACK_CHALLENGE_URL = "backChallenge";

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
        return getParseFile(FIELD_CREATED_MEDIA);
    }

    public void setCreatedMedia(ParseFile createdMedia) {
        put(FIELD_CREATED_MEDIA, createdMedia);
    }

    public ParseFile getCompletedMedia() {
        return getParseFile(FIELD_COMPLETED_MEDIA);
    }

    public void setCompletedMedia(ParseFile completedMedia) {
        put(FIELD_COMPLETED_MEDIA, completedMedia);
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

    public ChallengeStatus getStatus() {
        return ChallengeStatus.valueOf(getString(FIELD_STATUS));
    }

    public static void getOpenChallenges(FindCallback<Challenge> findCallback) {
        ParseQuery<Challenge> query = getChallengeParseQuery();
        Date now = new Date();
        query.whereGreaterThan(FIELD_EXPIRY_DATE, now);
        String[] statuses = {ChallengeStatus.OPEN.toString(), ChallengeStatus.BACKED.toString()};
        query.whereContainedIn(FIELD_STATUS, Arrays.asList(statuses));
                // Show the ones expiring soonest first
                        query.orderByAscending(FIELD_EXPIRY_DATE);
        query.findInBackground(findCallback);
    }

    public static void getFinishedChallenges(FindCallback<Challenge> findCallback) {
        ParseQuery<Challenge> query = getChallengeParseQuery();
        query.whereEqualTo(FIELD_STATUS, ChallengeStatus.VERIFIED.toString());
        query.findInBackground(findCallback);
    }

    private static ParseQuery<Challenge> getChallengeParseQuery() {
        ParseQuery<Challenge> query = ParseQuery.getQuery(Challenge.class);
        query.include(FIELD_POSTER);
        query.include(FIELD_BACKER);
        return query;
    }

    public User poster() {
        return (User) getParseUser(FIELD_POSTER);
    }

    public User backer() {
        return (User) getParseUser(FIELD_BACKER);
    }

// Request:
//    curl -X POST \
//            -H "X-Parse-Application-Id: gOqloKyikrHShtt0qNC9NcOpJipx2ijnVepC1dX1" \
//            -H "X-Parse-REST-API-Key: RQwuBnSNpKlm5bRVJktDwDqgHbgqt4KZeETB0Cks" \
//            -H "X-Parse-Session-Token: 6gBQTkhmnmlZvbar6Wy0aNBwW" \
//            -H "Content-Type: application/json" \
//            -d '{"challengeId" : "7yyGTSK3FQ"}' \
//    https://api.parse.com/1/functions/backChallenge
// Response:
//    {"result":true}
    public void back(final GetCallback<Challenge> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put(CHALLENGE_ID, getObjectId());
        ParseCloud.callFunctionInBackground(BACK_CHALLENGE_URL, params, new FunctionCallback<Boolean>() {
            @Override
            public void done(Boolean success, ParseException e) {
                if (success) {
                    // Challenge should have a backer now!
                    fetchInBackground(callback);
                }
            }
        });
    }

    public static void getChallengeByID(String challengeID, GetCallback<Challenge> getCallback) {
        ParseQuery<Challenge> query = ParseQuery.getQuery(Challenge.class);
        // First try to find from the cache and only then go to network
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
        // Execute the query to find the object with ID
        query.getInBackground(challengeID, getCallback);
    }

    public static boolean isVideo(String mediaURL) {
        String extension = "";

        int index = mediaURL.lastIndexOf('.');
        if (index >= 0) {
            extension = mediaURL.substring(index + 1);
        }

        // Check if extension is an image extension
        if (extension.equals("jpg")
                || extension.equals("jpeg")
                || extension.equals("png")
                || extension.equals("gif")
                || extension.equals("tiff")
                || extension.equals("tif")) {

            return false;
        }
        return true;
    }

}
