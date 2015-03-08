package com.example.vikramjeet.challengerapp.models;

import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Vikramjeet on 3/7/15.
 */
public class User extends ParseUser {

    private String name;
    private String photoURL;
    private String location;
    private int pointsEarned;
    private int challengesCompleted;
    private List<Challenge> currentlyAcceptedChallenges;        // challenges that he needs to complete
    private int leaderBoardRank;

    public User() {
        super();
    }


}
