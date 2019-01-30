package com.swsnack.catchhouse.viewmodel;

public interface ViewModelListener {

    void onError(Throwable throwable);

    void isWorking();

    void isFinished();

}
