package com.swsnack.catchhouse.data.db.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


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

    void setRoom(@NonNull String key, @NonNull Room room,
                 @Nullable OnSuccessListener<Void> onSuccessListener,
                 @Nullable OnFailedListener onFailedListener);

    void getRoom(@NonNull String key,
                 @Nullable OnSuccessListener<Room> onSuccessListener,
                 @Nullable OnFailedListener onFailedListener);
}
