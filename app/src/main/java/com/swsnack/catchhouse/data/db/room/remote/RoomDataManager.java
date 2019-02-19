package com.swsnack.catchhouse.data.db.room.remote;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

public interface RoomDataManager {

    String createKey();

    void uploadRoomImage(@NonNull String uuid, @NonNull List<Uri> imageList,
                         @NonNull OnSuccessListener<List<String>> onSuccessListener,
                         @NonNull OnFailedListener onFailedListener);

    void setRoom(@NonNull String key, @NonNull Room room,
                 @Nullable OnSuccessListener<Void> onSuccessListener,
                 @Nullable OnFailedListener onFailedListener);

    void getRoom(@NonNull String key,
                 @NonNull OnSuccessListener<Room> onSuccessListener,
                 @NonNull OnFailedListener onFailedListener);
}
