package com.swsnack.catchhouse.repository.room;

import android.net.Uri;

import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;

import java.util.List;

import androidx.annotation.NonNull;

public interface RoomRepository {

    String createKey();

    void uploadRoomImage(@NonNull String uuid, @NonNull List<Uri> imageList, @NonNull OnSuccessListener<List<String>> onSuccessListener, @NonNull OnFailedListener onFailedListener);

    void setRoom(@NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener);

    void getRoom(@NonNull String key, @NonNull OnSuccessListener<Room> onSuccessListener, @NonNull OnFailedListener onFailedListener);

    void deleteRoom(@NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener);

    void updateRoom(@NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener);

}
