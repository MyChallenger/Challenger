package com.example.vikramjeet.challengerapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.adapters.CompletedChallengesAdapter;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vikramjeet on 3/7/15.
 */
public class CompletedChallengesFragment extends Fragment {

    @InjectView(R.id.lvCompletedChallenges)
    ListView lvCompletedChallenges;
    @InjectView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private ArrayList<Challenge> challenges;
    private CompletedChallengesAdapter completedAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create DataSource
        challenges = new ArrayList<Challenge>();
        // Create Adapter
        completedAdapter = new CompletedChallengesAdapter(getActivity(), challenges);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the view
        View view = inflater.inflate(R.layout.fragment_completed_challenges, container, false);
        // Inject Butterknife
        ButterKnife.inject(this, view);

        // Hook adapter with list view
        lvCompletedChallenges.setAdapter(completedAdapter);
        // Get completed Challenges
        fetchCompletedChallenges();

        // Get SwipeContainer
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchCompletedChallenges();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    public void fetchCompletedChallenges() {
//        Challenge.getFinishedChallenges(new FindCallback<Challenge>() {
//            @Override
//            public void done(List<Challenge> challenges, ParseException e) {
//                if (e == null) {
//                    completedAdapter.clear();
//                    // Add new data to tweetAdapter
//                    completedAdapter.addAll(challenges);
//                    // Now we call setRefreshing(false) to signal refresh has finished
//                    swipeContainer.setRefreshing(false);
//                } else {
//                    Log.d("Completed Challenges", "Error: " + e.getMessage());
//                }
//            }
//        });
//
        Challenge.getOpenChallenges(new FindCallback<Challenge>() {
            @Override
            public void done(List<Challenge> challenges, ParseException e) {
                if (e == null) {
                    completedAdapter.clear();
                    // Add new data to tweetAdapter
                    completedAdapter.addAll(challenges);
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                } else {
                    Log.d("Completed Challenges", "Error: " + e.getMessage());
                }
            }
        });
    }
}
