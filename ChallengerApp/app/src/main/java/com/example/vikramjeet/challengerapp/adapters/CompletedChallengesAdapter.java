package com.example.vikramjeet.challengerapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.activities.CommentActivity;
import com.example.vikramjeet.challengerapp.configurations.Auth;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.models.callbacks.LikeStatusCallback;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.makeramen.RoundedTransformationBuilder;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CompletedChallengesAdapter extends ArrayAdapter<Challenge> implements YouTubePlayer.PlayerStateChangeListener, YouTubePlayer.OnFullscreenListener {

    private FragmentManager supportFragmentManager;
    private static final String YOUTUBE_FRAGMENT_TAG = "youtube";
    private YouTubePlayer mYouTubePlayer;
    private boolean mIsFullScreen = false;

    public void panToVideo(final String youtubeId) {
        popPlayerFromBackStack();
        YouTubePlayerSupportFragment playerFragment = YouTubePlayerSupportFragment
                .newInstance();
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.flCompletedChallenge, playerFragment,
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
                                .setPlayerStateChangeListener(CompletedChallengesAdapter.this);
                        youTubePlayer
                                .setOnFullscreenListener(CompletedChallengesAdapter.this);
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
        if (supportFragmentManager.findFragmentByTag(YOUTUBE_FRAGMENT_TAG) != null) {
            supportFragmentManager.popBackStack();
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT)
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

    public enum ViewValues {
        VIDEO, IMAGE
    }

    // View lookup cache
    public static class VideoViewHolder {
        @InjectView(R.id.ivCompletedUserPhoto)
        ImageView ivUserPhoto;
        @InjectView(R.id.tvCompletedUserName)
        TextView tvUserName;
        @InjectView(R.id.flCompletedChallenge)
        FrameLayout flCompletedChallenge;
        @InjectView(R.id.tvlikeCount)
        TextView tvLikes;
        @InjectView(R.id.tvComment)
        TextView tvComment;
        @InjectView(R.id.tvViews)
        TextView tvViews;
        @InjectView(R.id.progress)
        ProgressBar spinnerView;
        @InjectView(R.id.tvCompletedChallengeTitle)
        TextView tvTitle;

        public VideoViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public static class ImageViewHolder {
        @InjectView(R.id.ivCompletedUserPhoto_2)
        ImageView ivUserPhoto;
        @InjectView(R.id.tvCompletedUserName_2)
        TextView tvUserName;
        @InjectView(R.id.ivCompletedChallenge)
        ImageView ivCompletedImage;
        @InjectView(R.id.tvlikeCount_2)
        TextView tvLikes;
        @InjectView(R.id.tvComment_2)
        TextView tvComment;
        @InjectView(R.id.tvCategory_2)
        TextView tvCategory;
        @InjectView(R.id.tvCompletedChallengeTitle_2)
        TextView tvTitle;


        public ImageViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public CompletedChallengesAdapter(Context context, List<Challenge> challenges, FragmentManager supportFragmentManager) {
        super(context, android.R.layout.simple_list_item_1, challenges);
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = this.getItemViewType(position);
        // Get challenge
        final Challenge challenge = getItem(position);

        // Rounded image transformation
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .cornerRadiusDp(25)
                .oval(false)
                .build();

        // Choose proper view
        switch (viewType) {
            case 0:
                // View look up cache stored in tag
                VideoViewHolder viewHolder1 = null;
                if (convertView == null) {
                    // Inflate convertView
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_completed_challenge_video, parent, false);
                    // Create View cache
                    viewHolder1 = new VideoViewHolder(convertView);
                    convertView.setTag(viewHolder1);
                } else {
                    viewHolder1 = (VideoViewHolder) convertView.getTag();
                }

                // clear previous photo
                viewHolder1.ivUserPhoto.setImageResource(0);
                // Show user info
                viewHolder1.tvUserName.setText(challenge.getBacker().getName());

                Picasso.with(getContext())
                        .load(challenge.getBacker().getPhotoURL())
                        .placeholder(R.drawable.photo_placeholder)
                        .fit()
                        .transform(transformation)
                        .into(viewHolder1.ivUserPhoto);

                viewHolder1.tvTitle.setText(challenge.getTitle());
                viewHolder1.tvComment.setText(String.valueOf(challenge.getNumberOfComments()));
                viewHolder1.tvLikes.setText(String.valueOf(challenge.getNumberOfLikes()));
                viewHolder1.tvViews.setText(String.valueOf(challenge.getNumberOfViews()));

                // Get ViewHolder to call inside callback method
                final VideoViewHolder tempHolder = viewHolder1;

                // Add Click listener for Like button
                viewHolder1.tvLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        challenge.isLiked(new LikeStatusCallback<Boolean>() {
                            @Override
                            public void done(Boolean isLiked, ParseException e) {
                                if (isLiked) {
                                    challenge.unLike(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            tempHolder.tvLikes.setText(String.valueOf(challenge.getNumberOfLikes()));
                                        }
                                    });
                                } else {
                                    challenge.like(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            tempHolder.tvLikes.setText(String.valueOf(challenge.getNumberOfLikes()));
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

                // Add Click listener for Comment Button
                viewHolder1.tvComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Ask for comment screen here
                        // Ask for comment screen here
                        Intent i = new Intent(getContext(), CommentActivity.class);
                        i.putExtra("challenge_id", challenge.getObjectId());
                        getContext().startActivity(i);
                    }
                });

//                viewHolder1.flCompletedChallenge.removeAllViews();
                if (challenge.getCompletedMediaId() != null) {
                    panToVideo(challenge.getCompletedMediaId());
                }

                return convertView;
            case 1:
                // View look up cache stored in tag
                ImageViewHolder viewHolder2 = null;
                if (convertView == null) {
                    // Inflate convertView
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_completed_challenge_image, parent, false);
                    // Create View cache
                    viewHolder2 = new ImageViewHolder(convertView);
                    convertView.setTag(viewHolder2);
                } else {
                    viewHolder2 = (ImageViewHolder) convertView.getTag();
                }

                // clear previous photo
                viewHolder2.ivUserPhoto.setImageResource(0);
                // show user info
                viewHolder2.tvUserName.setText(challenge.getBacker().getName());

                Picasso.with(getContext())
                        .load(challenge.getBacker().getPhotoURL())
                        .transform(transformation)
                        .fit()
                        .placeholder(R.drawable.photo_placeholder)
                        .into(viewHolder2.ivUserPhoto);

                viewHolder2.tvTitle.setText(challenge.getTitle());
                viewHolder2.tvComment.setText(String.valueOf(challenge.getNumberOfComments()));
                viewHolder2.tvLikes.setText(String.valueOf(challenge.getNumberOfLikes()));
                viewHolder2.tvCategory.setText(challenge.getCategory());

                // Get ViewHolder to call inside callback method
                final ImageViewHolder tempHolder2 = viewHolder2;

                // Add Click listener for Like button
                viewHolder2.tvLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        challenge.isLiked(new LikeStatusCallback<Boolean>() {
                            @Override
                            public void done(Boolean isLiked, ParseException e) {
                                if (isLiked) {
                                    challenge.unLike(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            tempHolder2.tvLikes.setText(String.valueOf(challenge.getNumberOfLikes()));
                                        }
                                    });
                                } else {
                                    challenge.like(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            tempHolder2.tvLikes.setText(String.valueOf(challenge.getNumberOfLikes()));
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

                // Add Click listener for Comment Button
                viewHolder2.tvComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Ask for comment screen here
                        Intent i = new Intent(getContext(), CommentActivity.class);
                        i.putExtra("challenge_id", challenge.getObjectId());
                        ((Activity) getContext()).startActivityForResult(i, 200);
                    }
                });

                // Clear previous data
                viewHolder2.ivCompletedImage.setImageResource(0);

                if (challenge.getCompletedMedia() != null) {
                    Picasso.with(getContext()).
                            load(challenge.getCompletedMedia().getUrl()).
                            placeholder(R.drawable.photo_placeholder).
                            into(viewHolder2.ivCompletedImage);
                } else {
                    viewHolder2.ivCompletedImage.setImageResource(R.drawable.photo_placeholder);
                }

                return convertView;
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Challenge challenge = getItem(position);
        int viewType = ViewValues.IMAGE.ordinal();
        if (challenge.isVideo()) {
            viewType = ViewValues.VIDEO.ordinal();
        }
        return viewType;
    }

    // Total number of types is the number of enum values
    @Override
    public int getViewTypeCount() {
        return ViewValues.values().length;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("HERE:", "I am here");
    }
}
