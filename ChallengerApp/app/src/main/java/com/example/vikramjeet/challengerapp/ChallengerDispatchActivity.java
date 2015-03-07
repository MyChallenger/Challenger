package com.example.vikramjeet.challengerapp;

import com.parse.ui.ParseLoginDispatchActivity;

public class ChallengerDispatchActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
        return MainActivity.class;
    }
}
