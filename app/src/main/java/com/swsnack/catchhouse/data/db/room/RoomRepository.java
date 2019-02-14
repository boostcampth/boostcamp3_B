package com.swsnack.catchhouse.data.db.room;

import com.swsnack.catchhouse.data.db.room.local.AppFavoriteRoomDataManager;
import com.swsnack.catchhouse.data.db.room.local.FavoriteRoomManager;
import com.swsnack.catchhouse.data.db.room.remote.AppRoomRemoteDataManager;
import com.swsnack.catchhouse.data.db.room.remote.RoomDataManager;
import com.swsnack.catchhouse.data.entity.RoomEntity;
import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.pojo.Room;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class RoomRepository implements RoomDataManager, FavoriteRoomManager {

    private RoomRepository INSTANCE;
    private FavoriteRoomManager mLocalRoomDataManager;
    private RoomDataManager mRemoteRoomDataManager;

    public RoomRepository getInstance() {
        if(INSTANCE == null) {
            synchronized (RoomRepository.class) {
                INSTANCE = new RoomRepository();
            }
        }
        return INSTANCE;
    }

    private RoomRepository() {

        mLocalRoomDataManager = AppFavoriteRoomDataManager.getInstance();
        mRemoteRoomDataManager = AppRoomRemoteDataManager.getInstance();
    }

    @Override
    public void createKey(@NonNull OnSuccessListener<String> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        mRemoteRoomDataManager.createKey(onSuccessListener, onFailedListener);
    }

    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<byte[]> imageList, @NonNull OnSuccessListener<List<String>> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        mRemoteRoomDataManager.uploadRoomImage(uuid, imageList, onSuccessListener, onFailedListener);
    }

    @Override
    public void setRoom(@NonNull String key, @NonNull Room room, @Nullable OnSuccessListener<Void> onSuccessListener, @Nullable OnFailedListener onFailedListener) {
        mRemoteRoomDataManager.setRoom(key, room, onSuccessListener, onFailedListener);
    }

    @Override
    public void getRoom(@NonNull String key, @Nullable OnSuccessListener<Room> onSuccessListener, @Nullable OnFailedListener onFailedListener) {
        mRemoteRoomDataManager.getRoom(key, onSuccessListener, onFailedListener);
    }

    @Override
    public void setFavoriteRoom(RoomEntity roomEntity) {
        mLocalRoomDataManager.setFavoriteRoom(roomEntity);
    }

    @Override
    public void deleteFavoriteRoom(RoomEntity roomEntity) {
        mLocalRoomDataManager.deleteFavoriteRoom(roomEntity);
    }

    @Override
    public LiveData<List<RoomEntity>> getFavoriteRoom() {
        return mLocalRoomDataManager.getFavoriteRoom();
    }
}
