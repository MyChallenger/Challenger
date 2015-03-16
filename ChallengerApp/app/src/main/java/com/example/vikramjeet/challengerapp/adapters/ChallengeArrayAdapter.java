package com.example.vikramjeet.challengerapp.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by vinutha on 3/7/2015.
 */
public class ChallengeArrayAdapter extends ArrayAdapter<Challenge> {
    @InjectView(R.id.tvTitle) TextView tvTitle;
    @InjectView(R.id.tvCategory) TextView tvCategory;
    @InjectView(R.id.tvGoal) TextView tvGoal;
    @InjectView(R.id.tvExpiry) TextView tvExpiry;
    @InjectView(R.id.ivProfile) ImageView ivProfile;
    public ChallengeArrayAdapter(Context context, List<Challenge> challengeList) {
        super(context, android.R.layout.simple_list_item_1, challengeList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. Get the tweet
        final Challenge challenge = getItem(position);
        //2. Find or inflate the template
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_challenge, parent, false);
        }
        ButterKnife.inject(this, convertView);
        tvCategory.setText(challenge.getCategory());
        tvTitle.setText(challenge.getTitle());
        tvGoal.setText("Goal:"+ " $" + challenge.getPrize());
        if(challenge.getCreatedMedia() != null)
            Picasso.with(getContext()).load(challenge.getCreatedMedia().getUrl()).into(ivProfile);
        // FIXME: Format this date!
        tvExpiry.setText("expires " +getRelativeTimeAgo(challenge.getExpiryDate().toString()));
        //tvExpiry.setText("2 days left");
        return convertView;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
