package com.swsnack.catchhouse.viewmodel;

public interface ViewModelListener {

    void onError(String errorMessage);

    void onSuccess(String success);

    void isWorking();

    void isFinished();

}
