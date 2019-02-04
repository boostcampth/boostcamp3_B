package com.swsnack.catchhouse.data.userdata;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.bumptech.glide.request.RequestListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.userdata.pojo.User;

import java.util.Map;

public interface UserDataManager {

    void getUser(@NonNull String uuid, @NonNull ValueEventListener valueEventListener);

    void setProfile(@NonNull String uuid, @NonNull byte[] profile, @NonNull OnSuccessListener<Uri> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void getProfile(@NonNull Uri uri, RequestListener<Bitmap> requestListener);

    void setUser(@NonNull String uuid, @NonNull User user, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void deleteUserData(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void deleteProfile(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void deleteUser(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void queryUserBy(@NonNull String queryBy, @NonNull String findValue, @NonNull ValueEventListener valueEventListener);

    void updateUser(@NonNull String uuid, @NonNull Map<String, Object> updateFields, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

}