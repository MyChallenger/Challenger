package com.example.vikramjeet.challengerapp.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.vikramjeet.challengerapp.configurations.Auth;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class MediaPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = MediaPagerAdapter.class.getName();

    YouTubePlayer mYouTubePlayer;

    private final Challenge mChallenge;
    private String tabTitles[] = {"Info", "Tagline"};

    public MediaPagerAdapter(FragmentManager fm) {
        super(fm);
        mChallenge = null;
    }

    public MediaPagerAdapter(FragmentManager fm, Challenge challenge) {
        super(fm);
        mChallenge = challenge;
    }



    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            if (mChallenge.isCreatedMediaVideo()) {
                YouTubePlayerSupportFragment playerFragment = YouTubePlayerSupportFragment
                        .newInstance();
                playerFragment.initialize(Auth.KEY,
                        new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(
                                    YouTubePlayer.Provider provider,
                                    YouTubePlayer youTubePlayer, boolean b) {
                                mYouTubePlayer = youTubePlayer;
                                mYouTubePlayer.cueVideo(mChallenge.getCreatedMediaId());
                            }

                            @Override
                            public void onInitializationFailure(
                                    YouTubePlayer.Provider provider,
                                    YouTubeInitializationResult result) {
                                Log.e(TAG, result.toString());
                            }
                        });
                return playerFragment;
            } else {
                return ImageFragment.newInstance(mChallenge.getCreatedMedia());
            }
        } else if (position == 1) {
            if (mChallenge.isCompletedMediaVideo()) {
                YouTubePlayerSupportFragment playerFragment = YouTubePlayerSupportFragment.newInstance();
                playerFragment.initialize(Auth.KEY,
                        new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(
                                    YouTubePlayer.Provider provider,
                                    YouTubePlayer youTubePlayer, boolean b) {
                                mYouTubePlayer = youTubePlayer;
                            }

                            @Override
                            public void onInitializationFailure(
                                    YouTubePlayer.Provider provider,
                                    YouTubeInitializationResult result) {
                                Log.e(TAG, result.toString());
                            }
                        });
                return playerFragment;
            } else {
                return ImageFragment.newInstance(mChallenge.getCompletedMedia());
            }
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    public void onPageSelected(int position) {
        if (position == 0) {
            mYouTubePlayer.cueVideo(mChallenge.getCreatedMediaId());
        } else if (position == 1) {
            mYouTubePlayer.cueVideo(mChallenge.getCompletedMediaId());
        }
    }
}
