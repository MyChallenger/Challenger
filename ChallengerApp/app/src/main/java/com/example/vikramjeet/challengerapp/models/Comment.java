package com.example.vikramjeet.challengerapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Vikramjeet on 3/9/15.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject {

    // Constants for fields
    private static final String FIELD_AUTHOR = "author";
    private static final String FIELD_TEXT = "text";

    public Comment() {
        // A default constructor is required.
    }

    public User getAuthor() {
        return (User) getParseUser(FIELD_AUTHOR);
    }

    public String getText() {
        return getString(FIELD_TEXT);
    }

    public void setText(String text) {
        put(FIELD_TEXT, text);
    }

}
