package com.swsnack.catchhouse.viewmodel.homeviewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.Fragment;

import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.chattingdata.remote.RemoteChattingManager;
import com.swsnack.catchhouse.data.userdata.api.AppAPIManager;
import com.swsnack.catchhouse.data.userdata.remote.AppUserDataManager;
import com.swsnack.catchhouse.view.activities.BottomNavListener;

import io.reactivex.annotations.NonNull;

public class HomeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private BottomNavListener mBottomNavListener;

    public HomeViewModelFactory(BottomNavListener bottomNavListener) {
        this.mBottomNavListener = bottomNavListener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(AppApplication.getAppContext()
                    , AppDataManager.getInstance(
                    AppAPIManager.getInstance(),
                    AppUserDataManager.getInstance(),
                    RemoteChattingManager.getInstance()),
                    mBottomNavListener
                    );
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}
