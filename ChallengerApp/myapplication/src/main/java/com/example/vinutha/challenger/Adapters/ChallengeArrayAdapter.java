package com.example.vinutha.challenger.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vinutha.challenger.Model.TChallenge;
import com.example.vinutha.challenger.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by devadutta on 3/7/2015.
 */
public class ChallengeArrayAdapter extends ArrayAdapter<TChallenge> {
    @InjectView(R.id.tvTitle) TextView tvTitle;
    @InjectView(R.id.tvCategory) TextView tvCategory;
    @InjectView(R.id.tvGoal) TextView tvGoal;
    @InjectView(R.id.tvExpiry) TextView tvExpiry;
    public ChallengeArrayAdapter(Context context, List<TChallenge> challengeList) {
        super(context, android.R.layout.simple_list_item_1, challengeList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. Get the tweet
        final TChallenge challenge = getItem(position);
        //2. Find or inflate the template
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_challenge, parent, false);
        }
        ButterKnife.inject(this, convertView);
        tvCategory.setText(challenge.getCategory());
        tvTitle.setText(challenge.getTitle());
        tvGoal.setText("Goal:"+ " $" + challenge.getGoalAmount());
        tvExpiry.setText(challenge.getExpiryDate());
        return convertView;
    }
}
