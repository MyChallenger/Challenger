package com.example.vikramjeet.challengerapp;

import android.test.AndroidTestCase;

import com.example.vikramjeet.challengerapp.models.User;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserTest extends AndroidTestCase {

    private static final String FACEBOOK_PROFILE_PICTURE = "https://graph.facebook.com/v2.2/%s/picture?type=large";

    protected void setUp() {
        Parse.initialize(getContext(), getContext().getString(R.string.parse_app_id),
                getContext().getString(R.string.parse_client_key));
    }

    public void testFetchUser() {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo("provider", "FACEBOOK");
        try {
            List<User> users = query.find();
            for (User user : users) {
                assertNotNull(user.getProviderId());
                String photoURL = String.format(FACEBOOK_PROFILE_PICTURE, user.getProviderId());
                assertEquals(photoURL, user.getPhotoURL());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void testUpdate() {
        User user = (User) ParseUser.getCurrentUser();
        user.setLocation("Palo Alto, CA");
        boolean isSaved;
        try {
            user.save();
            isSaved = true;
        } catch (ParseException e) {
            e.printStackTrace();
            isSaved = false;
        }
        assertFalse(user.isDirty());
        assertTrue(isSaved);
    }
}

