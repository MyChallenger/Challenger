package com.example.vikramjeet.challengerapp.fragments;

import android.graphics.Color;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.configurations.Auth;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.models.ChallengeStatus;
import com.example.vikramjeet.challengerapp.models.User;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.makeramen.RoundedTransformationBuilder;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vikramjeet on 3/8/15.
 */
public class ChallengeDetailFragment extends Fragment implements YouTubePlayer.PlayerStateChangeListener, YouTubePlayer.OnFullscreenListener {

    private final int CHALLENGE_TYPE_IMAGE = 0;
    private final int CHALLENGE_TYPE_VIDEO = 1;

    private Challenge challenge;
    private String challengeId;
    private int fragmentType;

    private static final String YOUTUBE_FRAGMENT_TAG = "youtube";
    private YouTubePlayer mYouTubePlayer;
    private boolean mIsFullScreen = false;

    private ViewPager vpPager;
    private PagerSlidingTabStrip tabStrip;
    private ImageView ivUserPhoto;
    private TextView tvUsername;
    private ImageView ivChallengeImage;
    private FrameLayout flChallengeDetail;
    private TextView tvLikes;
    private TextView tvViews;
    private TextView btnStatus;
    private TextView tvTitle;

    private ChallengeDescriptionFragment descriptionFragment;
    private CommentListFragment commentFragment;

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
            flChallengeDetail = (FrameLayout) view.findViewById(R.id.flChallengeDetail);
        }

        ivUserPhoto = (ImageView) view.findViewById(R.id.ivChallengeDetailUserPhoto);
        tvUsername = (TextView) view.findViewById(R.id.tvChallengeDetailUserName);
        tvLikes = (TextView) view.findViewById(R.id.tvChallengeDetailLike);
        tvViews = (TextView) view.findViewById(R.id.tvChallengeDetailViews);
        tvTitle = (TextView) view.findViewById(R.id.tvChallengeDetailTitle);
        btnStatus = (Button) view.findViewById(R.id.btnStatus);
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
                    tvUsername.setText(challenge.getPoster().getName());

                    Transformation transformation = new RoundedTransformationBuilder()
                            .borderColor(Color.BLACK)
                            .cornerRadiusDp(25)
                            .oval(false)
                            .build();

                    Picasso.with(getActivity()).
                            load(challenge.getPoster().getPhotoURL()).
                            fit().
                            transform(transformation).
                            placeholder(R.drawable.photo_placeholder).
                            into(ivUserPhoto);

                    tvTitle.setText(challenge.getTitle());
//                    tvCategory.setText(challenge.getCategory());
                    tvLikes.setText(String.valueOf(challenge.getNumberOfLikes()));
                    tvViews.setText(String.valueOf(challenge.getNumberOfViews()));

                    configureButton();

                    if (challenge.isVideo()) {
                        panToVideo(challenge.getCompletedMediaId());
                    } else {
                        if (challenge.getCompletedMedia() != null) {
                            Picasso.with(getActivity()).
                                    load(challenge.getCompletedMedia().getUrl()).
                                    placeholder(R.drawable.photo_placeholder).
                                    into(ivChallengeImage);
                        }
                    }
                }

                // Populate dictionary with challenge detail
                Map<String, String> map = new HashMap<String, String>();
                map.put("description", challenge.getDescription());
                map.put("challenger_name", challenge.getPoster().getName());
                map.put("backer_name", challenge.getBacker().getName());
