package com.swsnack.catchhouse.viewmodel.sellroomviewmodel;

import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.repository.RoomRepository;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SellRoomViewModel extends ReactiveViewModel {

    private RoomRepository mRoomRepository;
    private MutableLiveData<List<Room>> mSellRoomList;

    SellRoomViewModel(RoomRepository roomRepository) {
        this.mRoomRepository = roomRepository;
        this.mSellRoomList  = new MutableLiveData<>();
    }

    public void getSellList() {
        mSellRoomList.setValue(
                mRoomRepository.getSellList());
    }

    public LiveData<List<Room>> getMySellList() {
        return mSellRoomList;
    }
}
