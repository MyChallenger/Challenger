package com.example.vikramjeet.challengerapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.vikramjeet.challengerapp.models.Challenge;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

public class ChallengeTest extends ChallengerTestCase {

    private void populateSampleData() {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        for (int i = 0; i < 50; i++) {
            Challenge challenge = new Challenge();
            challenge.setTitle("Open Challenge # " + (i + 1));
            challenge.setDescription("Will record a video and post!");
            Date aWeekFromNow = new Date(System.currentTimeMillis() + (7 * DAY_IN_MS));
            challenge.setExpiryDate(aWeekFromNow);
//            ParseFile createdMedia = new ParseFile("createdMedia.txt", "I'm a created media!".getBytes());
//            try {
//                createdMedia.save();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            challenge.setCreatedMedia(createdMedia);
//            ParseFile completedMedia = new ParseFile("completedMedia.txt", "I'm a completed media!".getBytes());
//            try {
//                completedMedia.save();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            challenge.setCompletedMedia(completedMedia);
//
            challenge.setNumberOfLikes(100);
            challenge.setNumberOfViews(205678);
            challenge.setNumberOfComments(15);
            // 1 Market Street, San Francisco CA
            challenge.setLocation(new ParseGeoPoint(37.793992, -122.394896));
            challenge.setPrize("Asking for $10 for beer");
            challenge.setCategory("Dare");
            challenge.setCompleted(false);
            try {
                challenge.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 50; i++) {
            Challenge challenge = new Challenge();
            challenge.setTitle("Finished Challenge # " + (i + 1));
            challenge.setDescription("Will record a video and post!");
            Date yesterday = new Date(System.currentTimeMillis() - (1 * DAY_IN_MS));
            challenge.setExpiryDate(yesterday);
//            ParseFile createdMedia = new ParseFile("createdMedia.txt", "I'm a created media!".getBytes());
//            try {
//                createdMedia.save();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            challenge.setCreatedMedia(createdMedia);
//            ParseFile completedMedia = new ParseFile("completedMedia.txt", "I'm a completed media!".getBytes());
//            try {
//                completedMedia.save();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            challenge.setCompletedMedia(completedMedia);
//
            challenge.setNumberOfLikes(100);
            challenge.setNumberOfViews(205678);
            challenge.setNumberOfComments(15);
            // 1 Market Street, San Francisco CA
            challenge.setLocation(new ParseGeoPoint(37.793992, -122.394896));
            challenge.setPrize("Asking for $10 for beer");
            challenge.setCategory("Dare");
            challenge.setCompleted(true);
            try {
                challenge.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void testAdd() {
        final Challenge challenge = new Challenge();
        challenge.setTitle("Stand on top of a checkout counter in a public store");
        challenge.setDescription("Will record a video and post!");
        challenge.setExpiryDate(new Date());
        challenge.setNumberOfLikes(100);
        challenge.setNumberOfViews(205678);
        challenge.setNumberOfComments(15);
        // 1 Market Street, San Francisco CA
        challenge.setLocation(new ParseGeoPoint(37.793992, -122.394896));
        challenge.setPrize("Asking for $10 for beer");
        challenge.setCategory("Dare");
        challenge.setCompleted(false);
        // We will save the createdMedia first and then save the challenge if the upload succeeds!
        Drawable backgroundImage = getContext().getResources().getDrawable(R.drawable.background);
        if (backgroundImage != null) {
            Bitmap bitmap = ((BitmapDrawable)backgroundImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            final ParseFile createdMedia = new ParseFile("createdMedia.jpeg", bitmapdata);
            createdMedia.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    assertNull(e);
                    challenge.setCreatedMedia(createdMedia);
                    challenge.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            assertNull(e);
                            countDown();
                        }
                    });
                }
            });
            await();
        }
    }

    public void testBack() {
        // Let's create a challenge first
        final Challenge challenge = new Challenge();
        challenge.setTitle("Backed challenge");
        challenge.setDescription("Will record a video and post!");
        challenge.setExpiryDate(new Date());
        challenge.setNumberOfLikes(100);
        challenge.setNumberOfViews(205678);
        challenge.setNumberOfComments(15);
        // 1 Market Street, San Francisco CA
        challenge.setLocation(new ParseGeoPoint(37.793992, -122.394896));
        challenge.setPrize("Asking for $10 for beer");
        challenge.setCategory("Dare");
        challenge.setCompleted(false);
        challenge.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // This challenge is not backed yet!
                assertNull(challenge.backer());
                challenge.back(new GetCallback<Challenge>() {
                    @Override
                    public void done(Challenge backedChallenge, ParseException e) {
                        // This challenge should be backed!
                        assertNotNull(backedChallenge.backer());
                        countDown();
                    }
                });
            }
        });
        await();
    }

    public void testGetOpenChallengesAsync() {
        Challenge.getOpenChallenges(new FindCallback<Challenge>() {
            @Override
            public void done(List<Challenge> results, ParseException e) {
                for (Challenge challenge : results) {
                    // Challenges should always have a poster
                    assertNotNull(challenge.poster());
                    assertFalse(challenge.isCompleted());
                }
                countDown();
            }
        });
        await();
    }

    public void testGetFinishedChallengesAsync() {
        Challenge.getFinishedChallenges(new FindCallback<Challenge>() {
            @Override
            public void done(List<Challenge> results, ParseException e) {
                for (Challenge challenge : results) {
                    // Challenges should always have a poster
                    assertNotNull(challenge.poster());
                    // Finished Challenges should always have a backer
                    assertNotNull(challenge.backer());
                    assertTrue(challenge.isCompleted());
                }
                countDown();
            }
        });
        await();
    }
}

