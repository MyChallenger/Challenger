package com.example.vikramjeet.challengerapp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;

import com.example.vikramjeet.challengerapp.models.Challenge;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class ChallengeTest extends TestCase {

    protected void setUp() {
    }

    public void testAdd() {
        Challenge challenge = new Challenge();
        challenge.setTitle("Stand on top of a checkout counter in a public store");
        challenge.setDescription("Will record a video and post!");
        challenge.setExpiryDate(new Date());
        ParseFile createdMedia = new ParseFile("createdMedia.txt", "I'm a created media!".getBytes());
        try {
            createdMedia.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        challenge.setCreatedMedia(createdMedia);
        ParseFile completedMedia = new ParseFile("completedMedia.txt", "I'm a completed media!".getBytes());
        try {
            completedMedia.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        challenge.setCompletedMedia(completedMedia);
        challenge.setNumberOfLikes(100);
        challenge.setNumberOfViews(205678);
        challenge.setNumberOfComments(15);
        // 1 Market Street, San Francisco CA
        challenge.setLocation(new ParseGeoPoint(37.793992, -122.394896));
        challenge.setPrize("Asking for $10 for beer");
        challenge.setCategory("Dare");
        challenge.setCompleted(false);
        boolean isSaved;
        try {
            challenge.save();
            isSaved = true;
        } catch (ParseException e) {
            e.printStackTrace();
            isSaved = false;
        }
        assertFalse(challenge.isDirty());
        assertTrue(isSaved);
    }

    public void testAddAsync() {
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
//        Drawable backgroundImage = getResources().getDrawable(R.drawable.background);
        Drawable backgroundImage = null;
        if (backgroundImage != null) {
            Bitmap bitmap = ((BitmapDrawable)backgroundImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            final ParseFile createdMedia = new ParseFile("createdMedia.jpeg", bitmapdata);
            createdMedia.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    challenge.setCreatedMedia(createdMedia);
                    challenge.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                        }
                    });
                }
            });
        }
    }
}

