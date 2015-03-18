package com.example.vikramjeet.challengerapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.models.Challenge;
import com.example.vikramjeet.challengerapp.models.MediaType;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import static junit.framework.Assert.assertNull;


public class NewChallengeActivity extends ActionBarActivity {
    public static final String TAG = "NewChallengeActivity";
    private EditText etTitle;
    private EditText etBurb;
    private EditText etLocation;
    private EditText etGoal;
    private EditText etImageUrl;
    private Spinner spExpiry;
    private Spinner spCategory;
    private String mChallengeId;

    Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_challenge);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etBurb = (EditText) findViewById(R.id.etShortBurb);
        etGoal = (EditText) findViewById(R.id.etGoal);
        etLocation = (EditText) findViewById(R.id.etLocation);
        spExpiry = (Spinner) findViewById(R.id.spinnerEndDate);
        spCategory = (Spinner) findViewById(R.id.spinnerCategory);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_challenge, menu);
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

    //Public Methods
    public void onAddChallenge(View view) {
        try {
        if(validateInput()) {
            finish();
            uploadVideo();
        }
        else {
            //Display Error message
            Toast.makeText(this, "Error could not add", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"Could not add new Challenge");
        }}
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void uploadVideo() {
        Intent i = new Intent(this, PickVideoActivity.class);
        i.putExtra(PickVideoActivity.EXTRA_CHALLENGE_ID, mChallengeId);
        i.putExtra(PickVideoActivity.EXTRA_MEDIA_TYPE, MediaType.CREATED.ordinal());
        startActivity(i);
    }

    private boolean validateInput() throws IOException {
        // Title should not be empty
        if(etTitle.getText().length() < 1) {
            Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Needs to be at least 20 chars long
        if(etBurb.getText().length() < 20) {
            Toast.makeText(this, "Description needs to be at least 20 chars long", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Goal cant be empty
        if(etGoal.getText().length() < 1) {
            Toast.makeText(this, "Goal cant be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        // End date cant be empty
        //if()
        final Challenge newChallenge;
        if(etTitle.getText() != null && etBurb.getText() != null) {
            newChallenge = new Challenge();
            //newChallenge.setStatus();
            newChallenge.setTitle(etTitle.getText().toString());
            newChallenge.setDescription(etBurb.getText().toString());
            newChallenge.setCategory(spCategory.getSelectedItem().toString());
            newChallenge.setPrize(etGoal.getText().toString());
            if(photoUri != null) {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                final ParseFile gallaryMedia = new ParseFile("CreatedFile.jpeg",bitmapdata);
                gallaryMedia.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        assertNull(e);
                        newChallenge.setCreatedMedia(gallaryMedia);
                        newChallenge.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                assertNull(e);
                                //countDown();
                            }
                        });
                    }
                });
                //await();
                //newChallenge.setCreatedMedia(gallaryMedia);
            }
            newChallenge.setExpiryDate(calculateExpiryTime(spExpiry.getSelectedItemPosition()));
           // newChallenge.setLocation(etLocation.getText().toString()); // Convert to geo location
            try {
                newChallenge.save();
                mChallengeId = newChallenge.getObjectId();
                Toast.makeText(this, "Created New Challenge", Toast.LENGTH_SHORT).show();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Log.e("TAG", "Adding Challenge");
        }

        return true;
    }

    private Date calculateExpiryTime( int offset) {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date today = null;
        if( offset == 0) {
            today  = new Date(System.currentTimeMillis()+  DAY_IN_MS);
        }
        else if(offset == 1)
            today =new Date(System.currentTimeMillis() + 2 * DAY_IN_MS);
        else if(offset == 2)
            today =new Date(System.currentTimeMillis() + 3 * DAY_IN_MS);
        else if(offset == 3)
            today =new Date(System.currentTimeMillis() + 7 * DAY_IN_MS);
        else if(offset == 4)
            today =new Date(System.currentTimeMillis() + 14 * DAY_IN_MS);
        return today;
    }

    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1047;

    // Trigger gallery selection for a photo
    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Bring up gallery to select a photo
        startActivityForResult(intent, PICK_PHOTO_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            photoUri = data.getData();
            try {
                // Do something with the photo based on Uri
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                // Load the selected image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.imageButton);
                ivPreview.setImageBitmap(selectedImage);
            }catch (IOException e) {
                Log.e(TAG,"Image could not be loaded(Gallery");
            }

        }
    }

}
