package com.example.vikramjeet.challengerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.fragments.CompletedChallengesFragment;
import com.example.vikramjeet.challengerapp.models.User;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.vikramjeet.challengerapp.R.color.primary_dark;


public class ChallengeActivity extends ActionBarActivity {

    @InjectView(R.id.viewpager)
    ViewPager vpPager;
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        //CHANGE STATUS BAR COLOR

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(primary_dark));
        // Inject Butterknife
        ButterKnife.inject(this);
        // Set view page adapter for the pager
        vpPager.setAdapter(new ChallengePagerAdapter(getSupportFragmentManager()));
        // Attach tabstrip to the viewpager
        tabStrip.setViewPager(vpPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    protected void onStart() {
        super.onStart();
        // Set up the profile page based on the current user.
        User user = (User) ParseUser.getCurrentUser();
        showProfile(user);
    }

    /**
     * Shows the profile of the given user.
     *
     * @param user
     */
    private void showProfile(User user) {
        if (user != null) {
//            tvUserName.setText(user.getEmail());
            String fullName = user.getString("name");
            ParseFacebookUtils.isLinked(user);
            if (fullName != null) {
//                tvUserName.setText(fullName);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 200) {
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        }
    }

    public class ChallengePagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = {getString(R.string.open_challenges), getString(R.string.completed_challenges)};

        public ChallengePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
//                return new OpenChallengesFragment();
            }
            return new CompletedChallengesFragment();
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

}
