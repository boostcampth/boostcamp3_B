package com.swsnack.catchhouse.viewmodel.postviewmodel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.entity.RoomEntity;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.data.model.ExpectedPrice;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends ReactiveViewModel {

    public final MutableLiveData<List<String>> mImageList = new MutableLiveData<>();
    public final MutableLiveData<String> mExpectedPrice = new MutableLiveData<>();
    public final MutableLiveData<String> mOptionTag = new MutableLiveData<>();
    private MutableLiveData<Room> mRoom;
    private MutableLiveData<Boolean> mIsFavorite;

    PostViewModel(DataManager dataManager, APIManager apiManager, ViewModelListener listener) {
        super(dataManager, apiManager);
        mRoom = new MutableLiveData<>();
        mIsFavorite = new MutableLiveData<>();
        init();
    }

    public void setInitRoomData(Room room) {
        mImageList.setValue(room.getImages());
        ExpectedPrice expectedPrice =
                new ExpectedPrice(room.getPrice(), room.getFrom(), room.getTo());

        mExpectedPrice.setValue(expectedPrice.getExpectedPrice());

        mOptionTag.setValue(
                createOptionString(
                        room.isOptionStandard(),
                        room.isOptionGender(),
                        room.isOptionPet(),
                        room.isOptionSmoking()
                )
        );
    }

    private void init() {
        mImageList.setValue(new ArrayList<>());
        mExpectedPrice.setValue("");
        mOptionTag.setValue("");
    }

    private String createOptionString(boolean std, boolean gender, boolean pet, boolean smoking) {
        String tag = "";

        if (std) {
            tag += "#기본옵션 ";
        }

        if (gender) {
            tag += "#동일성별 ";
        }

        if (pet) {
            tag += "#반려동물가능 ";
        }

        if (smoking) {
            tag += "#흡연가능 ";
        }

        return tag;
    }

    public void setRoomData(Room roomData) {
        mRoom.setValue(roomData);
    }

    public void addFavoriteRoom(View v) {
        getDataManager()
                .setFavoriteRoom(RoomEntity.toRoomEntity(mRoom.getValue()));
        mIsFavorite.setValue(true);
    }

    public LiveData<Boolean> isFavorite() {
        return mIsFavorite;
    }

}
