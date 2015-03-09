package com.example.vikramjeet.challengerapp;

import android.test.AndroidTestCase;

import com.parse.Parse;

import java.util.concurrent.CountDownLatch;

public abstract class ChallengerTestCase extends AndroidTestCase {

    protected CountDownLatch countDownLatch;

    @Override
    protected void setUp() {
        countDownLatch = new CountDownLatch(1);
        Parse.initialize(getContext(), getContext().getString(R.string.parse_app_id),
                getContext().getString(R.string.parse_client_key));
    }

    protected void await() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void countDown() {
        countDownLatch.countDown();
    }
}
