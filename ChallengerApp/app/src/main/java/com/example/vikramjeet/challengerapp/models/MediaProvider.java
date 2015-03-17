package com.example.vikramjeet.challengerapp.models;

public enum MediaProvider {
    YOUTUBE("YOUTUBE");

    private final String text;

    private MediaProvider(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}