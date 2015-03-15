package com.example.vikramjeet.challengerapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.adapters.CommentListAdapter;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.models.Comment;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vikramjeet on 3/9/15.
 */
public class CommentListFragment extends Fragment {

    @InjectView(R.id.commentSwipeContainer)
    SwipeRefreshLayout swipeContainer;
    @InjectView(R.id.lvComments)
    ListView lvComments;

    private String challengeId;
    private ArrayList<Comment> comments;
    private CommentListAdapter commentAdapter;
    private Boolean isComposeView;

    public static CommentListFragment newInstance(String challengeID, Boolean showComposeView) {
        // Create fragment
        CommentListFragment fragment = new CommentListFragment();
        // Create Bundle
        Bundle args = new Bundle();
        // Populate bundle
        args.putString("challenge_id", challengeID);
        args.putBoolean("compose_view", showComposeView);
        // Set arguments to fragment
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get arguments and populate fragmentType
        challengeId = getArguments().getString("challenge_id");
        isComposeView = getArguments().getBoolean("compose_view");
        // Get data source
        comments = new ArrayList<Comment>();
        // Create adapter
        commentAdapter = new CommentListAdapter(getActivity(), comments);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_list, parent, false);
        ButterKnife.inject(this, view);

        // Set adapter
        lvComments.setAdapter(commentAdapter);

        // Fetch comments
        fetchComments();

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchComments();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    private void fetchComments() {
        // Get challenge from challenge_id
        Challenge.getChallengeByID(challengeId, new GetCallback<Challenge>() {
            public void done(Challenge item, ParseException e) {
                if (e == null) {
                    // item was found
                    Challenge challenge = item;
                    // Clear previous entries from the adapter
                    commentAdapter.clear();
                    // Set comments in adapter
                    ArrayList<Comment> commentList = challenge.getComments();
                    if (commentList != null) {
                        commentAdapter.addAll(challenge.getComments());
                    }
                    // Stop refreshing
                    swipeContainer.setRefreshing(false);
                }
            }
        });
    }

}
