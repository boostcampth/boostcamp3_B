package com.swsnack.catchhouse.data.userdata.api;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.constants.Constants;
import com.swsnack.catchhouse.data.userdata.APIManager;

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
    public void firebaseSignUp(@NonNull AuthCredential authCredential, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        FirebaseAuth.getInstance().signInWithCredential(authCredential)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void firebaseSignUp(@NonNull String email, @NonNull String password, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void facebookUserProfile(@NonNull AccessToken token, GraphRequest.GraphJSONObjectCallback facebookUserDataCallback) {
        GraphRequest request = GraphRequest.newMeRequest(token, facebookUserDataCallback);
        Bundle parameter = new Bundle();
        parameter.putString(Constants.FacebookData.KEY, Constants.FacebookData.VALUE);
        request.setParameters(parameter);
        request.executeAsync();
    }

    @Override
    public void firebaseSignIn(@NonNull String email, @NonNull String password, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void firebaseDeleteUser(@NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailureListener.onFailure(new FirebaseException("user is not signIn"));
            return;
        }
        FirebaseAuth.getInstance().getCurrentUser()
                .delete()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
}
