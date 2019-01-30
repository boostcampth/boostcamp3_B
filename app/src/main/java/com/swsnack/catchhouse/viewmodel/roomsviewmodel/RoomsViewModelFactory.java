package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import io.reactivex.annotations.NonNull;

public class RoomsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private RoomsRepository mRepository;
    private ViewModelListener mListener;

    public RoomsViewModelFactory(@NonNull Application application, RoomsRepository repository, ViewModelListener listener) {
        this.mApplication = application;
        this.mRepository = repository;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RoomsViewModel.class)) {
            return (T) new RoomsViewModel(mApplication, mRepository, mListener);
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}
