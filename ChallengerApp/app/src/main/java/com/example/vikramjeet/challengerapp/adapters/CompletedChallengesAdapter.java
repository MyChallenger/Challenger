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
import android.widget.TextView;
import android.widget.VideoView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.makeramen.RoundedTransformationBuilder;
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
        @InjectView(R.id.tvViews_2)
        TextView tvViews;

        public ImageViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public CompletedChallengesAdapter(Context context, List<Challenge> challenges) {
        super(context, android.R.layout.simple_list_item_1, challenges);
    }

/*    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get challenge
        Challenge challenge = getItem(position);

        // Todo: Completed media is required. It should never be null. Tell Pritesh about it. Following condition should not even be there
        if (challenge.getCompletedMedia() != null) {


//        challenge.getCompletedMedia().getDataInBackground(new GetDataCallback() {
//            @Override
//            public void done(byte[] bytes, ParseException e) {

                if (Challenge.isVideo(challenge.getCompletedMedia().getUrl())) {       // Media is Video
                    // View look up cache stored in tag
                    VideoViewHolder viewHolder = null;
                    if (convertView == null) {
                        // Inflate convertView
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_completed_challenge_video, parent, false);
                        // Create View cache
                        viewHolder = new VideoViewHolder(convertView);
                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = (VideoViewHolder) convertView.getTag();
                    }

//        viewHolder.tvUserName = challenge.getUserPosted().getName();
                    // Todo: Show userphoto
                    viewHolder.tvComment.setText(String.valueOf(challenge.getNumberOfComments()));
                    viewHolder.tvLikes.setText(String.valueOf(challenge.getNumberOfLikes()));
                    viewHolder.tvViews.setText(String.valueOf(challenge.getNumberOfViews()));
                } else {            // Media is Image
                    // View look up cache stored in tag
                    ImageViewHolder viewHolder = null;
                    if (convertView == null) {
                        // Inflate convertView
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_completed_challenge_image, parent, false);
                        // Create View cache
                        viewHolder = new ImageViewHolder(convertView);
                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = (ImageViewHolder) convertView.getTag();
                    }

//        viewHolder.tvUserName = challenge.getUserPosted().getName();
                    // Todo: Show userphoto
                    viewHolder.tvComment.setText(String.valueOf(challenge.getNumberOfComments()));
                    viewHolder.tvLikes.setText(String.valueOf(challenge.getNumberOfLikes()));
                    viewHolder.tvViews.setText(String.valueOf(challenge.getNumberOfViews()) + " Views");

                    // Todo: Completed media is required. It should never be null. Tell Pritesh about it. Following condition should not even be there
                    if (challenge.getCompletedMedia() != null) {
                        Picasso.with(getContext()).
                                load(challenge.getCompletedMedia().getUrl()).
                                placeholder(R.drawable.photo_placeholder).
                                into(viewHolder.ivCompletedImage);
                    }
                }


//            }
//        });



        }
    else {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_completed_challenge_image, parent, false);
        }


        return convertView;

    }

*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = this.getItemViewType(position);
        // Get challenge
        Challenge challenge = getItem(position);

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
//                            finalViewHolder.vvCompletedVideo.start();
//                            progress.dismiss();
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
                viewHolder2.tvViews.setText(String.valueOf(challenge.getNumberOfViews()) + " Views");

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
