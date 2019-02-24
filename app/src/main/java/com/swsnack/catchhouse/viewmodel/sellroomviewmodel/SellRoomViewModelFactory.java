package com.swsnack.catchhouse.viewmodel.sellroomviewmodel;

import com.swsnack.catchhouse.repository.RoomRepository;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SellRoomViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private RoomRepository mRoomRepository;

    public SellRoomViewModelFactory(RoomRepository roomRepository) {
        this.mRoomRepository = roomRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SellRoomViewModel.class)) {
            return (T) new SellRoomViewModel(mRoomRepository);
        }
        throw new Fragment.InstantiationException("not viewModel Class", null);
    }
}
