package com.swsnack.catchhouse.data;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.userdata.APIManager;
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;

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

    @Override
    public void firebaseSignUp(@NonNull AuthCredential authCredential, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mApiManager.firebaseSignUp(authCredential, onSuccessListener, onFailureListener);
    }

    @Override
    public void firebaseSignUp(@NonNull String email, @NonNull String password, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mApiManager.firebaseSignUp(email, password, onSuccessListener, onFailureListener);
    }

    @Override
    public void firebaseSignIn(@NonNull String email, @NonNull String password, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mApiManager.firebaseSignIn(email, password, onSuccessListener, onFailureListener);
    }

    @Override
    public void firebaseDeleteUser(@NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mApiManager.firebaseDeleteUser(onSuccessListener, onFailureListener);
    }

    @Override
    public void facebookUserProfile(@NonNull AccessToken accessToken, @NonNull GraphRequest.GraphJSONObjectCallback facebookUserDataCallback) {
        mApiManager.facebookUserProfile(accessToken, facebookUserDataCallback);
    }

    @Override
    public void getUser(@NonNull String uuid, @NonNull ValueEventListener valueEventListener) {
        mUserDataManager.getUser(uuid, valueEventListener);
    }

    @Override
    public void setUser(@NonNull String uuid, @NonNull User user, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.setUser(uuid, user, onSuccessListener, onFailureListener);
    }

    @Override
    public void deleteUserData(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.deleteUserData(uuid, onSuccessListener, onFailureListener);
    }

    @Override
    public void deleteProfile(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.deleteProfile(uuid, onSuccessListener, onFailureListener);
    }

    @Override
    public void deleteUser(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.deleteUser(uuid, onSuccessListener, onFailureListener);
    }

    @Override
    public void setProfile(@NonNull String uuid, @NonNull byte[] profile, @NonNull OnSuccessListener<Uri> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.setProfile(uuid, profile, onSuccessListener, onFailureListener);
    }
}