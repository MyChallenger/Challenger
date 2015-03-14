package com.example.vikramjeet.challengerapp;

import android.app.Application;

import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.models.Comment;
import com.example.vikramjeet.challengerapp.models.User;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;

public class ChallengerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register Subclasses
        ParseObject.registerSubclass(Challenge.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Comment.class);

        // Required - Initialize the Parse SDK
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

        // Optional - If you don't want to allow Twitter login, you can
        // remove this line (and other related ParseTwitterUtils calls)
        ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
    }
}
