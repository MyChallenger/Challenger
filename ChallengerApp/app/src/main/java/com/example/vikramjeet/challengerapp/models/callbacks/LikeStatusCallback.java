package com.example.vikramjeet.challengerapp.models.callbacks;

public abstract class LikeStatusCallback<Boolean> {

    public abstract void done(Boolean isLiked, com.parse.ParseException e);
}
