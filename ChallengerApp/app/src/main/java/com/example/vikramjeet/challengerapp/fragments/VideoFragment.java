package com.example.vikramjeet.challengerapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.configurations.Auth;
import com.example.vikramjeet.challengerapp.models.MediaType;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import butterknife.ButterKnife;

public class VideoFragment extends Fragment implements YouTubePlayer.PlayerStateChangeListener, YouTubePlayer.OnFullscreenListener {

    private static final String TAG = VideoFragment.class.getName();

    public static final String EXTRA_MEDIA_ID = "com.example.vikramjeet.challengerapp.media_id";

    private static final String YOUTUBE_FRAGMENT_TAG = "youtube";
    private YouTubePlayer mYouTubePlayer;
    private boolean mIsFullScreen = false;

    public void panToVideo(final String youtubeId) {
        popPlayerFromBackStack();
        YouTubePlayerSupportFragment playerFragment = YouTubePlayerSupportFragment
                .newInstance();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flChallengeDetail, playerFragment,
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
                                .setPlayerStateChangeListener(VideoFragment.this);
                        youTubePlayer
                                .setOnFullscreenListener(VideoFragment.this);
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
        if (getActivity().getSupportFragmentManager().findFragmentByTag(YOUTUBE_FRAGMENT_TAG) != null) {
            getActivity().getSupportFragmentManager().popBackStack();
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
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
//                .show();
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

    public static VideoFragment newInstance(String mediaId, MediaType mediaType) {
        VideoFragment fragment = new VideoFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_MEDIA_ID, mediaId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, parent, false);
        ButterKnife.inject(this, view);
        String mediaId = getArguments().getString(EXTRA_MEDIA_ID);
        if (null != mediaId) {
            panToVideo(mediaId);
        }

        return view;
    }
}
