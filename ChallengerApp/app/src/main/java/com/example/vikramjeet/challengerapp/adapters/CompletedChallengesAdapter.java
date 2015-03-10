package com.example.vikramjeet.challengerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CompletedChallengesAdapter extends ArrayAdapter<Challenge>{
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get challenge
        Challenge challenge = getItem(position);

        if (isVideo(challenge.getCompletedMedia().getUrl())) {       // Media is Video
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

            Picasso.with(getContext()).
                    load(challenge.getCompletedMedia().getUrl()).
                    placeholder(R.drawable.photo_placeholder).
                    into(viewHolder.ivCompletedImage);

        }

        return convertView;

    }

    private boolean isVideo(String mediaURL) {
        String extension = "";

        int index = mediaURL.lastIndexOf('.');
        if (index >= 0) {
            extension = mediaURL.substring(index + 1);
        }

        // Check if extension is an image extension
        if (extension.equals("jpg")
                || extension.equals("jpeg")
                || extension.equals("png")
                || extension.equals("gif")
                || extension.equals("tiff")
                || extension.equals("tif")) {

            return false;
        }
        return true;
    }
}
