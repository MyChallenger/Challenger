package com.example.vikramjeet.challengerapp.broadcastreceivers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.vikramjeet.challengerapp.activities.CompleteChallengeActivity;
import com.example.vikramjeet.challengerapp.activities.CompletedChallengeDetailActivity;
import com.example.vikramjeet.challengerapp.activities.LeaderboardActivity;
import com.example.vikramjeet.challengerapp.models.ChallengeStatus;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ChallengerPushBroadcastReceiver extends ParsePushBroadcastReceiver {

    private static final String TAG = ChallengerPushBroadcastReceiver.class.getName();

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        // Parse push message and handle accordingly
        processPush(context, intent);
    }

//    {
//        "title": "Game on!",
//        "alert": "Challenge YourChallenge has been backed by Vik",
//        "customdata": {
//            "status": "BACKED",
//            "id": "MObCzmgoX1"
//        }
//    }
    private void processPush(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "got action " + action);
        if (action.equals(ChallengerPushBroadcastReceiver.ACTION_PUSH_OPEN)) {
            String channel = intent.getExtras().getString("com.parse.Channel");
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
                // Iterate the parse keys if needed
                Iterator<String> itr = json.keys();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    // Extract custom push data
                    if (key.equals("customdata")) {
                        // Handle push notification by invoking activity directly
                        launchAppropriateActivity(context, json.getJSONObject(key));
                        // OR trigger a broadcast to activity
//                        triggerBroadcastToActivity(context);
                        // OR create a local notification
//                        createNotification(context);
                        break;
                    }
                    Log.d(TAG, "..." + key + " => " + json.getString(key));
                }
            } catch (JSONException ex) {
                Log.d(TAG, "JSON failed!");
            }
        }
    }

    private void launchAppropriateActivity(Context context, JSONObject datavalue) {
        if (datavalue.optString("status") != null) {
            Class clazz = null;
            String name = null;
            switch (ChallengeStatus.valueOf(datavalue.optString("status"))) {
                case OPEN:
                    break;
                case BACKED:
                    clazz = CompleteChallengeActivity.class;
                    name = CompleteChallengeActivity.EXTRA_CHALLENGE_ID;
                    break;
                case COMPLETED:
                    clazz = CompletedChallengeDetailActivity.class;
                    name = CompletedChallengeDetailActivity.EXTRA_CHALLENGE_ID;
                    break;
                case VERIFIED:
                    clazz = LeaderboardActivity.class;
                    break;
            }
            Intent pupInt = new Intent(context, clazz);
            pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            if (name != null) {
                pupInt.putExtra(name, datavalue.optString("id"));
            }
            context.getApplicationContext().startActivity(pupInt);
        }
    }
}
