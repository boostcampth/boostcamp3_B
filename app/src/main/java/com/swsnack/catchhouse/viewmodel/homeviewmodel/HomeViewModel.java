package com.swsnack.catchhouse.viewmodel.homeviewmodel;

import android.app.Application;

import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.view.activities.BottomNavListener;

public class HomeViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private BottomNavListener mListener;
    private DataManager mDataManager;

    HomeViewModel(Application application, DataManager dataManager, BottomNavListener listener) {
        super(dataManager);
        mAppContext = application;
        mListener = listener;
        mDataManager = dataManager;
    }

    public void onClickSearchButton() {
        mListener.openMapFragment();
    }

    public void onClickPostButton() {
        mListener.openWriteActivity();
    }
}
