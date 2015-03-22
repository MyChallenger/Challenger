package com.example.vikramjeet.challengerapp.models;

public enum AuthProvider {
    FACEBOOK("FACEBOOK"),
    TWITTER("TWITTER"),
    PARSE("PARSE");

    private final String text;

    private AuthProvider(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}