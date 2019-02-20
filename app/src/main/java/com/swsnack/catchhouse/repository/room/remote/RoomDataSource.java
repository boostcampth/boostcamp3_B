package com.swsnack.catchhouse.repository.room.remote;

import android.net.Uri;

import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface RoomDataSource {

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
