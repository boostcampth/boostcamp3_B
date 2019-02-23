package com.swsnack.catchhouse.viewmodel.favoriteroomviewmodel;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.repository.FavoriteRoomRepository;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FavoriteRoomViewModel extends ReactiveViewModel {

    private FavoriteRoomRepository mFavoriteRoomRepository;
    private MutableLiveData<List<Room>> mFavoriteRoomList;
    private MutableLiveData<Boolean> mIsFavorite;

    FavoriteRoomViewModel(FavoriteRoomRepository favoriteRoomRepository) {
        this.mFavoriteRoomRepository = favoriteRoomRepository;
        this.mFavoriteRoomList = new MutableLiveData<>();
        this.mIsFavorite = new MutableLiveData<>();
        this.mIsFavorite.setValue(false);
    }

    public void getFavoriteRoom() {
        mFavoriteRoomList.
                setValue(mFavoriteRoomRepository.getFavoriteRoomList());
    }

    public void setFavoriteRoom(Room room) {
        mFavoriteRoomRepository.setFavoriteRoom(room);
    }

    public void isFavorite(Room room) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        if (!mIsFavorite.getValue()) {
            mFavoriteRoomRepository
                    .setFavoriteRoom(room);
            mIsFavorite.setValue(true);
        } else {
            mFavoriteRoomRepository
                    .deleteFavoriteRoom(room);
            mIsFavorite.setValue(false);
        }
    }

    public LiveData<List<Room>> getFavoriteRoomList() {
        return mFavoriteRoomList;
    }
}
