package com.example.vikramjeet.challengerapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vikramjeet.challengerapp.R;

/**
 * Created by Vikramjeet on 3/9/15.
 */
public class ChallengeDescriptionFragment extends Fragment {

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
        return view;
    }
}