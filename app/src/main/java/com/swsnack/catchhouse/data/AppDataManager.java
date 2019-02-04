package com.swsnack.catchhouse.data;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.userdata.APIManager;
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;
import com.swsnack.catchhouse.util.DataConverter;

import java.io.IOException;
import java.util.Map;

import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.FAILED_LOAD_IMAGE;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.NOT_SIGNED_USER;

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
    public void updateProfile(@NonNull String uuid, @NonNull Uri uri, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        Log.d("프로필 변경 4.데이터 매니저", "시작");
        mUserDataManager.getUser(uuid, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("프로필 변경 5.데이터 매니저 스냅샷 받음", dataSnapshot.getKey());
                if (user == null) {
                    Log.d("프로필 변경 6. 유저 널 아닙ㅁ", user.getNickName());
                    onFailureListener.onFailure(new DatabaseException(NOT_SIGNED_USER));
                    return;
                }
                mUserDataManager.getProfile(uri, new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        onFailureListener.onFailure(new GlideException(FAILED_LOAD_IMAGE));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("프로필 변경 7.uri에서 bitmap", uri.toString());
                        try {
                            mUserDataManager.setProfile(uuid, DataConverter.getByteArray(DataConverter.getScaledBitmap(resource)), uri -> {
                                user.setProfile(uri.toString());
                                mUserDataManager.setUser(uuid, user, onSuccessListener, onFailureListener);
                            }, onFailureListener);
                        } catch (IOException e) {
                            e.printStackTrace();
                            onFailureListener.onFailure(new GlideException(FAILED_LOAD_IMAGE));
                        }
                        return true;
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailureListener.onFailure(new DatabaseException(NOT_SIGNED_USER));
            }
        });
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
    public void firebaseUpdatePassword(@NonNull String oldPassword, @NonNull String newPassword, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mApiManager.firebaseUpdatePassword(oldPassword, newPassword, onSuccessListener, onFailureListener);
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
    public void queryUserBy(@NonNull String queryBy, @NonNull String findValue, @NonNull ValueEventListener valueEventListener) {
        mUserDataManager.queryUserBy(queryBy, findValue, valueEventListener);
    }

    @Override
    public void updateUser(@NonNull String uuid, @NonNull Map<String, Object> updateFields, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.updateUser(uuid, updateFields, onSuccessListener, onFailureListener);
    }

    @Override
    public void setProfile(@NonNull String uuid, @NonNull byte[] profile, @NonNull OnSuccessListener<Uri> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.setProfile(uuid, profile, onSuccessListener, onFailureListener);
    }

    @Override
    public void getProfile(@NonNull Uri uri, RequestListener<Bitmap> requestListener) {
        mUserDataManager.getProfile(uri, requestListener);
    }
}