package com.swsnack.catchhouse.viewmodel.searchviewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.db.rooms.RoomsRepository;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

public class SearchViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private RoomsRepository mRepository;
    private APIManager mApiManager;
    private ViewModelListener mListener;

    public SearchViewModelFactory(@NonNull Application application, RoomsRepository repository, APIManager apiManager, ViewModelListener listener) {
        this.mApplication = application;
        this.mRepository = repository;
        this.mApiManager = apiManager;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(mApplication, mRepository, mApiManager, mListener);
        }
        throw new Fragment.InstantiationException("not viewModel Class", null);

    }


}
