package com.swsnack.catchhouse.viewmodel.chattingviewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.AppDataManager;
import com.swsnack.catchhouse.repository.chatting.remote.RemoteChattingManager;
import com.swsnack.catchhouse.repository.location.remote.AppLocationDataManager;
import com.swsnack.catchhouse.repository.room.RoomRepository;
import com.swsnack.catchhouse.repository.searching.remote.AppSearchingDataManager;
import com.swsnack.catchhouse.repository.user.remote.AppUserDataManager;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

public class ChattingViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private APIManager mApiManager;
    private ViewModelListener mBottomNavListener;

    public ChattingViewModelFactory(ViewModelListener bottomNavListener) {
        this.mBottomNavListener = bottomNavListener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChattingViewModel.class)) {
            return (T) new ChattingViewModel(AppDataManager.getInstance(
                    AppUserDataManager.getInstance(),
                    RemoteChattingManager.getInstance(),
                    RoomRepository.getInstance(),
                    AppLocationDataManager.getInstance(),
                    AppSearchingDataManager.getInstance()),
                    mApiManager,
                    mBottomNavListener);
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}