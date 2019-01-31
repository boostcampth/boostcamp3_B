package com.swsnack.catchhouse.data;

import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.swsnack.catchhouse.data.userdata.APIManager;
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AppDataManager implements DataManager {

    private APIManager mApiManager;
    private UserDataManager mUserDataManager;

    private AppDataManager(APIManager apiManager, UserDataManager userDataManager) {
        mApiManager = apiManager;
        mUserDataManager = userDataManager;
    }

    private static AppDataManager INSTANCE;

    public static synchronized AppDataManager getInstance(@NonNull APIManager apiManager, @NonNull UserDataManager userDataManager) {
        if (INSTANCE == null) {
            INSTANCE = new AppDataManager(apiManager, userDataManager);
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public APIManager getAPIManager() {
        return mApiManager;
    }

    @NonNull
    @Override
    public UserDataManager getUserDataManager() {
        return mUserDataManager;
    }

    @NonNull
    @Override
    public Single<String> firebaseSignUp(@NonNull AuthCredential authCredential) {
        return mApiManager.firebaseSignUp(authCredential);
    }

    @NonNull
    @Override
    public Single<String> firebaseSignUp(@NonNull String email, @NonNull String password) {
        return mApiManager.firebaseSignUp(email, password);
    }

    @NonNull
    @Override
    public Completable firebaseSignIn(@NonNull String email, @NonNull String password) {
        return mApiManager.firebaseSignIn(email, password);
    }

    @NonNull
    @Override
    public Completable firebaseDeleteUser(@NonNull String uuid) {
        return mApiManager.firebaseDeleteUser(uuid)
                .andThen(deleteUser(uuid))
                .andThen(firebaseDeleteStorage(uuid));
    }

    @NonNull
    @Override
    public Completable firebaseDeleteStorage(@NonNull String uuid) {
        return mApiManager.firebaseDeleteStorage(uuid);
    }

    @NonNull
    @Override
    public Single<User> facebookUserProfile(@NonNull AccessToken accessToken) {
        return mApiManager.facebookUserProfile(accessToken);
    }

    @NonNull
    @Override
    public Single<User> getUser(@NonNull String uuid) {
        return mUserDataManager.getUser(uuid);
    }

    @NonNull
    @Override
    public Completable setUser(@NonNull String uuid, @NonNull User user) {
        return mUserDataManager.setUser(uuid, user);
    }

    @NonNull
    @Override
    public Completable deleteUser(@NonNull String uuid) {
        return mApiManager.firebaseDeleteUser(uuid)
                .andThen(mUserDataManager.deleteUser(uuid));
    }

    @NonNull
    @Override
    public Single<String> setProfile(@NonNull String uuid, @NonNull byte[] profile) {
        return mUserDataManager.setProfile(uuid, profile);
    }
}