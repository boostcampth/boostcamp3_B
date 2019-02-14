package com.swsnack.catchhouse.viewmodel.searchingviewmodel;


import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

public class SearchingViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private DataManager mDataManager;
    private APIManager mApiManager;
    private ViewModelListener mListener;

    public SearchingViewModelFactory(@NonNull Application application, DataManager dataManager, APIManager apiManager, ViewModelListener listener) {
        this.mApplication = application;
        this.mDataManager = dataManager;
        this.mApiManager = apiManager;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchingViewModel.class)) {
            return (T) new SearchingViewModel(mApplication, mDataManager, mApiManager, mListener);
        }
        throw new Fragment.InstantiationException("not viewModel Class", null);

    }

}

