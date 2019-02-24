package com.swsnack.catchhouse.viewmodel.chattingviewmodel;

import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.AppDataSource;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChattingViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ViewModelListener mBottomNavListener;

    public ChattingViewModelFactory(ViewModelListener bottomNavListener) {
        this.mBottomNavListener = bottomNavListener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChattingViewModel.class)) {
            return (T) new ChattingViewModel(AppDataSource.getInstance(), APIManager.getInstance(), mBottomNavListener);
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}