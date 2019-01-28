package com.boostcampa2.catchhouse.viewmodel;

public interface ViewModelListener {

    void onError(Throwable throwable);

    void isWorking();

    void isFinished();

}
