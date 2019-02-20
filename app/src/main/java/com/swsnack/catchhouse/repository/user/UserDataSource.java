package com.swsnack.catchhouse.repository.user;

import android.graphics.Bitmap;
import android.net.Uri;

import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;
import com.swsnack.catchhouse.data.model.User;

import androidx.annotation.NonNull;

public interface UserDataSource {

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

    void deleteUser(@NonNull String uuid,
                    @NonNull OnSuccessListener<Void> onSuccessListener,
                    @NonNull OnFailedListener onFailedListener);

    void getProfile(@NonNull Uri uri,
                    @NonNull OnSuccessListener<Bitmap> onSuccessListener,
                    @NonNull OnFailedListener onFailedListener);

}