package com.swsnack.catchhouse.data.db.room;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.swsnack.catchhouse.data.pojo.Room;

import java.util.List;

public interface RoomDataManager {

    void createKey(@NonNull OnSuccessListener<String> onSuccessListener,
                   @NonNull OnFailureListener onFailureListener);

    void uploadRoomImage(@NonNull String uuid, @NonNull List<byte[]> imageList,
                         @NonNull OnSuccessListener<List<String>> onSuccessListener,
                         @NonNull OnFailureListener onFailureListener);

    void uploadRoomData(@NonNull String uuid, @NonNull Room room,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailureListener onFailureListener);

}
