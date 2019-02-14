package com.swsnack.catchhouse.data.db.room.local;

import com.swsnack.catchhouse.data.AppDatabase;
import com.swsnack.catchhouse.data.entity.RoomEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

public class AppRoomLocalDataManager implements LocalRoomDataSource {

    private static AppRoomLocalDataManager INSTANCE;
    private RoomDao mRoomDao;

    public static AppRoomLocalDataManager getInstance() {
        if (INSTANCE == null) {
            synchronized (AppRoomLocalDataManager.class) {
                INSTANCE = new AppRoomLocalDataManager();
            }
        }
        return INSTANCE;
    }

    private AppRoomLocalDataManager() {
        mRoomDao = AppDatabase.getInstance().getRoomDataAccesser();
    }

    @Override
    public void setFavoriteRoom(RoomEntity roomEntity) {
        new RoomLocalDataHelper.AsyncSetFavoriteRoom(mRoomDao).execute(roomEntity);
    }

    @Override
    public void deleteFavoriteRoom(RoomEntity roomEntity) {
        new RoomLocalDataHelper.AsyncDeleteFavoriteRoom(mRoomDao).execute(roomEntity);

    }

    @Override
    public LiveData<List<RoomEntity>> getFavoriteRoom() {
        try {
            return new RoomLocalDataHelper.AsyncLoadFavoriteRoom(mRoomDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }
}
