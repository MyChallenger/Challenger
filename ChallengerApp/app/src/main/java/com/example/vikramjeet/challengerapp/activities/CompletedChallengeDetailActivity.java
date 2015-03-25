package com.example.vikramjeet.challengerapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.fragments.ChallengeDetailFragment;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.parse.GetCallback;
import com.parse.ParseException;

public class CompletedChallengeDetailActivity extends ActionBarActivity {

    public static final String EXTRA_CHALLENGE_ID = "challenge_id";
    public static final String EXTRA_OPEN_CHALLENGE_ID = "challenge_open_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_challenge_detail);
        // Get challege_id from Intent
        final String challengeId = getIntent().getStringExtra(EXTRA_CHALLENGE_ID);
        if (challengeId != null) {
            //Action Bar
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Completed Challenge");
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            // final String challengeId = getIntent().getStringExtra(EXTRA_CHALLENGE_ID);

            // Get challenge from challenge_id
            Challenge.getChallengeByID(challengeId, new GetCallback<Challenge>() {
                public void done(Challenge challenge, ParseException e) {
                    if (e == null) {
                        // item was found

                        // Get the fragment
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ChallengeDetailFragment challengeDetailFragment = ChallengeDetailFragment.newInstance(challengeId, challenge.isCompletedMediaVideo());
                        ft.replace(R.id.my_placeholder, challengeDetailFragment);
                        ft.commit();
                    }
                }
            });
        } else {
            final String open_challengeId = getIntent().getStringExtra(EXTRA_OPEN_CHALLENGE_ID);
            if (open_challengeId != null) {
                //Action Bar
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setTitle("Open Challenge");
                actionBar.setDisplayUseLogoEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);

                // final String challengeId = getIntent().getStringExtra(EXTRA_CHALLENGE_ID);

                // Get challenge from challenge_id
                Challenge.getChallengeByID(open_challengeId, new GetCallback<Challenge>() {
                    public void done(Challenge challenge, ParseException e) {
                        if (e == null) {
                            // item was found

                            // Get the fragment
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ChallengeDetailFragment challengeDetailFragment = ChallengeDetailFragment.newInstance(open_challengeId, challenge.isCompletedMediaVideo());
                            ft.replace(R.id.my_placeholder, challengeDetailFragment);
                            ft.commit();
                        }
                    }
                });
            }
        }


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

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
