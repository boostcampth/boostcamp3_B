package com.swsnack.catchhouse.view.activities;

public interface WriteListener {

    void onError(String errorMessage);

    void onSuccess(String success);

    void isWorking();

    void isFinished();
}
