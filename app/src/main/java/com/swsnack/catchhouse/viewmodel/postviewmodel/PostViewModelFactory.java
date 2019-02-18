package com.swsnack.catchhouse.viewmodel.postviewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.Fragment;

import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.DataDataSource;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import io.reactivex.annotations.NonNull;

public class PostViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private DataDataSource mDataManager;
    private APIManager mApiManager;
    private ViewModelListener mListener;

    public PostViewModelFactory(DataDataSource dataManager, APIManager apiManager, ViewModelListener listener) {
        this.mDataManager = dataManager;
        this.mApiManager = apiManager;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PostViewModel.class)) {
            return (T) new PostViewModel(mDataManager, mApiManager, mListener);
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}
