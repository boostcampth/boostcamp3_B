package com.boostcampa2.catchhouse.data.userdata;

import android.support.annotation.NonNull;

import com.boostcampa2.catchhouse.data.userdata.pojo.User;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserDataSource {

    @NonNull
    Single<User> getUser(String uuid);

    @NonNull
    Completable setUser(User user);

}
