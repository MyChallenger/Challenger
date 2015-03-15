package com.example.vikramjeet.challengerapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.fragments.CommentListFragment;

public class CommentActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // show activity as PopUp
        showAsPopup(CommentActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Get challenge_id from Intent
        final String challengeId = getIntent().getStringExtra("challenge_id");

        // Get the fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CommentListFragment commentFragment = CommentListFragment.newInstance(challengeId, false);
        ft.replace(R.id.my_placeholder, commentFragment);
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
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

    private void showAsPopup(Activity activity) {
        //To show activity as dialog and dim the background, you need to declare android:theme="@style/PopupTheme" on for the chosen activity on the manifest
        activity.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = 1000;
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        activity.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
