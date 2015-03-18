package com.example.vikramjeet.challengerapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vikramjeet.challengerapp.R;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vikramjeet on 3/9/15.
 */
public class ChallengeDescriptionFragment extends Fragment {

    @InjectView(R.id.tvChallengeDescription)
    TextView tvDescription;
    @InjectView(R.id.tvChallengerName)
    TextView tvChallengerName;
    @InjectView(R.id.tvLocation)
    TextView tvLocation;
    @InjectView(R.id.tvBackerName)
    TextView tvBackerName;
    @InjectView(R.id.tvChallengeCategory)
    TextView tvCategory;

    public static ChallengeDescriptionFragment newInstance() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_description, parent, false);
        ButterKnife.inject(this, view);
        return view;
    }

    public void populateViewsFromDict(Map<String, String> dict) {
        if (dict != null) {
            tvDescription.setText(dict.get("description"));
            tvChallengerName.setText(dict.get("challenger_name"));
            tvBackerName.setText(dict.get("backer_name"));
            tvCategory.setText(dict.get("category"));
        }
    }
}
