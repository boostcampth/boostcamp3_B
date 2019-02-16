package com.swsnack.catchhouse.data.db.room;

import com.swsnack.catchhouse.data.db.room.local.AppFavoriteRoomDataManager;
import com.swsnack.catchhouse.data.db.room.local.AppRecentRoomManager;
import com.swsnack.catchhouse.data.db.room.local.FavoriteRoomManager;
import com.swsnack.catchhouse.data.db.room.local.RecentRoomManager;
import com.swsnack.catchhouse.data.db.room.remote.AppRoomRemoteDataManager;
import com.swsnack.catchhouse.data.db.room.remote.RoomDataManager;
import com.swsnack.catchhouse.data.entity.RoomEntity;
import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RoomRepository implements RoomDataManager, FavoriteRoomManager, RecentRoomManager {

    private static RoomRepository INSTANCE;
    private FavoriteRoomManager mLocalRoomDataManager;
    private RoomDataManager mRemoteRoomDataManager;
    private RecentRoomManager mRecentRoomDataManager;

    public static RoomRepository getInstance() {
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
        mRecentRoomDataManager = AppRecentRoomManager.getInstance();
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
    public List<RoomEntity> getFavoriteRoomList() {
        return mLocalRoomDataManager.getFavoriteRoomList();
    }

    @Override
    public RoomEntity getFavoriteRoom(String key) {
        return mLocalRoomDataManager.getFavoriteRoom(key);
    }

    @Override
    public void setRecentRoom(Room room) {
        mRecentRoomDataManager.setRecentRoom(room);
    }

    @Override
    public List<Room> getRecentRoom() {
        return mRecentRoomDataManager.getRecentRoom();
    }
}
