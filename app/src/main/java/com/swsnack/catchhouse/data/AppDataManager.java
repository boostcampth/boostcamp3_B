package com.swsnack.catchhouse.data;

import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.swsnack.catchhouse.data.userdata.APIManager;
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.api.AppAPIManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;
import com.swsnack.catchhouse.data.userdata.remote.UserRemoteData;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AppDataManager implements DataManager {

    private APIManager mApiManager;
    private UserDataManager mUserDataManager;

    private AppDataManager() {
        mApiManager = AppAPIManager.getInstance();
        mUserDataManager = UserRemoteData.getInstance();
    }

    private static class SINGLETON {
        private static AppDataManager INSTANCE = new AppDataManager();
    }

    public static AppDataManager getInstance() {
        return SINGLETON.INSTANCE;
    }

    @Override
    public APIManager getAPIManager() {
        return mApiManager;
    }

    @Override
    public UserDataManager getUserDataManager() {
        return mUserDataManager;
    }

    @Override
    public Single<String> firebaseSignUp(AuthCredential authCredential) {
        return mApiManager.firebaseSignUp(authCredential);
    }

    @Override
    public Single<String> firebaseSignUp(String email, String password) {
        return mApiManager.firebaseSignUp(email, password);
    }

    @Override
    public Completable firebaseSignIn(String email, String password) {
        return mApiManager.firebaseSignIn(email, password);
    }

    @Override
    public Completable firebaseDeleteUser() {
        return mApiManager.firebaseDeleteUser();
    }

    @Override
    public Single<User> facebookUserProfile(AccessToken accessToken) {
        return mApiManager.facebookUserProfile(accessToken);
    }

    @NonNull
    @Override
    public Single<User> getUser(String uuid) {
        return mUserDataManager.getUser(uuid);
    }

    @NonNull
    @Override
    public Completable setUser(String uuid, User user) {
        return mUserDataManager.setUser(uuid, user);
    }

    @NonNull
    @Override
    public Single<String> setProfile(String uuid, byte[] profile) {
        return mUserDataManager.setProfile(uuid, profile);
    }
}