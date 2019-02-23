package com.swsnack.catchhouse.viewmodel.recentroomviewmodel;

import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.repository.recentroom.RecentRoomRepository;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class RecentRoomViewModel extends ReactiveViewModel {

    private RecentRoomRepository mRecentRoomRepository;
    private MutableLiveData<List<Room>> mRecentRoomList;

    RecentRoomViewModel(RecentRoomRepository recentRoomRepository) {
        this.mRecentRoomRepository = recentRoomRepository;
        this.mRecentRoomList = new MutableLiveData<>();
    }

    public void getRecentRoom() {
        mRecentRoomList.setValue(
                mRecentRoomRepository.getRecentRoom()
        );
    }

    public void addRecentRoom(Room room) {
        mRecentRoomRepository.setRecentRoom(room);
    }

    public LiveData<List<Room>> getRecentRoomList() {
        return mRecentRoomList;
    }
}
