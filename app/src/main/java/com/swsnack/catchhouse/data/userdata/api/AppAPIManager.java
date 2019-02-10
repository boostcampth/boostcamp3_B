package com.swsnack.catchhouse.data.userdata.api;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.constants.Constants;
import com.swsnack.catchhouse.data.userdata.APIManager;

import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.NOT_SIGNED_USER;

public class AppAPIManager implements APIManager {

    private static class Singleton {
        private static AppAPIManager INSTANCE = new AppAPIManager();
    }

    private AppAPIManager() {
    }

    public static AppAPIManager getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void signUp(@NonNull AuthCredential authCredential, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        FirebaseAuth.getInstance()
                .signInWithCredential(authCredential)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void signUp(@NonNull String email, @NonNull String password, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void getUserInfoFromFacebook(@NonNull AccessToken token, GraphRequest.GraphJSONObjectCallback facebookUserDataCallback) {
        GraphRequest request = GraphRequest.newMeRequest(token, facebookUserDataCallback);
        Bundle parameter = new Bundle();
        parameter.putString(Constants.FacebookData.KEY, Constants.FacebookData.VALUE);
        request.setParameters(parameter);
        request.executeAsync();
    }

    @Override
    public void signIn(@NonNull String email, @NonNull String password, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void deleteUser(@NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailureListener.onFailure(new FirebaseException(NOT_SIGNED_USER));
            return;
        }
        FirebaseAuth.getInstance().getCurrentUser()
                .delete()
                .addOnSuccessListener(result -> {
                    FirebaseAuth.getInstance().signOut();
                    onSuccessListener.onSuccess(null);
                })
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void updatePassword(@NonNull String oldPassword, @NonNull String newPassword, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailureListener.onFailure(new FirebaseException(NOT_SIGNED_USER));
            return;
        }
        FirebaseAuth.getInstance().getCurrentUser()
                .reauthenticate(EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(), oldPassword))
                .addOnSuccessListener(result -> FirebaseAuth.getInstance().getCurrentUser()
                        .updatePassword(newPassword)
                        .addOnSuccessListener(onSuccessListener)
                        .addOnFailureListener(onFailureListener))
                .addOnFailureListener(onFailureListener);

    }
}
