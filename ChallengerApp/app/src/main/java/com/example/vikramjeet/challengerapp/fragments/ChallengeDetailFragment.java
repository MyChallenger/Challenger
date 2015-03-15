package com.example.vikramjeet.challengerapp.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.astuetz.PagerSlidingTabStrip;
import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

/**
 * Created by Vikramjeet on 3/8/15.
 */
public class ChallengeDetailFragment extends Fragment {

    private final int CHALLENGE_TYPE_IMAGE = 0;
    private final int CHALLENGE_TYPE_VIDEO = 1;

    private Challenge challenge;
    private String challengeId;
    private int fragmentType;

    private ViewPager vpPager;
    private PagerSlidingTabStrip tabStrip;
    private ImageView ivUserPhoto;
    private TextView tvUsername;
    private ImageView ivChallengeImage;
    private VideoView vvChallengeVideo;
    private TextView tvLikes;
    private TextView tvViews;
    private TextView tvCategory;
    private TextView tvTitle;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments and populate fragmentType
        challengeId = getArguments().getString("challenge_id");
        fragmentType = getArguments().getBoolean("challenge_fragment_type") ? 1 : 0;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = null;

        // Inflate the fragment view
        if (fragmentType == CHALLENGE_TYPE_IMAGE) {
            view = inflater.inflate(R.layout.fragment_challenge_detail_image, parent, false);
            ivChallengeImage = (ImageView) view.findViewById(R.id.ivChallengeDetail);
        } else {
            view = inflater.inflate(R.layout.fragment_challenge_detail_video, parent, false);
            vvChallengeVideo = (VideoView) view.findViewById(R.id.vvChallengeDetail);
        }

        ivUserPhoto = (ImageView) view.findViewById(R.id.ivChallengeDetailUserPhoto);
        tvUsername = (TextView) view.findViewById(R.id.tvChallengeDetailUserName);
        tvLikes = (TextView) view.findViewById(R.id.tvChallengeDetailLike);
        tvViews = (TextView) view.findViewById(R.id.tvChallengeDetailViews);
        tvTitle = (TextView) view.findViewById(R.id.tvChallengeDetailTitle);
        tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        vpPager = (ViewPager) view.findViewById(R.id.vpChallengerDetail);
        tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.challengeDetailTabs);
        populateViews();
        // Set view page adapter for the pager
        vpPager.setAdapter(new ChallengeDetailPagerAdapter(getChildFragmentManager()));
        // Attach tabstrip to the viewpager
        tabStrip.setViewPager(vpPager);

        return view;
    }

    private void populateViews() {
//        Get challenge from challenge_id
        Log.d("ReceivedObjectID:", challengeId);
        Challenge.getChallengeByID(challengeId, new GetCallback<Challenge>() {
            public void done(Challenge item, ParseException e) {
                if (e == null) {
                    // item was found
                    challenge = item;
                    // Populate views

                    // Populate user info
//                    tvUsername.setText(challenge.getPoster().getName());
//
///                    Transformation transformation = new RoundedTransformationBuilder()
//                            .borderColor(Color.BLACK)
//                            .cornerRadiusDp(25)
//                            .oval(false)
//                            .build();

//                    Picasso.with(getActivity()).
//                            load(challenge.getPoster().getPhotoURL()).
//                            fit().
//                            transform(transformation).
//                            placeholder(R.drawable.photo_placeholder).
//                            into(ivUserPhoto);

                    tvTitle.setText(challenge.getTitle());
                    tvCategory.setText(challenge.getCategory());
                    tvLikes.setText(String.valueOf(challenge.getNumberOfLikes()));
                    tvViews.setText(String.valueOf(challenge.getNumberOfViews()));

                    if (challenge.getCompletedMedia() != null) {

                        if (Challenge.isVideo(challenge.getCompletedMedia().getUrl())) {
                            if (vvChallengeVideo.isPlaying()) {
                                vvChallengeVideo.stopPlayback();
                            }

                            vvChallengeVideo.setVideoPath(challenge.getCompletedMedia().getUrl());
                            MediaController mediaController = new MediaController(getActivity());
                            mediaController.setAnchorView(vvChallengeVideo);
                            vvChallengeVideo.setMediaController(mediaController);
                            vvChallengeVideo.requestFocus();

                            vvChallengeVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                // Close the progress bar and play the video
                                public void onPrepared(MediaPlayer mp) {
//                            mp.setLooping(true);
                                    vvChallengeVideo.start();
                                    // Dismiss spinner
//                                    spinnerView.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            Picasso.with(getActivity()).
                                    load(challenge.getCompletedMedia().getUrl()).
                                    placeholder(R.drawable.photo_placeholder).
                                    into(ivChallengeImage);
                        }


                    }
                }
            }
        });
    }

    public class ChallengeDetailPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = { getString(R.string.description), getString(R.string.comments) };

        public ChallengeDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new ChallengeDescriptionFragment();
            }

            return CommentListFragment.newInstance(challengeId, true);
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
