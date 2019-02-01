package com.swsnack.catchhouse.data.userdata;

import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;

public interface APIManager {

    void firebaseSignUp(@NonNull AuthCredential authCredential, @NonNull OnSuccessListener onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void firebaseSignUp(@NonNull String email, @NonNull String password, @NonNull OnSuccessListener onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void facebookUserProfile(@NonNull AccessToken accessToken, GraphRequest.GraphJSONObjectCallback facebookUserDataCallback);

    void firebaseSignIn(@NonNull String email, @NonNull String password, @NonNull OnSuccessListener onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void firebaseDeleteUser(@NonNull OnSuccessListener onSuccessListener, @NonNull OnFailureListener onFailureListener);

}
