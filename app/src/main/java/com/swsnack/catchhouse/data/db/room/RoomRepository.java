package com.swsnack.catchhouse.data.db.room;

import com.swsnack.catchhouse.data.db.room.local.AppRoomLocalDataManager;
import com.swsnack.catchhouse.data.db.room.remote.AppRoomDataManager;
import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.pojo.Room;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RoomRepository implements RoomDataManager {

    private RoomDataManager mLocalRoomDataManager;
    private RoomDataManager mRemoteRoomDataManager;

    public RoomRepository(RoomDataManager localRoomDataManager, RoomDataManager RemoteRoomDataManager) {

        mLocalRoomDataManager = AppRoomLocalDataManager.getInstance();
        mRemoteRoomDataManager = AppRoomDataManager.getInstance();
    }

    @Override
    public void createKey(@NonNull OnSuccessListener<String> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        mRemoteRoomDataManager.createKey(onSuccessListener, onFailedListener);
    }

    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<byte[]> imageList, @NonNull OnSuccessListener<List<String>> onSuccessListener, @NonNull OnFailedListener onFailedListener) {

    }

    @Override
    public void setRoom(@NonNull String key, @NonNull Room room, @Nullable OnSuccessListener<Void> onSuccessListener, @Nullable OnFailedListener onFailedListener) {

    }

    @Override
    public void getRoom(@NonNull String key, @Nullable OnSuccessListener<Room> onSuccessListener, @Nullable OnFailedListener onFailedListener) {

    }

}
