package com.example.vinutha.challenger.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vinutha.challenger.Adapters.ChallengeArrayAdapter;
import com.example.vinutha.challenger.Model.TChallenge;
import com.example.vinutha.challenger.R;

import java.util.ArrayList;

/**
 * Created by devadutta on 3/7/2015.
 */
public class OpenChallengesFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private ArrayList<TChallenge> challenges;
    private ChallengeArrayAdapter aChallenges;
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
        aChallenges = new ChallengeArrayAdapter(getActivity(), challenges);
        //Connect to the client
        //Generate the timeLine
        populateData();
    }

    private void populateData() {
        //Download json objects
        //Fill into data Model
        //Populating dummy data for now
        TChallenge challenge = new TChallenge("Jump the hoop", "danger",350,"4 hours left");
        aChallenges.add(challenge);
        aChallenges.add( new TChallenge("Clean park", "community",350,"2 days left"));
        aChallenges.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Populate data", Toast.LENGTH_SHORT).show();
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.open_challenges_fragment, container, false);
        lvOpenChallenges = (ListView) view.findViewById(R.id.lvOpenChallenges);
        lvOpenChallenges.setAdapter(aChallenges);
        return view;
    }
}
