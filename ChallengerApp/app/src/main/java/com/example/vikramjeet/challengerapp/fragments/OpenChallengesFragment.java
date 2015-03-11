package com.example.vikramjeet.challengerapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.adapters.ChallengeArrayAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devadutta on 3/7/2015.
 */
public class OpenChallengesFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private ArrayList<Challenge> challenges;
    private ChallengeArrayAdapter aOpenChallenges;
    private ListView lvOpenChallenges;

    private int mPage;

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
                    //  swipeContainer.setRefreshing(false);
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
        return view;
    }
}