//                map.put("location", challenge.getLocation().toString());
                map.put("category", challenge.getCategory());

                // Populate description view fragment
                descriptionFragment.populateViewsFromDict(map);
            }
        });
    }

    private void configureButton() {
        User currentUser = (User) ParseUser.getCurrentUser();

        // Set proper button text
        ChallengeStatus status = challenge.getStatus();

        switch(status) {
            case OPEN: {
                if (currentUser != challenge.getPoster()) {
                    btnStatus.setText("SPONSOR");
                }
                btnStatus.setText("OPEN");
                btnStatus.setEnabled(false);
                break;
            }
            case BACKED: {
                if (currentUser == challenge.getPoster()) {
                    btnStatus.setText("COMPLETE");
                }
                btnStatus.setText("BACKED");
                btnStatus.setEnabled(false);
                break;
            }
            case COMPLETED: {
                if (currentUser == challenge.getBacker()) {
                    btnStatus.setText("VERIFY");
                }
                btnStatus.setText("COMPLETED");
                btnStatus.setEnabled(false);
                break;
            }
            case VERIFIED: {
                btnStatus.setText("VERIFIED");
                btnStatus.setEnabled(false);
                break;
            }
            default:
                break;
        }

        // Add click listeners
        addClickListenerToStatusButton();
    }

    private void addClickListenerToStatusButton() {
        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get challenge status
                ChallengeStatus status = challenge.getStatus();

                switch (status) {
                    case OPEN: {
                        sponsorChallenge();
                        break;
                    }
                    case BACKED: {
                        completeChallenge();
                        break;
                    }
                    case COMPLETED: {
                        verifyChallenge();
                        break;
                    }
                    case VERIFIED: {
                        btnStatus.setText("VERIFIED");
                        btnStatus.setEnabled(false);
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }

    private void sponsorChallenge() {
        challenge.back(new GetCallback<Challenge>() {
            @Override
            public void done(Challenge challenge, ParseException e) {
                //Update button
                btnStatus.setText("SPONSORED");
                btnStatus.setEnabled(false);
            }
        });
    }

    private void completeChallenge() {
        challenge.complete(new GetCallback<Challenge>() {
            @Override
            public void done(Challenge challenge, ParseException e) {
                //Update button
                btnStatus.setText("COMPLETED");
                btnStatus.setEnabled(false);
            }
        });
    }

    private void verifyChallenge() {
        challenge.verify(new GetCallback<Challenge>() {
            @Override
            public void done(Challenge challenge, ParseException e) {
                //Update button
                btnStatus.setText("VERIFIED");
                btnStatus.setEnabled(false);
            }
        });
    }

    public void panToVideo(final String youtubeId) {
        popPlayerFromBackStack();
        YouTubePlayerSupportFragment playerFragment = YouTubePlayerSupportFragment
                .newInstance();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flChallengeDetail, playerFragment,
                        YOUTUBE_FRAGMENT_TAG)
                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null).commit();
        playerFragment.initialize(Auth.KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(
                            YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(youtubeId);
                        mYouTubePlayer = youTubePlayer;
                        youTubePlayer
                                .setPlayerStateChangeListener(ChallengeDetailFragment.this);
                        youTubePlayer
                                .setOnFullscreenListener(ChallengeDetailFragment.this);
                    }

                    @Override
                    public void onInitializationFailure(
                            YouTubePlayer.Provider provider,
                            YouTubeInitializationResult result) {
                        showErrorToast(result.toString());
                    }
                });
    }

    public boolean popPlayerFromBackStack() {
        if (mIsFullScreen) {
            mYouTubePlayer.setFullscreen(false);
            return false;
        }
        if (getActivity().getSupportFragmentManager().findFragmentByTag(YOUTUBE_FRAGMENT_TAG) != null) {
            getActivity().getSupportFragmentManager().popBackStack();
            return false;
        }
        return true;
    }

    @Override
    public void onAdStarted() {
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        showErrorToast(errorReason.toString());
    }

    private void showErrorToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onLoaded(String arg0) {
    }

    @Override
    public void onLoading() {
    }

    @Override
    public void onVideoEnded() {
        // popPlayerFromBackStack();
    }

    @Override
    public void onVideoStarted() {
    }

    @Override
    public void onFullscreen(boolean fullScreen) {
        mIsFullScreen = fullScreen;
    }

    public class ChallengeDetailPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = { getString(R.string.description), getString(R.string.comments) };

        public ChallengeDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                descriptionFragment = new ChallengeDescriptionFragment();
                return descriptionFragment;
            }

            commentFragment = CommentListFragment.newInstance(challengeId, true);

            return commentFragment;
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
