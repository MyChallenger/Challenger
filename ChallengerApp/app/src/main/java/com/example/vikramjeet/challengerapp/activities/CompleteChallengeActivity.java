package com.example.vikramjeet.challengerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vikramjeet.challengerapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CompleteChallengeActivity extends ActionBarActivity {

    public static final String EXTRA_CHALLENGE_ID = "challenge_id";

    @InjectView(R.id.tvChallengeId)
    TextView tvChallengeId;

    String challengeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_challenge);
        ButterKnife.inject(this);

        challengeId = getIntent().getStringExtra(EXTRA_CHALLENGE_ID);
        tvChallengeId.setText(challengeId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complete, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 200) {
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        }
    }
}
