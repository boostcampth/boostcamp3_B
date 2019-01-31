package com.swsnack.catchhouse.data.userdata;

import android.support.annotation.NonNull;

import com.swsnack.catchhouse.data.userdata.pojo.User;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserDataManager {

    @NonNull
    Single<User> getUser(@NonNull String uuid);

    @NonNull
    Single<String> setProfile(@NonNull String uuid, @NonNull byte[] profile);

    @NonNull
    Completable setUser(@NonNull String uuid, @NonNull User user);

    @NonNull
    Completable deleteUser(@NonNull String uuid);


}
