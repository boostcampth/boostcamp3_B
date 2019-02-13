package com.swsnack.catchhouse.data.db.room;

import androidx.annotation.NonNull;


import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.pojo.Room;

import java.util.List;

public interface RoomDataManager {

    void createKey(@NonNull OnSuccessListener<String> onSuccessListener,
                   @NonNull OnFailedListener onFailedListener);

    void uploadRoomImage(@NonNull String uuid, @NonNull List<byte[]> imageList,
                         @NonNull OnSuccessListener<List<String>> onSuccessListener,
                         @NonNull OnFailedListener onFailedListener);

    void uploadRoomData(@NonNull String uuid, @NonNull Room room,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener);

    void readRoomData(@NonNull String uuid,
                      @NonNull OnSuccessListener<Room> onSuccessListener,
                      @NonNull OnFailedListener onFailedListener);
}
