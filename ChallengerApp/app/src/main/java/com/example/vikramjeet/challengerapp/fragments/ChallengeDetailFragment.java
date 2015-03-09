package com.example.vikramjeet.challengerapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.parse.GetCallback;
import com.parse.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vikramjeet on 3/8/15.
 */
public class ChallengeDetailFragment extends Fragment {

    private final int CHALLENGE_TYPE_IMAGE = 0;
    private final int CHALLENGE_TYPE_VIDEO = 1;

    private Challenge challenge;
    private String challengeId;
    private int fragmentType;
    private FragmentActivity myContext;

    @InjectView(R.id.vpChallengerDetail)
    ViewPager vpPager;
    @InjectView(R.id.challengeDetailTabs)
    PagerSlidingTabStrip tabStrip;

    public static ChallengeDetailFragment newInstance(String challengeID, boolean isVideoChallenge) {
        // Create fragment
        ChallengeDetailFragment fragment = new ChallengeDetailFragment();
        // Create Bundle
        Bundle args = new Bundle();
        // Populate bundle
        args.putString("challenge_id", challengeID);
        args.putBoolean("challenge_fragment_type", isVideoChallenge);
        // Set arguments to fragment
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments and populate fragmentType
        challengeId = getArguments().getString("challenge_id");


        // Get challenge from challenge_id
        Challenge.getChallengeByID(challengeId, new GetCallback<Challenge>() {
            public void done(Challenge item, ParseException e) {
                if (e == null) {
                    // item was found
                    challenge = item;
                }
            }
        });
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = null;

        // Inflate the fragment view
        if (fragmentType == CHALLENGE_TYPE_IMAGE) {
            view = inflater.inflate(R.layout.fragment_challenge_detail_image, parent, false);
        } else {
            view = inflater.inflate(R.layout.fragment_challenge_detail_video, parent, false);
        }
        // Inject Butterknife
        ButterKnife.inject(this, view);
        // Set view page adapter for the pager
        vpPager.setAdapter(new ChallengeDetailPagerAdapter(myContext.getSupportFragmentManager()));
        // Attach tabstrip to the viewpager
        tabStrip.setViewPager(vpPager);

        return view;
    }

    public class ChallengeDetailPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = { getString(R.string.description), getString(R.string.comments) };

        public ChallengeDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
//                return new OpenChallengesFragment();
            }
            return new CompletedChallengesFragment();
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
