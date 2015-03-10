package com.example.vikramjeet.challengerapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.fragments.ChallengeDetailFragment;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.parse.GetCallback;
import com.parse.ParseException;

public class CompletedChallengeDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_challenge_detail);
        // Get challege_id from Intent
        final String challengeId = getIntent().getStringExtra("challenge_id");

        // Get challenge from challenge_id
        Challenge.getChallengeByID(challengeId, new GetCallback<Challenge>() {
            public void done(Challenge item, ParseException e) {
                if (e == null) {
                    // item was found
                    Challenge challenge = item;

                    // Todo: Completed media is required. It should never be null. Tell Pritesh about it. Following condition should not even be there
                    if (challenge.getCompletedMedia() != null) {
                        // Get the fragment
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ChallengeDetailFragment challengeDetailFragment = ChallengeDetailFragment.newInstance(challengeId, Challenge.isVideo(challenge.getCompletedMedia().getUrl()));
                        ft.replace(R.id.my_placeholder, challengeDetailFragment);
                        ft.commit();
                    } else {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ChallengeDetailFragment challengeDetailFragment = ChallengeDetailFragment.newInstance(challengeId, false);
                        ft.replace(R.id.my_placeholder, challengeDetailFragment);
                        ft.commit();
                    }
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_completed_challenge_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}