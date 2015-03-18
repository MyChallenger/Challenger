package com.example.vikramjeet.challengerapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.configurations.Auth;
import com.example.vikramjeet.challengerapp.models.MediaType;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ImageFragment extends Fragment {

    private static final String TAG = ImageFragment.class.getName();

    public static final String EXTRA_URL = "com.example.vikramjeet.challengerapp.url";

    @InjectView(R.id.ivChallengeDetail)
    ImageView ivChallengeDetail;

    public static ImageFragment newInstance(ParseFile file) {
        ImageFragment fragment = new ImageFragment();
        Bundle arguments = new Bundle();
        if (file != null) {
            arguments.putString(EXTRA_URL, file.getUrl());
        }
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, parent, false);
        ButterKnife.inject(this, view);
        String url = getArguments().getString(EXTRA_URL);
        if (null != url) {
            Picasso.with(getActivity()).
                    load(url).
                    placeholder(R.drawable.photo_placeholder).
                    into(ivChallengeDetail);
        }

        return view;
    }
}
