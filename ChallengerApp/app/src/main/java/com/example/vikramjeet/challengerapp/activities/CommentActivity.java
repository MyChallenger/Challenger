package com.example.vikramjeet.challengerapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.fragments.CommentListFragment;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.models.Comment;
import com.example.vikramjeet.challengerapp.models.User;
import com.makeramen.RoundedTransformationBuilder;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CommentActivity extends ActionBarActivity {

    private Challenge challenge;
    private CommentListFragment commentFragment;

    @InjectView(R.id.ivCommentUserPhoto)
    ImageView ivUserPhoto;
    @InjectView(R.id.tvCommentUserName)
    TextView tvUsername;
    @InjectView(R.id.etComment)
    EditText etComment;
    @InjectView(R.id.btnCancel)
    Button btnCancel;
    @InjectView(R.id.btnPost)
    Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // show activity as PopUp
        showAsPopup(CommentActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.inject(this);

        // Get current user
        User user = (User) ParseUser.getCurrentUser();

        // Populate views here
<<<<<<< .merge_file_lZCfIx
        tvUsername.setText(user.getName());
=======
        tvUsername.setText(user.getUsername());
>>>>>>> .merge_file_d0Wnhe
        // Rounded image transformation
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .cornerRadiusDp(25)
                .oval(false)
                .build();

        // clear previous photo
        ivUserPhoto.setImageResource(0);

        Picasso.with(this)
                .load(user.getPhotoURL())
                .placeholder(R.drawable.photo_placeholder)
                .fit()
                .transform(transformation)
                .into(ivUserPhoto);

        // Get challenge_id from Intent
        final String challengeId = getIntent().getStringExtra("challenge_id");

        // Get the fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
<<<<<<< .merge_file_lZCfIx
        commentFragment = CommentListFragment.newInstance(challengeId, false);
=======
        CommentListFragment commentFragment = CommentListFragment.newInstance(challengeId, false);
>>>>>>> .merge_file_d0Wnhe
        ft.replace(R.id.my_placeholder, commentFragment);
        ft.commit();

        // Get Challenge
        Challenge.getChallengeByID(challengeId, new GetCallback<Challenge>() {
            @Override
            public void done(Challenge challng, ParseException e) {
                challenge = challng;
            }
        });

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

    public void onCancel(View v) {

        this.finish();

//        int commentCount = challenge.getComments().size();

//        // Prepare data intent
//        Intent data = new Intent();
//        // Pass relevant data back as a result
//        data.putExtra("number", commentCount);
//        data.putExtra("code", 200); // ints work too
//        // Activity finished ok, return the data
//        setResult(RESULT_OK, data); // set result code and bundle data for response
//        finish(); // closes the activity, pass data to parent

    }

    public void onPost(View v) {
        // Create comment object
        Comment comment = new Comment();
        comment.setText(etComment.getText().toString());

        // Post challenge
        challenge.addComment(comment, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getApplicationContext(), "Posted comment: " + etComment.getText().toString(), Toast.LENGTH_SHORT).show();
                etComment.setText("");
                // Refresh comment list here
<<<<<<< .merge_file_lZCfIx
                commentFragment.refreshComments();
=======
                int commentCount = challenge.getComments().size();
>>>>>>> .merge_file_d0Wnhe


                // Prepare data intent
                Intent data = new Intent();
                // Pass relevant data back as a result
                data.putExtra("number", commentCount);
                data.putExtra("code", 200); // ints work too
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }
        });

    }
}
