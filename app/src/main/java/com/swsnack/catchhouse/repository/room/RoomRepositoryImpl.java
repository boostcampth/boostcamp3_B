package com.swsnack.catchhouse.repository.room;

import android.net.Uri;

import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;
import com.swsnack.catchhouse.repository.room.local.SellRoomData;
import com.swsnack.catchhouse.repository.room.remote.RemoteRoomData;

import java.util.List;

import androidx.annotation.NonNull;

public class RoomRepositoryImpl implements RoomRepository {

    private static RoomRepositoryImpl INSTANCE;
    private RemoteRoomData mRemoteRoom;
    private SellRoomData mLocalRoom;


    public static RoomRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (RoomRepositoryImpl.class) {
                INSTANCE = new RoomRepositoryImpl();
            }
        }
        return INSTANCE;
    }

    private RoomRepositoryImpl() {
        mRemoteRoom = RemoteRoomData.getInstance();
        mLocalRoom = SellRoomData.getInstance();
    }


    @Override
    public String createKey() {
        return mRemoteRoom.createKey();
    }

    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<Uri> imageList, @NonNull OnSuccessListener<List<String>> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        mRemoteRoom.uploadRoomImage(uuid, imageList, onSuccessListener, onFailedListener);
    }

    @Override
    public void setRoom(@NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        mRemoteRoom.setRoom(room, success -> {
            mLocalRoom.setSellRoom(room);
            onSuccessListener.onSuccess(success);
        }, onFailedListener);
    }

    @Override
    public void getRoom(@NonNull String key, @NonNull OnSuccessListener<Room> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        mRemoteRoom.getRoom(key, onSuccessListener, onFailedListener);
    }

    @Override
    public void deleteRoom(@NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        mRemoteRoom.delete(room, success -> {
            mLocalRoom.deleteSellRoom(room);
            onSuccessListener.onSuccess(success);
        }, onFailedListener);

    }

    @Override
    public void updateRoom(@NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        updateRoom(room, success -> {
            mLocalRoom.updateRoom(room);
            onSuccessListener.onSuccess(success);
        }, onFailedListener);
    }
}
