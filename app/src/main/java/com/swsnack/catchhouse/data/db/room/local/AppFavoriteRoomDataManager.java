package com.swsnack.catchhouse.data.db.room.local;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.data.AppDatabase;
import com.swsnack.catchhouse.data.entity.RoomEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

public class AppFavoriteRoomDataManager implements FavoriteRoomManager {

    private static AppFavoriteRoomDataManager INSTANCE;
    private RoomDao mRoomDao;

    public static AppFavoriteRoomDataManager getInstance() {
        if (INSTANCE == null) {
            synchronized (AppFavoriteRoomDataManager.class) {
                INSTANCE = new AppFavoriteRoomDataManager();
            }
        }
        return INSTANCE;
    }

    private AppFavoriteRoomDataManager() {
        mRoomDao = AppDatabase.getInstance().getRoomDataAccessor();
    }

    @Override
    public void setFavoriteRoom(RoomEntity roomEntity) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        roomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new FavoriteRoomHelper.AsyncSetFavoriteRoom(mRoomDao).execute(roomEntity);
    }

    @Override
    public void deleteFavoriteRoom(RoomEntity roomEntity) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        roomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new FavoriteRoomHelper.AsyncDeleteFavoriteRoom(mRoomDao).execute(roomEntity);

    }

    @Override
    public List<RoomEntity> getFavoriteRoomList() {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }

        try {
            return new FavoriteRoomHelper.AsyncLoadFavoriteRoomList(mRoomDao).execute(FirebaseAuth.getInstance().getCurrentUser().getUid()).get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    @Override
    public RoomEntity getFavoriteRoom(String key) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }

        try {
            return new FavoriteRoomHelper.AsyncLoadFavoriteRoom(mRoomDao).execute(key, FirebaseAuth.getInstance().getCurrentUser().getUid()).get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }
}
