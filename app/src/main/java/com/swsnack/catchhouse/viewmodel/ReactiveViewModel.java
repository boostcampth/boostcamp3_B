package com.swsnack.catchhouse.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.swsnack.catchhouse.data.DataManager;

import io.reactivex.disposables.CompositeDisposable;

public class ReactiveViewModel extends ViewModel {

    private CompositeDisposable mDisposables;
    private DataManager mDataManager;

    public ReactiveViewModel(DataManager dataManager) {
        mDisposables = new CompositeDisposable();
        mDataManager = dataManager;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposables.dispose();
    }

    protected CompositeDisposable getCompositeDisposable() {
        return mDisposables;
    }

    protected DataManager getDataManager() {
        return mDataManager;
    }
}
