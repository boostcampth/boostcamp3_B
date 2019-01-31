package com.swsnack.catchhouse.data.userdata;

import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.swsnack.catchhouse.data.userdata.pojo.User;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface APIManager {

    @NonNull
    Single<String> firebaseSignUp(@NonNull AuthCredential authCredential);

    @NonNull
    Single<String> firebaseSignUp(@NonNull String email, @NonNull String password);

    @NonNull
    Single<User> facebookUserProfile(@NonNull AccessToken accessToken);

    @NonNull
    Completable firebaseSignIn(@NonNull String email, @NonNull String password);

    @NonNull
    Completable firebaseDeleteUser(@NonNull String uuid);

    @NonNull
    Completable firebaseDeleteStorage(@NonNull String uuid);

}
