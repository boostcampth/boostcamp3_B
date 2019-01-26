package com.boostcampa2.catchhouse.viewmodel.userviewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;

    public UserViewModelFactory(@NonNull Application application) {
        this.mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(mApplication);
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}
