package com.swsnack.catchhouse.view.activities;

public interface BottomNavListener {

    void onError(String errorMessage);

    void onSuccess(String success);

    void isWorking();

    void isFinished();

    void openMapFragment();

    void openWriteActivity();

}
