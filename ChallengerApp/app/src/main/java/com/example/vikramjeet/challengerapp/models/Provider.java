package com.example.vikramjeet.challengerapp.models;

public enum Provider {
    FACEBOOK("FACEBOOK"),
    TWITTER("TWITTER"),
    PARSE("PARSE")
    ;

    private final String text;

    private Provider(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}