package com.example.vikramjeet.challengerapp.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.models.MediaType;

public class MediaPagerAdapter extends FragmentPagerAdapter {

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
                return VideoFragment.newInstance(mChallenge.getCreatedMediaId(), MediaType.CREATED);
            } else {
                return ImageFragment.newInstance(mChallenge.getCreatedMedia());
            }
        } else if (position == 1) {
            if (mChallenge.isCompletedMediaVideo()) {
                return VideoFragment.newInstance(mChallenge.getCompletedMediaId(), MediaType.COMPLETED);
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
}
