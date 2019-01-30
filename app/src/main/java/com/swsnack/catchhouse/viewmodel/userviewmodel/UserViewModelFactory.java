package com.swsnack.catchhouse.viewmodel.userviewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.swsnack.catchhouse.viewmodel.ViewModelListener;

public class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private ViewModelListener mListener;

    public UserViewModelFactory(@NonNull Application application, ViewModelListener listener) {
        this.mApplication = application;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(mApplication, mListener);
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}
