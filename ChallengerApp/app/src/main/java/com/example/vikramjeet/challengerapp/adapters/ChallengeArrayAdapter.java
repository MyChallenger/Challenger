package com.example.vikramjeet.challengerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by devadutta on 3/7/2015.
 */
public class ChallengeArrayAdapter extends ArrayAdapter<Challenge> {
    @InjectView(R.id.tvTitle) TextView tvTitle;
    @InjectView(R.id.tvCategory) TextView tvCategory;
    @InjectView(R.id.tvGoal) TextView tvGoal;
    @InjectView(R.id.tvExpiry) TextView tvExpiry;
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
        // FIXME: Format this date!
        tvExpiry.setText(challenge.getExpiryDate().toString());
        return convertView;
    }
}
