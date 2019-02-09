package com.swsnack.catchhouse.viewmodel.chattingviewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.chattingdata.remote.RemoteChattingManager;
import com.swsnack.catchhouse.data.userdata.api.AppAPIManager;
import com.swsnack.catchhouse.data.userdata.remote.AppUserDataManager;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

public class ChattingViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ViewModelListener mViewModelListener;

    public ChattingViewModelFactory(ViewModelListener viewModelListener) {
        this.mViewModelListener = viewModelListener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChattingViewModel.class)) {
            return (T) new ChattingViewModel(AppDataManager.getInstance(AppAPIManager.getInstance(),
                    AppUserDataManager.getInstance(),
                    RemoteChattingManager.getInstance()),
                    mViewModelListener);
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}