package com.swsnack.catchhouse.viewmodel.recentroomviewmodel;


import com.swsnack.catchhouse.data.source.recentroom.RecentRoomRepository;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RecentRoomViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private RecentRoomRepository mRecentRoomRepository;

    public RecentRoomViewModelFactory(RecentRoomRepository recentRoomRepository) {
        this.mRecentRoomRepository = recentRoomRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecentRoomViewModel.class)) {
            return (T) new RecentRoomViewModel(mRecentRoomRepository);
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}
