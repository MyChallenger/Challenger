package com.example.vikramjeet.challengerapp.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

// Defines a generic receiver used to pass data to Activity from a Service
public class UploadResultReceiver extends ResultReceiver {

    public static final String EXTRA_RESULT_VALUE = "resultValue";

    private Receiver receiver;

    // Constructor takes a handler
    public UploadResultReceiver(Handler handler) {
        super(handler);
    }

    // Setter for assigning the receiver
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    // Defines our event interface for communication
    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    // Delegate method which passes the result to the receiver if the receiver has been assigned
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }
}
