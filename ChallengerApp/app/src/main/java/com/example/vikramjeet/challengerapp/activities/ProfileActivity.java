package com.example.vikramjeet.challengerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.models.User;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.vikramjeet.challengerapp.R.color.primary_dark;


public class ProfileActivity extends ActionBarActivity {
    @InjectView(R.id.ivBG)
    ImageView ivBG;
    @InjectView(R.id.ivProfileFG)
    ImageView ivProfile;
    @InjectView(R.id.tvName)
    TextView tvName;
    @InjectView(R.id.tvLocation)
    TextView tvLocation;
    @InjectView(R.id.tvPoints)
    TextView tvPoint;
    @InjectView(R.id.tvRank)
    Button btnRank;
    @InjectView(R.id.tvChallengesCount)
    TextView tvChallengesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //CHANGE STATUS BAR COLOR

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(primary_dark));
        ButterKnife.inject(this);
        //Action Bar

        getUserData();

    }

    private void getUserData() {
        User user = (User) ParseUser.getCurrentUser();
        if (user == null) {
            Log.e("wtf", "wtf");
            return;
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Welcome " + user.getName());
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        ivProfile.setEnabled(false);
        Picasso.with(this).load(user.getPhotoURL()).into(ivBG);
        tvName.setText(user.getName());

        tvLocation.setText(user.getLocation());
        // tvPoint.setText("100");
        tvPoint.setText("Points Earned: " + user.getPointsEarned());
        btnRank.setText(" Your Rank: " + user.getLeaderBoardRank()+ " ");
        tvChallengesCount.setText("Challenges Accepted: " + user.getChallengesBacked() + user.getChallengesCompleted());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
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

    public void onRankBoard(View view) {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);


    }

}
