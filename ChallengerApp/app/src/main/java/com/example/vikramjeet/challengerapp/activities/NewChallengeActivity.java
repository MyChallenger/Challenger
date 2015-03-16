package com.example.vikramjeet.challengerapp.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vikramjeet.challengerapp.R;
import com.example.vikramjeet.challengerapp.models.Challenge;


public class NewChallengeActivity extends ActionBarActivity {
    public static final String TAG = "NewChallengeActivity";
    private EditText etTitle;
    private EditText etBurb;
    private EditText etLocation;
    private EditText etGoal;
    private EditText etImageUrl;
    private Spinner spExpiry;
    private Spinner spCategory;

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
        if(validateInput()) {
            finish();
        }
        else {
            //Display Error message
            Toast.makeText(this, "Error could not add", Toast.LENGTH_SHORT);
            Log.d(TAG,"Could not add new Challenge");
        }
    }

    private boolean validateInput() {
        // Title should not be empty
        if(etTitle.getText().length() < 1) {
            Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT);
            return false;
        }
        // Needs to be at least 20 chars long
        if(etBurb.getText().length() < 20) {
            Toast.makeText(this, "Description needs to be at least 20 chars long", Toast.LENGTH_SHORT);
            return false;
        }
        // Goal cant be empty
        if(etGoal.getText().length() < 1) {
            Toast.makeText(this, "Goal cant be empty!", Toast.LENGTH_SHORT);
            return false;
        }
        // End date cant be empty
        //if()
        Challenge newChallenge;
        if(etTitle.getText() != null && etBurb.getText() != null) {
            newChallenge = new Challenge();
            newChallenge.setTitle(etTitle.getText().toString());
            newChallenge.setDescription(etBurb.getText().toString());
            newChallenge.setCategory(spCategory.getSelectedItem().toString());
            newChallenge.setPrize(etGoal.getText().toString());
            //newChallenge.setExpiryDate(spExpiry.getSelectedItem().toString());
           // newChallenge.setLocation(etLocation.getText().toString()); // Convert to geo location
            try {
                newChallenge.save();
                Toast.makeText(this, "Created New Challenge", Toast.LENGTH_SHORT);
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
}
