package com.example.messageapp.asyncTask;

public interface Callback<R> {

    void runResultOnUiThread(R result);
}
