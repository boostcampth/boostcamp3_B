package com.swsnack.catchhouse.data;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.swsnack.catchhouse.data.chattingdata.ChattingManager;
import com.swsnack.catchhouse.data.userdata.APIManager;
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;

public interface DataManager extends APIManager, UserDataManager, ChattingManager {

    APIManager getAPIManager();

    UserDataManager getUserDataManager();

    void signUpAndSetUser(@NonNull String password,
                          @NonNull User user,
                          @Nullable Uri uri,
                          @NonNull OnSuccessListener<Void> onSuccessListener,
                          @NonNull OnFailureListener onFailureListener);

    void signUpAndSetUser(@NonNull AuthCredential credential,
                          @NonNull User user,
                          @NonNull OnSuccessListener<Void> onSuccessListener,
                          @NonNull OnFailureListener onFailureListener);

    void deleteUserAll(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);
}
