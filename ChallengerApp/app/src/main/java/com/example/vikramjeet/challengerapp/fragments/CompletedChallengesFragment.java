package com.example.vikramjeet.challengerapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.activities.CompletedChallengeDetailActivity;
import com.example.vikramjeet.challengerapp.adapters.CompletedChallengesAdapter;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by vikramjeet on 3/7/2015.
 */
public class CompletedChallengesFragment extends Fragment{
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
        completedAdapter = new CompletedChallengesAdapter(getActivity(), challenges, getActivity().getSupportFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Inflate the view
        View view = inflater.inflate(R.layout.fragment_completed_challenges, parent, false);        // Inject Butterknife
        ButterKnife.inject(this, view);

        // Hook adapter with list view
        lvCompletedChallenges.setAdapter(completedAdapter);
        // Add OnItemClickListener to Listview
        lvCompletedChallenges.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create an intent
                Intent challengeDetailIntent = new Intent(getActivity(), CompletedChallengeDetailActivity.class);
                // Get the challenge
                Challenge challenge = challenges.get(position);
                // Pass challenge into the intent
                challengeDetailIntent.putExtra("challenge_id", challenge.getObjectId());
                Log.d("ObjectID:", challenge.getObjectId());
                // Start activity
                startActivity(challengeDetailIntent);
            }
        });

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
        Challenge.getFinishedChallenges(new FindCallback<Challenge>() {
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


//    protectedvoid onActivityResult(int requestCode, int resultCode, Intent data) {
//        cha.onActivityResult(requestCode, resultCode, data);
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        completedAdapter.onActivityResult(requestCode, resultCode, data);

    }
}
