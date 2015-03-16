package com.example.vikramjeet.challengerapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.vikramjeet.challengerapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.example.vikramjeet.challengerapp.services.UploadResultReceiver;
import com.example.vikramjeet.challengerapp.services.UploadService;

public class ReviewVideoActivity extends ActionBarActivity {
    @InjectView(R.id.vv)
    VideoView vv;
    @InjectView(R.id.btnUpload)
    Button btnUpload;

    MediaController mc;
    private String mChosenAccountName;
    private Uri mFileUri;

    private UploadResultReceiver mUploadResultReceiver;

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
            Intent uploadIntent = new Intent(this, UploadService.class);
            uploadIntent.setData(mFileUri);
            uploadIntent.putExtra(PickVideoActivity.ACCOUNT_KEY, mChosenAccountName);
            uploadIntent.putExtra("receiver", mUploadResultReceiver);
            uploadIntent.putExtra("receiver", mUploadResultReceiver);
            startService(uploadIntent);
            Toast.makeText(this, R.string.youtube_upload_started,
                    Toast.LENGTH_LONG).show();
            // Go back to PickVideoActivity after upload
            finish();
        }
    }

    // Setup the callback for when data is received from the service
    public void setupServiceReceiver() {
        mUploadResultReceiver = new UploadResultReceiver(new Handler());
        // This is where we specify what happens when data is received from the service
        mUploadResultReceiver.setReceiver(new UploadResultReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    String videoId = resultData.getString("resultValue");
                    Toast.makeText(ReviewVideoActivity.this, videoId, Toast.LENGTH_SHORT).show();
                }
            }
        });
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

}
