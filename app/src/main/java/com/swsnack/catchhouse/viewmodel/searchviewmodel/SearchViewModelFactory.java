package com.swsnack.catchhouse.viewmodel.searchviewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.view.activities.BottomNavListener;

public class SearchViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private RoomsRepository mRepository;
    private BottomNavListener mListener;

    public SearchViewModelFactory(@NonNull Application application, RoomsRepository repository, BottomNavListener listener) {
        this.mApplication = application;
        this.mRepository = repository;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(mApplication, mRepository, mListener);
        }
        throw new Fragment.InstantiationException("not viewModel Class", null);

    }


}
