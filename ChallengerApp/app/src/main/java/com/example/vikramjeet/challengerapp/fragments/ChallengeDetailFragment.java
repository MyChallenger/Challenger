package com.example.vikramjeet.challengerapp.fragments;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.activities.PickVideoActivity;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.models.ChallengeStatus;
import com.example.vikramjeet.challengerapp.models.MediaType;
import com.example.vikramjeet.challengerapp.models.User;
import com.makeramen.RoundedTransformationBuilder;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vikramjeet on 3/8/15.
 */
public class ChallengeDetailFragment extends Fragment {

    private Challenge challenge;
    private String challengeId;

    private ViewPager vpPager;
    private PagerSlidingTabStrip tabStrip;
    private ImageView ivUserPhoto;
    private TextView tvUsername;
    private TextView tvLikes;
    private TextView tvViews;
    private TextView btnStatus;
    private TextView tvTitle;

    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    @InjectView(R.id.indicator)
    CirclePageIndicator indicator;

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
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = null;

        view = inflater.inflate(R.layout.fragment_challenge_detail_video, parent, false);
        ButterKnife.inject(this, view);

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

                    final MediaPagerAdapter adapter = new MediaPagerAdapter(getActivity().getSupportFragmentManager(), challenge);
                    viewPager.setAdapter(adapter);
                    indicator.setViewPager(viewPager);
                    // Attach the page change listener inside the activity
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                        // This method will be invoked when a new page becomes selected.
                        @Override
                        public void onPageSelected(int position) {
                            adapter.onPageSelected(position);
                        }

                        // This method will be invoked when the current page is scrolled
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            // Code goes here
                        }

                        // Called when the scroll state changes:
                        // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
                        @Override
                        public void onPageScrollStateChanged(int state) {
                            // Code goes here
                        }
                    });
                }

                // Populate dictionary with challenge detail
                Map<String, String> map = new HashMap<String, String>();
                map.put("description", challenge.getDescription());
                map.put("challenger_name", challenge.getPoster().getName());
                if(challenge.getBacker() != null)
                    map.put("backer_name", challenge.getBacker().getName());
                else
                    map.put("backer_name", "none");
//                map.put("location", challenge.getLocation().toString());
                map.put("category", challenge.getCategory());

                // Populate description view fragment
                descriptionFragment.populateViewsFromDict(map);
            }
        });
    }


        private void configureButton() {
            User currentUser = (User) ParseUser.getCurrentUser();
            User poster = challenge.getPoster();
            User backer = challenge.getBacker();

            // Set proper button text
            ChallengeStatus status = challenge.getStatus();

            switch(status) {
                case OPEN: {
                    if (!currentUser.getObjectId().equals(poster.getObjectId())) {
                        btnStatus.setText("SPONSOR");
                    } else {
                        btnStatus.setText("OPEN");
                        btnStatus.setEnabled(false);
                    }
                    break;
                }
                case BACKED: {
                    if (currentUser.getObjectId().equals(poster.getObjectId())) {
                        btnStatus.setText("COMPLETE");

                    } else {
                        btnStatus.setText("SPONSORED");
                        btnStatus.setEnabled(false);
                    }
                    break;
                }
                case COMPLETED: {
                    if (currentUser.getObjectId().equals(backer.getObjectId())) {
                        btnStatus.setText("VERIFY");
                    } else {
                        btnStatus.setText("COMPLETED");
                        btnStatus.setEnabled(false);
                    }
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

                // Start activity to upload the video
                Intent i = new Intent(getActivity(), PickVideoActivity.class);
                if(challenge.getObjectId() != null) {
                    i.putExtra(PickVideoActivity.EXTRA_CHALLENGE_ID, challenge.getObjectId());
                    i.putExtra(PickVideoActivity.EXTRA_MEDIA_TYPE, MediaType.COMPLETED.ordinal());
                    startActivity(i);
                }
                else {
                    Toast.makeText(getActivity(), "no id"+challengeId+"and "+challenge.getObjectId(), Toast.LENGTH_SHORT).show();
                }
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
