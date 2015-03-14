package com.example.vikramjeet.challengerapp.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.adapters.LeaderboardQueryAdapter;
import com.example.vikramjeet.challengerapp.models.User;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LeaderboardActivity extends ActionBarActivity {

    @InjectView(R.id.lvUsers)
    ListView lvUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        ButterKnife.inject(this);

        LeaderboardQueryAdapter<User> adapter =
                new LeaderboardQueryAdapter<>(this, new LeaderboardQueryAdapter.QueryFactory<User>() {
                    public ParseQuery<User> create() {
                        ParseQuery query = ParseUser.getQuery();
                        query.orderByDescending("leaderBoardRank");
                        return query;
                    }
                });
        lvUsers.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leaderboard, menu);
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
