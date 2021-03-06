package com.example.vikramjeet.challengerapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.adapters.LeaderboardQueryAdapter;
import com.example.vikramjeet.challengerapp.models.User;
import com.parse.ParseQuery;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.vikramjeet.challengerapp.R.color.primary_dark;

public class LeaderboardActivity extends ActionBarActivity {

    @InjectView(R.id.lvUsers)
    ListView lvUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        //CHANGE STATUS BAR COLOR

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(primary_dark));
        ButterKnife.inject(this);

        LeaderboardQueryAdapter<User> adapter =
                new LeaderboardQueryAdapter<>(this, new LeaderboardQueryAdapter.QueryFactory<User>() {
                    public ParseQuery<User> create() {
                        ParseQuery query = User.getQuery();
                        // Order by leaderBoardRank
                        query.orderByAscending("leaderBoardRank");
                        // Then by name
                        query.addAscendingOrder("name");
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
