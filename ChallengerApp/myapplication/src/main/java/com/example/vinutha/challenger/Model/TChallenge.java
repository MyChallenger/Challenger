package com.example.vinutha.challenger.Model;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class TChallenge {
    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getGoalAmount() {
        return goalAmount;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    private String title;
    private String category;
    private int goalAmount;
    private String expiryDate;

    public TChallenge(String name, String category, int amount, String date) {
        this.title = name;
        this.category = category;
        this.goalAmount = amount;
        this.expiryDate = date;
    }
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
            // Toast.makeText(this, relativeDate, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
