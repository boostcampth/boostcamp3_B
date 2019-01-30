package com.swsnack.catchhouse.data.userdata;

import android.support.annotation.NonNull;

import com.swsnack.catchhouse.data.userdata.pojo.User;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserDataManager {

    @NonNull
    Single<User> getUser(String uuid);

    @NonNull
    Completable setUser(String uuid, User user);

    @NonNull
    Single<String> setProfile(String uuid, byte[] profile);

}
