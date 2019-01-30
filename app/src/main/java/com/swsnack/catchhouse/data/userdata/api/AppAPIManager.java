package com.swsnack.catchhouse.data.userdata.api;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.constants.Constants;
import com.swsnack.catchhouse.data.userdata.APIManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AppAPIManager implements APIManager {

    private static class SINGLETON {
        private static AppAPIManager INSTANCE = new AppAPIManager();
    }

    private AppAPIManager() {
    }

    public static AppAPIManager getInstance() {
        return SINGLETON.INSTANCE;
    }

    @Override
    public Single<String> firebaseSignUp(AuthCredential authCredential) {
        return Single.defer(() ->
                Single.create(subscriber ->
                        FirebaseAuth.getInstance().signInWithCredential(authCredential)
                                .addOnSuccessListener(authResult -> subscriber.onSuccess(authResult.getUser().getUid()))
                                .addOnFailureListener(subscriber::onError)));
    }

    @Override
    public Single<String> firebaseSignUp(String email, String password) {
        return Single.defer(() ->
                Single.create(subscriber ->
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                .addOnSuccessListener(authResult -> subscriber.onSuccess(authResult.getUser().getUid()))
                                .addOnFailureListener(subscriber::onError)
                ));
    }

    @Override
    public Single<User> facebookUserProfile(AccessToken token) {
        return Single.defer(() ->
                Single.create(subscriber -> {
                    GraphRequest request = GraphRequest.newMeRequest(token, (object, response) -> {
                        String name = object.optString(Constants.FacebookData.NAME);
                        String gender = object.optString(Constants.FacebookData.GENDER);
                        subscriber.onSuccess(new User(name, gender));
                    });
                    Bundle parameter = new Bundle();
                    parameter.putString(Constants.FacebookData.KEY, Constants.FacebookData.VALUE);
                    request.setParameters(parameter);
                    request.executeAsync();
                }));
    }

    @Override
    public Completable firebaseSignIn(String email, String password) {
        return Completable.defer(() ->
                Completable.create(subscriber ->
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener(authResult -> subscriber.onComplete())
                                .addOnFailureListener(subscriber::onError)));
    }

    @Override
    public Completable firebaseDeleteUser() {
        return Completable.defer(() ->
                Completable.create(subscriber ->
                        FirebaseAuth.getInstance().getCurrentUser()
                                .delete()
                                .addOnSuccessListener(authResult -> subscriber.onComplete())
                                .addOnFailureListener(subscriber::onError)));
    }
}
