package com.example.vikramjeet.challengerapp;

import com.example.vikramjeet.challengerapp.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class UserTest extends ChallengerTestCase {

    private static final String FACEBOOK_PROFILE_PICTURE = "https://graph.facebook.com/v2.2/%s/picture?type=large";

    public void testFetchUser() {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo("provider", "FACEBOOK");
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> facebookUsers, ParseException e) {
                for (User facebookUser : facebookUsers) {
                    assertNotNull(facebookUser.getProviderId());
                    String photoURL = String.format(FACEBOOK_PROFILE_PICTURE, facebookUser.getProviderId());
                    assertEquals(photoURL, facebookUser.getPhotoURL());
                }
                countDown();
            }
        });
        await();
    }

    public void testUpdate() {
        User user = (User) ParseUser.getCurrentUser();
        user.setLocation("Palo Alto, CA");
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                assertNull(e);
                countDown();
            }
        });
        await();
    }
}

