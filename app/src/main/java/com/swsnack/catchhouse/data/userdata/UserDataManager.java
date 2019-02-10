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
import com.swsnack.catchhouse.data.userdata.pojo.User;

import java.util.Map;

public interface UserDataManager {

    void getUserAndListeningForChanging(@NonNull String uuid,
                                        @NonNull OnSuccessListener<User> onSuccessListener,
                                        @NonNull OnFailureListener onFailureListener);

    void getUserFromSingleSnapShot(@NonNull String uuid,
                                   @NonNull OnSuccessListener<User> onSuccessListener,
                                   @NonNull OnFailureListener onFailureListener);

    void setUser(@NonNull String uuid, @NonNull User user, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void updateUser(@NonNull String uuid, @NonNull Map<String, Object> updateFields, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void updateNickName(@NonNull String uuid, @NonNull String changeNickName, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void deleteUserData(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void uploadProfile(@NonNull String uuid, @NonNull Uri imageUri, @NonNull OnSuccessListener<Uri> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void getProfile(@NonNull Uri uri, @NonNull OnSuccessListener<Bitmap> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void updateProfile(@NonNull String uuid, @NonNull Uri uri, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void deleteProfile(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void createKey(@NonNull OnSuccessListener<String> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void uploadRoomImage(@NonNull String uuid, @NonNull List<byte[]> imageList, @NonNull OnSuccessListener<List<String>> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void uploadRoomData(@NonNull String uuid, @NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void findUserByQueryString(@NonNull String queryString,
                               @NonNull String findValue,
                               @NonNull OnSuccessListener<String> onSuccessListener,
                               @NonNull OnFailureListener onFailureListener);

}