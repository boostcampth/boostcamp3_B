package com.swsnack.catchhouse.data;

import android.net.Uri;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.Constant;
import com.swsnack.catchhouse.data.db.chatting.remote.RemoteChattingManager;
import com.swsnack.catchhouse.data.db.location.remote.AppLocationDataManager;
import com.swsnack.catchhouse.data.db.room.RoomRepository;
import com.swsnack.catchhouse.data.db.searching.remote.AppSearchingDataManager;
import com.swsnack.catchhouse.data.db.user.remote.AppUserDataManager;
import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.model.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.swsnack.catchhouse.Constant.ExceptionReason.NOT_SIGNED_USER;
import static com.swsnack.catchhouse.Constant.FacebookData.GENDER;
import static com.swsnack.catchhouse.Constant.FacebookData.NAME;

public class APIManager {

    private static APIManager INSTANCE;
    private DataManager mDataManager;

    public static synchronized APIManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new APIManager(AppDataManager.getInstance(AppUserDataManager.getInstance(),
                    RemoteChattingManager.getInstance(),
                    RoomRepository.getInstance(),
                    AppLocationDataManager.getInstance(),
                    AppSearchingDataManager.getInstance()));
        }
        return INSTANCE;
    }

    private APIManager(DataManager dataManager) {
        mDataManager = dataManager;
    }

    private void signInWithApi(@NonNull AuthCredential authCredential,
                               @NonNull OnSuccessListener<AuthResult> onSuccessListener,
                               @NonNull OnFailedListener onFailedListener) {

        FirebaseAuth.getInstance()
                .signInWithCredential(authCredential)
                .addOnSuccessListener(onSuccessListener::onSuccess)
                .addOnFailureListener(onFailedListener::onFailed);
    }

    private void signUpWithEmail(@NonNull String email,
                                 @NonNull String password,
                                 @NonNull OnSuccessListener<AuthResult> onSuccessListener,
                                 @NonNull OnFailedListener onFailedListener) {

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(onSuccessListener::onSuccess)
                .addOnFailureListener(onFailedListener::onFailed);
    }

    public void firebaseSignUp(@NonNull AuthCredential authCredential,
                               @NonNull User user,
                               @NonNull OnSuccessListener<Void> onSuccessListener,
                               @NonNull OnFailedListener onFailedListener) {

        signInWithApi(authCredential,
                signUpSuccess ->
                        mDataManager.setUserNotAlreadySigned(signUpSuccess.getUser().getUid(),
                                user, onSuccessListener, onFailedListener)
                , onFailedListener);
    }

    public void firebaseSignUp(@NonNull String email,
                               @NonNull String password,
                               @NonNull User user,
                               @Nullable Uri uri,
                               @NonNull OnSuccessListener<Void> onSuccessListener,
                               @NonNull OnFailedListener onFailedListener) {

        signUpWithEmail(email,
                password,
                authResult -> {
                    if (uri == null) {
                        mDataManager.setUser(authResult.getUser().getUid(), user, onSuccessListener, onFailedListener);
                        return;
                    }
                    mDataManager.setUser(authResult.getUser().getUid(), user, uri, onSuccessListener, onFailedListener);
                },
                onFailedListener);
    }

    public void firebaseSignIn(@NonNull String email,
                               @NonNull String password,
                               @NonNull OnSuccessListener<AuthResult> onSuccessListener,
                               @NonNull OnFailedListener onFailedListener) {

        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(onSuccessListener::onSuccess)
                .addOnFailureListener(onFailedListener::onFailed);
    }

    private void deleteUser(@NonNull OnSuccessListener<Void> onSuccessListener,
                            @NonNull OnFailedListener onFailedListener) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailedListener.onFailed(new FirebaseException(NOT_SIGNED_USER));
            return;
        }

        FirebaseAuth.getInstance().getCurrentUser()
                .delete()
                .addOnSuccessListener(result -> {
                    FirebaseAuth.getInstance().signOut();
                    onSuccessListener.onSuccess(null);
                })
                .addOnFailureListener(onFailedListener::onFailed);
    }

    public void firebaseDeleteUser(@NonNull String uuid,
                                   @NonNull OnSuccessListener<Void> onSuccessListener,
                                   @NonNull OnFailedListener onFailedListener) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailedListener.onFailed(new FirebaseException(NOT_SIGNED_USER));
            return;
        }

        mDataManager.cancelMessageModelObserving();
        mDataManager.cancelObservingChattingList();
        mDataManager.deleteRecentRoomList();
        mDataManager.deleteFavoriteRoom();

        mDataManager.deleteUser(uuid,
                deleteUserSuccess -> deleteUser(onSuccessListener, onFailedListener),
                onFailedListener);
    }

    public void firebaseSignOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        FirebaseAuth.getInstance().signOut();
        mDataManager.cancelMessageModelObserving();
        mDataManager.cancelObservingChattingList();
        mDataManager.deleteRecentRoomList();
    }

    public void updatePassword(@NonNull String oldPassword,
                               @NonNull String newPassword,
                               @NonNull OnSuccessListener<Void> onSuccessListener,
                               @NonNull OnFailedListener onFailedListener) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailedListener.onFailed(new FirebaseException(NOT_SIGNED_USER));
            return;
        }
        FirebaseAuth.getInstance().getCurrentUser()
                .reauthenticate(EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(), oldPassword))
                .addOnSuccessListener(result -> FirebaseAuth.getInstance().getCurrentUser()
                        .updatePassword(newPassword)
                        .addOnSuccessListener(onSuccessListener::onSuccess)
                        .addOnFailureListener(onFailedListener::onFailed))
                .addOnFailureListener(onFailedListener::onFailed);
    }

    public void getUserInfoFromFacebook(@NonNull AccessToken accessToken,
                                        @NonNull OnSuccessListener<User> onSuccessListener,
                                        @NonNull OnFailedListener onFailedListener) {
        GraphRequest request = GraphRequest
                .newMeRequest(accessToken,
                        (result, response) -> {
                            if (result == null) {
                                onFailedListener.onFailed(new RuntimeException());
                                return;
                            }
                            onSuccessListener.onSuccess(new User(result.optString(NAME), result.optString(GENDER)));
                        });
        Bundle parameter = new Bundle();
        parameter.putString(Constant.FacebookData.KEY, Constant.FacebookData.VALUE);
        request.setParameters(parameter);
        request.executeAsync();
    }
}