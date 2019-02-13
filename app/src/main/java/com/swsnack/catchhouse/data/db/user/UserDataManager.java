package com.swsnack.catchhouse.data.db.user;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.model.User;

public interface UserDataManager {

    void getUserAndListeningForChanging(@NonNull String uuid,
                                        @NonNull OnSuccessListener<User> onSuccessListener,
                                        @NonNull OnFailedListener onFailedListener);

    void getUserFromSingleSnapShot(@NonNull String uuid,
                                   @NonNull OnSuccessListener<User> onSuccessListener,
                                   @NonNull OnFailedListener onFailedListener);

    void setUser(@NonNull String uuid,
                 @NonNull User user,
                 @NonNull OnSuccessListener<Void> onSuccessListener,
                 @NonNull OnFailedListener onFailedListener);

    void setUser(@NonNull String uuid,
                 @NonNull User user,
                 @NonNull Uri uri,
                 @NonNull OnSuccessListener<Void> onSuccessListener,
                 @NonNull OnFailedListener onFailedListener);

    void setUserNotAlreadySigned(@NonNull String uuid,
                                 @NonNull User user,
                                 @NonNull OnSuccessListener<Void> onSuccessListener,
                                 @NonNull OnFailedListener onFailedListener);

    void updateUser(@NonNull String uuid,
                    @NonNull User user,
                    @NonNull OnSuccessListener<Void> onSuccessListener,
                    @NonNull OnFailedListener onFailedListener);

    void updateProfile(@NonNull String uuid,
                       @NonNull Uri uri,
                       @NonNull User user,
                       @NonNull OnSuccessListener<Void> onSuccessListener,
                       @NonNull OnFailedListener onFailedListener);

    void deleteUserData(@NonNull String uuid,
                        @NonNull User user,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener);

    void deleteProfile(@NonNull String uuid,
                       @NonNull OnSuccessListener<Void> onSuccessListener,
                       @NonNull OnFailedListener onFailedListener);

    void getProfile(@NonNull Uri uri,
                    @NonNull OnSuccessListener<Bitmap> onSuccessListener,
                    @NonNull OnFailedListener onFailedListener);

    void findUserByQueryString(@NonNull String queryString,
                               @NonNull String findValue,
                               @NonNull OnSuccessListener<String> onSuccessListener,
                               @NonNull OnFailedListener onFailedListener);
}