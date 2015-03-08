package com.example.vinutha.challenger.Model;

import java.util.List;

/**
 * Created by devadutta on 3/7/2015.
 */
public class User {
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
