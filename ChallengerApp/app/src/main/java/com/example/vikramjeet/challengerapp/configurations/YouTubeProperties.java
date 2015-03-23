package com.example.vikramjeet.challengerapp.configurations;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.vikramjeet.challengerapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class YouTubeProperties extends Properties {

    public static final String PROP_USERNAME = "userName";

    private static final String TAG = YouTubeProperties.class.getName();

    public YouTubeProperties(Context context) {
        // Read from the /res/raw directory
        try {
            InputStream rawResource = context.getResources().openRawResource(R.raw.youtube);
            this.load(rawResource);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Did not find raw resource: " + e);
        } catch (IOException e) {
            Log.e(TAG, "Failed to open youtube.properties property file" + e);
        }
    }
}
