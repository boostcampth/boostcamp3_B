package com.swsnack.catchhouse.viewmodel;

import androidx.lifecycle.ViewModel;

import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.DataSource;

import io.reactivex.disposables.CompositeDisposable;

public class ReactiveViewModel extends ViewModel {

    private CompositeDisposable mDisposables;
    private DataSource mDataManager;
    private APIManager mApiManager;

    public ReactiveViewModel(DataSource dataManager, APIManager apiManager) {
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

    protected DataSource getDataManager() {
        return mDataManager;
    }

    protected APIManager getApiManager() {
        return mApiManager;
    }
}
