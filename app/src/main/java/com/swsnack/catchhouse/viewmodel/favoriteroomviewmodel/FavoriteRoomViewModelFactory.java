package com.swsnack.catchhouse.viewmodel.favoriteroomviewmodel;

import com.swsnack.catchhouse.repository.favoriteroom.FavoriteRoomRepository;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FavoriteRoomViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private FavoriteRoomRepository mFavoriteRoomRepository;

    public FavoriteRoomViewModelFactory(FavoriteRoomRepository favoriteRoomRepository) {
        this.mFavoriteRoomRepository = favoriteRoomRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FavoriteRoomViewModel.class)) {
            return (T) new FavoriteRoomViewModel(this.mFavoriteRoomRepository);
        }
        throw new Fragment.InstantiationException("not viewModel class", null);
    }
}
