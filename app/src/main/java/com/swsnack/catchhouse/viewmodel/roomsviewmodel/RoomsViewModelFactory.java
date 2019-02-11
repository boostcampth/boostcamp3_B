package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.Fragment;

import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.view.activities.BottomNavListener;
import com.swsnack.catchhouse.view.activities.WriteListener;

import io.reactivex.annotations.NonNull;

public class RoomsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private DataManager mDataManager;
    private WriteListener mListener;

    public RoomsViewModelFactory(@NonNull Application application, DataManager dataManager, WriteListener listener) {
        this.mApplication = application;
        this.mDataManager = dataManager;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RoomsViewModel.class)) {
            return (T) new RoomsViewModel(mApplication, mDataManager, mListener);
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}
