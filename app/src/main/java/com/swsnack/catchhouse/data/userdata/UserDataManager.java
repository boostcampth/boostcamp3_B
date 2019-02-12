package com.swsnack.catchhouse.data.userdata;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.swsnack.catchhouse.data.roomsdata.pojo.Room;
import com.swsnack.catchhouse.data.userdata.model.User;

import java.util.List;

public interface UserDataManager {

    void getUserAndListeningForChanging(@NonNull String uuid,
                                        @NonNull OnSuccessListener<User> onSuccessListener,
                                        @NonNull OnFailureListener onFailureListener);

    void getUserFromSingleSnapShot(@NonNull String uuid,
                                   @NonNull OnSuccessListener<User> onSuccessListener,
                                   @NonNull OnFailureListener onFailureListener);

    void setUser(@NonNull String uuid,
                 @NonNull User user,
                 @NonNull OnSuccessListener<Void> onSuccessListener,
                 @NonNull OnFailureListener onFailureListener);

    void setUser(@NonNull String uuid,
                 @NonNull User user,
                 @NonNull Uri uri,
                 @NonNull OnSuccessListener<Void> onSuccessListener,
                 @NonNull OnFailureListener onFailureListener);

    void updateUser(@NonNull String uuid,
                    @NonNull User user,
                    @NonNull OnSuccessListener<Void> onSuccessListener,
                    @NonNull OnFailureListener onFailureListener);

    void updateProfile(@NonNull String uuid,
                       @NonNull Uri uri,
                       @NonNull User user,
                       @NonNull OnSuccessListener<Void> onSuccessListener,
                       @NonNull OnFailureListener onFailureListener);

    void deleteUserData(@NonNull String uuid,
                        @NonNull User user,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailureListener onFailureListener);

    void deleteProfile(@NonNull String uuid,
                       @NonNull OnSuccessListener<Void> onSuccessListener,
                       @NonNull OnFailureListener onFailureListener);

    void getProfile(@NonNull Uri uri,
                    @NonNull OnSuccessListener<Bitmap> onSuccessListener,
                    @NonNull OnFailureListener onFailureListener);

    void createKey(@NonNull OnSuccessListener<String> onSuccessListener,
                   @NonNull OnFailureListener onFailureListener);

    void uploadRoomImage(@NonNull String uuid,
                         @NonNull List<byte[]> imageList,
                         @NonNull OnSuccessListener<List<String>> onSuccessListener,
                         @NonNull OnFailureListener onFailureListener);

    void uploadRoomData(@NonNull String uuid,
                        @NonNull Room room,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailureListener onFailureListener);

    void findUserByQueryString(@NonNull String queryString,
                               @NonNull String findValue,
                               @NonNull OnSuccessListener<String> onSuccessListener,
                               @NonNull OnFailureListener onFailureListener);

}