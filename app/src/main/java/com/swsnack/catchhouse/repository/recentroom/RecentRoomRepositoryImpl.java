package com.swsnack.catchhouse.repository.recentroom;

import android.os.Build;

import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

import androidx.annotation.RequiresApi;

public class RecentRoomRepositoryImpl implements RecentRoomRepository{

    private static RecentRoomRepositoryImpl INSTANCE;
    private RecentRoomDao mRecentRoomDao;

    public static RecentRoomRepositoryImpl getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new RecentRoomRepositoryImpl();
        }
        return INSTANCE;
    }

    private RecentRoomRepositoryImpl() {
        this.mRecentRoomDao = RecentRoomDao.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setRecentRoom(Room room) {
        mRecentRoomDao.setRecentRoom(room);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Room> getRecentRoom() {
        return mRecentRoomDao.getRecentRoom();
    }

    @Override
    public void deleteRecentRoomList() {
        mRecentRoomDao.deleteRecentRoomList();
    }

    @Override
    public void deleteRoom(Room room) {
        mRecentRoomDao.deleteRoom(room);
    }
}
