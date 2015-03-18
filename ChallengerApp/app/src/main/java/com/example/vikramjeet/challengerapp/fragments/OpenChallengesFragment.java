package com.example.vikramjeet.challengerapp.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.vikramjeet.challengerapp.adapters.ChallengeArrayAdapter;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinutha on 3/7/2015.
 */
public class OpenChallengesFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private ArrayList<Challenge> challenges;
    private ChallengeArrayAdapter aOpenChallenges;
    private ListView lvOpenChallenges;

    private int mPage;
    private SwipeRefreshLayout swipeContainer;

    public static OpenChallengesFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        OpenChallengesFragment fragment = new OpenChallengesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        challenges = new ArrayList<>();
        aOpenChallenges = new ChallengeArrayAdapter(getActivity(), challenges);
        //Connect to the client
        //Generate the timeLine
        populateData();
    }

    private void populateData() {
//        //Download json objects
//        //Fill into data Model
//        //Populating dummy data for now
//        Challenge challenge = new Challenge("Jump the hoop", "danger",350,"4 hours left");
//        aChallenges.add(challenge);
//        aChallenges.add( new TChallenge("Clean park", "community",350,"2 days left"));
//        aChallenges.notifyDataSetChanged();
//        Toast.makeText(getActivity(), "Populate data", Toast.LENGTH_SHORT).show();
        Challenge.getOpenChallenges(new FindCallback<Challenge>() {
            @Override
            public void done(List<Challenge> challenges, ParseException e) {
                if (e == null) {
                    aOpenChallenges.clear();
                    // Add new data to tweetAdapter
                    aOpenChallenges.addAll(challenges);
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                } else {
                    Log.d("Completed Challenges", "Error: " + e.getMessage());
                }

            }
        });

    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.open_challenges_fragment, container, false);
        lvOpenChallenges = (ListView) view.findViewById(R.id.lvOpenChallenges);
        lvOpenChallenges.setAdapter(aOpenChallenges);
        //Listener
        lvOpenChallenges.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        // Get SwipeContainer
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerOpenChallenges);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateData();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return view;
    }
}
