package com.swsnack.catchhouse.data;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.request.RequestListener;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageException;
import com.swsnack.catchhouse.data.userdata.APIManager;
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;

import java.util.Map;

import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.DELETE_EXCEPTION;
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

    public void signUpAndSetUser(@NonNull String password, @NonNull User user, @Nullable byte[] profile,
                                 @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        firebaseSignUp(user.getEMail(), password, signUpSuccess -> {
            if (profile == null) {
                setUser(signUpSuccess.getUser().getUid(), user, onSuccessListener, onFailureListener);
                return;
            }
            setProfile(signUpSuccess.getUser().getUid(), profile, uri -> {
                user.setProfile(uri.toString());
                setUser(signUpSuccess.getUser().getUid(), user, onSuccessListener, onFailureListener);
            }, onFailureListener);
        }, onFailureListener);
    }

    @Override
    public void signUpAndSetUser(@NonNull AuthCredential authCredential, @NonNull User user, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        firebaseSignUp(authCredential, signUpSuccess ->
                        setUser(signUpSuccess.getUser().getUid(), user, onSuccessListener,
                                error -> {
                                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                        FirebaseAuth.getInstance().getCurrentUser().delete();
                                    }
                                })
                , onFailureListener);
    }

    @Override
    public void deleteUserAll(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailureListener.onFailure(new FirebaseException(NOT_SIGNED_USER));
            return;
        }
        // FIXME callback이 4개가 중첩되어있는 콜백지옥 구조인데 이런방식은 아주 좋지 않습니다. 개선할 방법을 고민하셔서 간결한 코드로 수정해주세요
        getUserForSingle(uuid, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    onFailureListener.onFailure(new FirebaseException(NOT_SIGNED_USER));
                    return;
                }

                deleteUserData(uuid,
                        deleteDBSuccess -> {
                            if (user.getProfile() == null) {
                                firebaseDeleteUser(onSuccessListener,
                                        error -> {
                                            onFailureListener.onFailure(new FirebaseException(DELETE_EXCEPTION));
                                            setUser(uuid, user, null, null);
                                        });
                                return;
                            }
                            deleteProfile(uuid,
                                    deleteProfileSuccess ->
                                            firebaseDeleteUser(onSuccessListener,
                                                    error -> {
                                                        onFailureListener.onFailure(new FirebaseException(DELETE_EXCEPTION));
                                                        setUser(uuid, user, null, null);
                                                    })
                                    , error -> {
                                        if (error instanceof StorageException) {
                                            firebaseDeleteUser(onSuccessListener, onFailureListener);
                                            return;
                                        }
                                        onFailureListener.onFailure(new FirebaseException(DELETE_EXCEPTION));
                                        setUser(uuid, user, null, null);
                                    });
                        }, onFailureListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailureListener.onFailure(databaseError.toException());
            }
        });
    }

    @Override
    public void updateProfile(@NonNull String uuid, @NonNull Uri uri, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.updateProfile(uuid, uri, onSuccessListener, onFailureListener);
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
    public void getUserForListening(@NonNull String uuid, @NonNull ValueEventListener valueEventListener) {
        mUserDataManager.getUserForListening(uuid, valueEventListener);
    }

    @Override
    public void getUserForSingle(@NonNull String uuid, @NonNull ValueEventListener valueEventListener) {
        mUserDataManager.getUserForSingle(uuid, valueEventListener);
    }

    @Override
    public void setUser(@NonNull String uuid, @NonNull User user, @Nullable OnSuccessListener<Void> onSuccessListener, @Nullable OnFailureListener onFailureListener) {
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
    public void queryUserBy(@NonNull String queryBy, @NonNull String findValue, @NonNull ValueEventListener valueEventListener) {
        mUserDataManager.queryUserBy(queryBy, findValue, valueEventListener);
    }

    @Override
    public void updateUser(@NonNull String uuid, @NonNull Map<String, Object> updateFields, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.updateUser(uuid, updateFields, onSuccessListener, onFailureListener);
    }

    @Override
    public void updateNickName(@NonNull String uuid, @NonNull String changeNickName, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.updateNickName(uuid, changeNickName, onSuccessListener, onFailureListener);
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