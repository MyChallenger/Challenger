package com.example.vikramjeet.challengerapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.configurations.YouTubeProperties;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.models.MediaProvider;
import com.example.vikramjeet.challengerapp.models.MediaType;
import com.example.vikramjeet.challengerapp.services.UploadResultReceiver;
import com.example.vikramjeet.challengerapp.services.UploadService;
import com.melnykov.fab.FloatingActionButton;
import com.parse.GetCallback;
import com.parse.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ReviewVideoActivity extends ActionBarActivity {

    public static String EXTRA_CHALLENGE_ID = "com.example.vikramjeet.challengerapp.challenge_id";
    public static String EXTRA_MEDIA_TYPE = "com.example.vikramjeet.challengerapp.media_type";
    public static String EXTRA_RECEIVER = "com.example.vikramjeet.challengerapp.receiver";

    @InjectView(R.id.vv)
    VideoView vv;
    @InjectView(R.id.btnUpload)
    FloatingActionButton btnUpload;

    MediaController mc;
    private String mChosenAccountName;
    private Uri mFileUri;

    private ResultReceiver mUploadResultReceiver;
    private YouTubeProperties mYouTubeProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_review_video);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            btnUpload.setVisibility(View.GONE);
            setTitle(R.string.playing_the_video_in_upload_progress);
        }
        mFileUri = intent.getData();
        loadAccount();
        mYouTubeProperties = new YouTubeProperties(this);

        if (mChosenAccountName == null) {
            String hardcodedUserName = mYouTubeProperties.getProperty(YouTubeProperties.PROP_USERNAME);
            if (hardcodedUserName != null) {
                mChosenAccountName = hardcodedUserName;
            }
        }

        reviewVideo(mFileUri);
        setupServiceReceiver();
    }

    private void reviewVideo(Uri mFileUri) {
        try {
            vv = (VideoView) findViewById(R.id.vv);
            mc = new MediaController(this);
            vv.setMediaController(mc);
            vv.setVideoURI(mFileUri);
            mc.show();
            vv.start();
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), e.toString());
        }
    }

    private void loadAccount() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        mChosenAccountName = sp.getString(PickVideoActivity.ACCOUNT_KEY, null);
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.review, menu);
        return true;
    }

    public void uploadVideo(View view) {
        if (mChosenAccountName == null) {
            return;
        }
        // if a video is picked or recorded.
        if (mFileUri != null) {
            Cursor returnCursor =
                    getContentResolver().query(mFileUri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            String fileName = returnCursor.getString(nameIndex);
//            String fileSize = Long.toString(returnCursor.getLong(sizeIndex));
            // Have we already uploaded the video for the demo?
            // If so, skip the upload and just update the videoId in Parse
            String challengeId = getIntent().getStringExtra(EXTRA_CHALLENGE_ID);
            final String videoId = mYouTubeProperties.getProperty(fileName);
            final MediaType mediaType = MediaType.values()[getIntent().getIntExtra(EXTRA_MEDIA_TYPE, 0)];
            if (videoId != null) {
                Challenge.getChallengeByID(challengeId, new GetCallback<Challenge>() {
                    @Override
                    public void done(Challenge challenge, ParseException e) {
                        challenge.updateMediaInformation(videoId, MediaProvider.YOUTUBE, mediaType);
                        showHomeScreen();
                        Bundle bundle = new Bundle();
                        bundle.putString(UploadResultReceiver.EXTRA_RESULT_VALUE, videoId);
                        // Here we call send passing a resultCode and the bundle of extras
                        mUploadResultReceiver.send(Activity.RESULT_OK, bundle);
                    }
                });
            } else {
                Intent uploadIntent = new Intent(this, UploadService.class);
                uploadIntent.setData(mFileUri);
                uploadIntent.putExtra(PickVideoActivity.ACCOUNT_KEY, mChosenAccountName);
                uploadIntent.putExtra(UploadService.EXTRA_RECEIVER, mUploadResultReceiver);
                uploadIntent.putExtra(UploadService.EXTRA_CHALLENGE_ID, challengeId);
                uploadIntent.putExtra(UploadService.EXTRA_MEDIA_TYPE, getIntent().getIntExtra(EXTRA_MEDIA_TYPE, 0));
                startService(uploadIntent);
                showHomeScreen();
            }
            // Go back to PickVideoActivity after upload
//            finish();
        }
    }

    private void showHomeScreen() {
        Toast.makeText(this, R.string.youtube_upload_started,
                Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    // Setup the callback for when data is received from the service
    public void setupServiceReceiver() {
        mUploadResultReceiver = getIntent().getParcelableExtra(EXTRA_RECEIVER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
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
