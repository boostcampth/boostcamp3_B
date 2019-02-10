package com.swsnack.catchhouse.viewmodel.homeviewmodel;

import android.app.Application;

import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

public class HomeViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private ViewModelListener mListener;
    private DataManager mDataManager;

    HomeViewModel(Application application, DataManager dataManager, ViewModelListener listener) {
        super(dataManager);
        mAppContext = application;
        mListener = listener;
        mDataManager = dataManager;
    }
}
