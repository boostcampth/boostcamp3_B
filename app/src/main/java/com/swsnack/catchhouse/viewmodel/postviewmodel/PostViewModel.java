package com.swsnack.catchhouse.viewmodel.postviewmodel;

import android.app.Application;

import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

public class PostViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private ViewModelListener mListener;

    PostViewModel(DataManager dataManager, ViewModelListener listener) {
        super(dataManager);
        mAppContext = AppApplication.getAppContext();
        mListener = listener;
    }


}