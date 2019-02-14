package com.swsnack.catchhouse.viewmodel;

import androidx.lifecycle.ViewModel;

import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.DataManager;

import io.reactivex.disposables.CompositeDisposable;

public class ReactiveViewModel extends ViewModel {

    private CompositeDisposable mDisposables;
    private DataManager mDataManager;
    private APIManager mApiManager;

    public ReactiveViewModel(DataManager dataManager, APIManager apiManager) {
        mDisposables = new CompositeDisposable();
        mDataManager = dataManager;
        mApiManager = apiManager;
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

    protected APIManager getApiManager() {
        return mApiManager;
    }
}
