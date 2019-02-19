package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.Fragment;

import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.DataSource;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import io.reactivex.annotations.NonNull;

public class RoomsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private DataSource mDataManager;
    private APIManager mApiManager;
    private ViewModelListener mListener;

    public RoomsViewModelFactory(@NonNull Application application, DataSource dataManager, APIManager apiManager, ViewModelListener listener) {
        this.mApplication = application;
        this.mDataManager = dataManager;
        this.mApiManager = apiManager;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RoomsViewModel.class)) {
            return (T) new RoomsViewModel(mApplication, mDataManager, mApiManager, mListener);
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}
