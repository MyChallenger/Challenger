package com.example.vikramjeet.challengerapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.models.callbacks.LikeStatusCallback;
import com.makeramen.RoundedTransformationBuilder;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CompletedChallengesAdapter extends ArrayAdapter<Challenge>{

    public enum ViewValues {
        VIDEO, IMAGE
    }

    // View lookup cache
    public static class VideoViewHolder {
        @InjectView(R.id.ivCompletedUserPhoto)
        ImageView ivUserPhoto;
        @InjectView(R.id.tvCompletedUserName)
        TextView tvUserName;
        @InjectView(R.id.vvCompletedChallenge)
        VideoView vvCompletedVideo;
        @InjectView(R.id.tvlikeCount)
        TextView tvLikes;
        @InjectView(R.id.tvComment)
        TextView tvComment;
        @InjectView(R.id.tvViews)
        TextView tvViews;
        @InjectView(R.id.progress)
        ProgressBar spinnerView;

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

        public ImageViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public CompletedChallengesAdapter(Context context, List<Challenge> challenges) {
        super(context, android.R.layout.simple_list_item_1, challenges);
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

                viewHolder1.tvComment.setText(String.valueOf(challenge.getNumberOfComments()));
                viewHolder1.tvLikes.setText(String.valueOf(challenge.getNumberOfLikes()));
                viewHolder1.tvViews.setText(String.valueOf(challenge.getNumberOfViews()));

                // Get ViewHolder to call inside callback method
                final VideoViewHolder tempHolder = viewHolder1;

                // Add Click listener for Like button
                viewHolder1.tvLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        challenge.like(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                // Increment Like Count so we don't have to call the server again
                                int newLikeCount = challenge.getNumberOfLikes() + 1;
                                tempHolder.tvLikes.setText(String.valueOf(newLikeCount));
                            }
                        });
                    }
                });

                // Add Click listener for Comment Button
                viewHolder1.tvComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Ask for comment screen here
                    }
                });

                if (challenge.getCompletedMedia() != null) {
                    if (viewHolder1.vvCompletedVideo.isPlaying()) {
                        viewHolder1.vvCompletedVideo.stopPlayback();
                    }

//                    final ProgressDialog progress = ProgressDialog.show(getContext(), null, "Loading...", true);

                    viewHolder1.vvCompletedVideo.setVideoPath(challenge.getCompletedMedia().getUrl());
                    MediaController mediaController = new MediaController(getContext());
                    mediaController.setAnchorView(viewHolder1.vvCompletedVideo);
                    viewHolder1.vvCompletedVideo.setMediaController(mediaController);
                    viewHolder1.vvCompletedVideo.requestFocus();

                    final VideoViewHolder finalViewHolder = viewHolder1;
                    viewHolder1.vvCompletedVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        // Close the progress bar and play the video
                        public void onPrepared(MediaPlayer mp) {
//                            mp.setLooping(true);
                            finalViewHolder.vvCompletedVideo.start();
                            // Dismiss spinner
                            finalViewHolder.spinnerView.setVisibility(View.GONE);
                        }
                    });
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
                    }
                });

                if (challenge.getCompletedMedia() != null) {
                    Picasso.with(getContext()).
                            load(challenge.getCompletedMedia().getUrl()).
                            placeholder(R.drawable.photo_placeholder).
                            into(viewHolder2.ivCompletedImage);
                }

                return convertView;
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Challenge challenge = getItem(position);
        if (challenge.getCompletedMedia() != null) {
            boolean isVideo = Challenge.isVideo(challenge.getCompletedMedia().getUrl());
            if (isVideo) {
                return ViewValues.VIDEO.ordinal();
            } else {
                return ViewValues.IMAGE.ordinal();
            }
        }

        return ViewValues.IMAGE.ordinal();
    }

    // Total number of types is the number of enum values
    @Override
    public int getViewTypeCount() {
        return ViewValues.values().length;
    }

}
