package com.example.vikramjeet.challengerapp.models;

public enum ChallengeStatus {
    OPEN("OPEN"),
    BACKED("BACKED"),
    COMPLETED("COMPLETED"), // completed by poster
    VERIFIED("VERIFIED") // verified by backer
    ;

    private final String text;

    private ChallengeStatus(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}