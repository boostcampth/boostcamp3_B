package com.swsnack.catchhouse.viewmodel.postviewmodel;

import android.app.Application;

import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.view.activities.PostListener;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;

public class PostViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private PostListener mListener;

    PostViewModel(DataManager dataManager, PostListener listener) {
        super(dataManager);
        mAppContext = AppApplication.getAppContext();
        mListener = listener;
    }


}
