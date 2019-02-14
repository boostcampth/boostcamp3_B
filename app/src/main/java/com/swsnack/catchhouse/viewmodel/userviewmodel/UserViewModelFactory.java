package com.swsnack.catchhouse.viewmodel.userviewmodel;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

public class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private DataManager mDatamanager;
    private APIManager mApiManager;
    private ViewModelListener mListener;

    public UserViewModelFactory(@NonNull Application application, DataManager dataManager, APIManager apiManager, ViewModelListener listener) {
        this.mApplication = application;
        this.mDatamanager = dataManager;
        this.mApiManager = apiManager;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(mApplication, mDatamanager, mApiManager, mListener);
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}
