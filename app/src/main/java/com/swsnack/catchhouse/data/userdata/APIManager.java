package com.swsnack.catchhouse.data.userdata;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.swsnack.catchhouse.data.userdata.pojo.User;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface APIManager {

    Single<String> firebaseSignUp(AuthCredential authCredential);

    Single<String> firebaseSignUp(String email, String password);

    Single<User> facebookUserProfile(AccessToken accessToken);

    Completable firebaseSignIn(String email, String password);

    Completable firebaseDeleteUser();

}
