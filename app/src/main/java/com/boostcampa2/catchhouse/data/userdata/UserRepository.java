package com.boostcampa2.catchhouse.data.userdata;

import android.support.annotation.NonNull;

import com.boostcampa2.catchhouse.data.userdata.pojo.User;
import com.boostcampa2.catchhouse.data.userdata.remote.UserRemoteData;

import io.reactivex.Completable;
import io.reactivex.Single;

public class UserRepository {

    private UserRemoteData mUserRemote;

    public static UserRepository getInstance() {
        return RepositoryHelper.INSTANCE;
    }

    private static class RepositoryHelper {
        private static final UserRepository INSTANCE = new UserRepository();
    }

    private UserRepository() {
        mUserRemote = UserRemoteData.getInstance();
    }

    @NonNull
    public Single<User> getUserFromRemote(String uuid) {
        return mUserRemote.getUser(uuid);
    }

    @NonNull
    public Completable setUserToRemote(User user) {
        return mUserRemote.setUser(user);
    }
}
