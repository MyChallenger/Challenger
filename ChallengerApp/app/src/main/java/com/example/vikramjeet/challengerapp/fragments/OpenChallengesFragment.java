package com.example.vikramjeet.challengerapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.activities.CompletedChallengeDetailActivity;
import com.example.vikramjeet.challengerapp.activities.NewChallengeActivity;
import com.example.vikramjeet.challengerapp.adapters.ChallengeArrayAdapter;
import com.example.vikramjeet.challengerapp.adapters.SimpleRecycleViewAdapter;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by vinutha on 3/7/2015.
 */
public class OpenChallengesFragment extends Fragment implements ChallengeArrayAdapter.ChallengeArrayAdapterListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    private ArrayList<Challenge> mchallenges;
    private ChallengeArrayAdapter aOpenChallenges;
    private ListView lvOpenChallenges;
    private  RecyclerView rvOpenChallenges;
    public static final String EXTRA_OPEN_CHALLENGE_ID = "challenge_open_id";
    private SimpleRecycleViewAdapter adapter;
    private static final int SCAN_REQUEST_CODE = 100;

    private int mPage;
    private SwipeRefreshLayout swipeContainer;
    private Challenge currentlySponsoredChallenge;

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
        mchallenges = new ArrayList<>();
       // aOpenChallenges = new ChallengeArrayAdapter(getActivity(), challenges, this);*/

        //Connect to the client
        //Generate the timeLine
       // setContentView(R.layout.activity_main);


        populateData();
    }

    private void populateData() {
//        //Download json objects
//        //Fill into data Model

        Challenge.getOpenChallenges(new FindCallback<Challenge>() {
            @Override
            public void done(List<Challenge> local_challenges, ParseException e) {
                if (e == null) {
/*                    aOpenChallenges.clear();
                    // Add new data to tweetAdapter
                    aOpenChallenges.addAll(challenges);

                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);*/
                    //for recycle view
                    mchallenges.addAll(local_challenges);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("Completed Challenges", "Error: " + e.getMessage());
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        populateData();
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_challenges, container, false);
       // lvOpenChallenges = (ListView) view.findViewById(R.id.lvOpenChallenges);
          rvOpenChallenges = (RecyclerView) view.findViewById((R.id.rvOpenChallenges));
        // Setup layout manager for items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // Control orientation of the items
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        // Attach layout manager
        rvOpenChallenges.setLayoutManager(layoutManager);
        //lvOpenChallenges.setAdapter(aOpenChallenges);
        adapter = new SimpleRecycleViewAdapter(getActivity(),mchallenges,this);
        rvOpenChallenges.setAdapter(adapter);
        // Adding floating button to listview
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //fab.attachToListView(lvOpenChallenges);
        fab.attachToRecyclerView(rvOpenChallenges);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewChallengeActivity.class);
                startActivity(i);
            }
        });

        //Listener
       /* lvOpenChallenges.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create an intent
                Intent challengeDetailIntent = new Intent(getActivity(), CompletedChallengeDetailActivity.class);
                // Get the challenge
                Challenge challenge = challenges.get(position);
                // Pass challenge into the intent

                challengeDetailIntent.putExtra(EXTRA_OPEN_CHALLENGE_ID, challenge.getObjectId());
               // Log.d("ObjectID:", challenge.getObjectId());
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
                android.R.color.holo_red_light);*/
        return view;
    }

    @Override
    public void onChallengeSponsor(Challenge challenge) {
        currentlySponsoredChallenge = challenge;
        Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, SCAN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_REQUEST_CODE) {

            String redactedNumber;
            String expiryMonth;
            String expiryYear;
            String cvv;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                redactedNumber = scanResult.getRedactedCardNumber();
                expiryMonth = String.valueOf(scanResult.expiryMonth);
                expiryYear = String.valueOf(scanResult.expiryYear);
                cvv = scanResult.cvv;
            } else {
                redactedNumber = "XXXX-XXXX-XXXX-3258";
                expiryMonth = "5";
                expiryYear = "15";
                cvv = "123";
            }
            String confirmation = "You will be charged: $" + currentlySponsoredChallenge.getPrize();
            confirmation += "\nCredit Card Number: " + redactedNumber;
            confirmation += "\nExpiration: " + expiryMonth + "/" + expiryYear;
            confirmation += "\nCVV: " + cvv;

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(confirmation)
                    .setTitle(R.string.back_confirmation);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    currentlySponsoredChallenge.back(new GetCallback<Challenge>() {
                        @Override
                        public void done(Challenge challenge, com.parse.ParseException e) {
                            Toast.makeText(getActivity(), "Challenge backed successfully!", Toast.LENGTH_SHORT).show();
                            populateData();
                        }

                    });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), "Did not back challenge! :(", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog dialog = builder.create();

            dialog.show();
        }
    }
}
