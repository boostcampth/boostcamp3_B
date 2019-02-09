package com.swsnack.catchhouse.data.userdata;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.request.RequestListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.roomsdata.pojo.Room;
import com.swsnack.catchhouse.data.userdata.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDataManager {

    void getUserForListening(@NonNull String uuid, @NonNull ValueEventListener valueEventListener);

    void getUserForSingle(@NonNull String uuid, @NonNull ValueEventListener valueEventListener);

    void setUser(@NonNull String uuid, @NonNull User user, @Nullable OnSuccessListener<Void> onSuccessListener, @Nullable OnFailureListener onFailureListener);

    void updateUser(@NonNull String uuid, @NonNull Map<String, Object> updateFields, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void updateNickName(@NonNull String uuid, @NonNull String changeNickName, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void deleteUserData(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void setProfile(@NonNull String uuid, @NonNull byte[] profile, @NonNull OnSuccessListener<Uri> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void getProfile(@NonNull Uri uri, RequestListener<Bitmap> requestListener);

    void updateProfile(@NonNull String uuid, @NonNull Uri uri, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void deleteProfile(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void queryUserBy(@NonNull String queryBy, @NonNull String findValue, @NonNull ValueEventListener valueEventListener);

    void createKey(@NonNull OnSuccessListener<String> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void uploadRoomImage(@NonNull String uuid, @NonNull List<byte[]> imageList, @NonNull OnSuccessListener<List<String>> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void uploadRoomData(@NonNull String uuid, @NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);
}