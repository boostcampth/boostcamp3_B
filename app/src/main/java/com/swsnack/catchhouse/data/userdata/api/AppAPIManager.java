package com.swsnack.catchhouse.data.userdata.api;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.swsnack.catchhouse.constants.Constants;
import com.swsnack.catchhouse.data.userdata.APIManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;

import io.reactivex.Completable;
import io.reactivex.Single;

import static com.swsnack.catchhouse.constants.Constants.FirebaseKey.STORAGE_PROFILE;

public class AppAPIManager implements APIManager {

    private static class SINGLETON {
        private static AppAPIManager INSTANCE = new AppAPIManager();
    }

    private AppAPIManager() {
    }

    public static AppAPIManager getInstance() {
        return SINGLETON.INSTANCE;
    }

    @NonNull
    @Override
    public Single<String> firebaseSignUp(@NonNull AuthCredential authCredential) {
        return Single.defer(() ->
                Single.create(subscriber ->
                        FirebaseAuth.getInstance().signInWithCredential(authCredential)
                                .addOnSuccessListener(authResult -> subscriber.onSuccess(authResult.getUser().getUid()))
                                .addOnFailureListener(subscriber::onError)));
    }

    @NonNull
    @Override
    public Single<String> firebaseSignUp(@NonNull String email, @NonNull String password) {
        return Single.defer(() ->
                Single.create(subscriber ->
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                .addOnSuccessListener(authResult -> subscriber.onSuccess(authResult.getUser().getUid()))
                                .addOnFailureListener(subscriber::onError)
                ));
    }

    @NonNull
    @Override
    public Single<User> facebookUserProfile(@NonNull AccessToken token) {
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

    @NonNull
    @Override
    public Completable firebaseSignIn(@NonNull String email, @NonNull String password) {
        return Completable.defer(() ->
                Completable.create(subscriber ->
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener(authResult -> subscriber.onComplete())
                                .addOnFailureListener(subscriber::onError)));
    }

    @NonNull
    @Override
    public Completable firebaseDeleteUser(@NonNull String uuid) {
        return Completable.defer(() ->
                Completable.create(subscriber ->
                        FirebaseAuth.getInstance().getCurrentUser()
                                .delete()
                                .addOnSuccessListener(authResult -> subscriber.onComplete())
                                .addOnFailureListener(subscriber::onError)));
    }

    @NonNull
    @Override
    public Completable firebaseDeleteStorage(@NonNull String uuid) {
        return Completable.defer(() ->
                Completable.create(subscriber -> FirebaseStorage.getInstance()
                        .getReference(STORAGE_PROFILE)
                        .child(uuid)
                        .delete()
                        .addOnSuccessListener(result -> subscriber.onComplete())
                        .addOnFailureListener(subscriber::onError)));
    }
}
