package com.swsnack.catchhouse.data;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.chattingdata.ChattingManager;
import com.swsnack.catchhouse.data.chattingdata.pojo.Chatting;
import com.swsnack.catchhouse.data.chattingdata.pojo.Message;
import com.swsnack.catchhouse.data.roomsdata.pojo.Room;
import com.swsnack.catchhouse.data.userdata.APIManager;
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;

import java.util.List;
import java.util.Map;

import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.NOT_SIGNED_USER;

public class AppDataManager implements DataManager {

    private APIManager mApiManager;
    private UserDataManager mUserDataManager;
    private ChattingManager mRemoteChattingManager;

    private AppDataManager(APIManager apiManager, UserDataManager userDataManager, ChattingManager remoteChattingManager) {
        mApiManager = apiManager;
        mUserDataManager = userDataManager;
        mRemoteChattingManager = remoteChattingManager;
    }

    private static AppDataManager INSTANCE;

    public static synchronized AppDataManager getInstance(@NonNull APIManager apiManager,
                                                          @NonNull UserDataManager userDataManager,
                                                          @NonNull ChattingManager remoteChattingManager) {
        if (INSTANCE == null) {
            INSTANCE = new AppDataManager(apiManager, userDataManager, remoteChattingManager);
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

    public void signUpAndSetUser(@NonNull String password,
                                 @NonNull User user,
                                 @Nullable Uri uri,
                                 @NonNull OnSuccessListener<Void> onSuccessListener,
                                 @NonNull OnFailureListener onFailureListener) {
        signUp(user.getEMail(), password,
                signUpSuccess -> {
                    if (uri == null) {
                        setUser(signUpSuccess.getUser().getUid(), user, onSuccessListener, onFailureListener);
                        return;
                    }
                    uploadProfile(signUpSuccess.getUser().getUid(),
                            uri,
                            storageUri -> {
                                user.setProfile(storageUri.toString());
                                setUser(signUpSuccess.getUser().getUid(), user, onSuccessListener, onFailureListener);
                            }, onFailureListener);
                },
                onFailureListener);
    }

    @Override
    public void signUpAndSetUser(@NonNull AuthCredential authCredential,
                                 @NonNull User user,
                                 @NonNull OnSuccessListener<Void> onSuccessListener,
                                 @NonNull OnFailureListener onFailureListener) {
        signUp(authCredential,
                signUpSuccess ->
                        getUserFromSingleSnapShot(signUpSuccess.getUser().getUid(),
                                signed -> onSuccessListener.onSuccess(null),
                                error -> {
                                    if (error.getMessage().equals(NOT_SIGNED_USER)) {
                                        setUser(signUpSuccess.getUser().getUid(), user, onSuccessListener, onFailureListener);
                                        return;
                                    }
                                    onFailureListener.onFailure(error);
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
        getUserFromSingleSnapShot(uuid,
                user ->
                        deleteUserData(uuid,
                                deleteDB -> {
                                    if (user.getProfile() == null) {
                                        deleteUser(onSuccessListener,
                                                error -> {
                                                    onFailureListener.onFailure(error);
                                                    setUser(uuid, user,
                                                            Void -> {
                                                            },
                                                            transactionError -> {
                                                            });
                                                });
                                        return;
                                    }
                                    deleteProfile(uuid,
                                            deleteProfile ->
                                                    deleteUser(onSuccessListener, onFailureListener),
                                            error -> {
                                                onFailureListener.onFailure(error);
                                                setUser(uuid, user,
                                                        Void -> {
                                                        },
                                                        transactionError -> {
                                                        });
                                            });
                                },
                                onFailureListener),
                onFailureListener);
    }

    @Override
    public void updateProfile(@NonNull String uuid, @NonNull Uri uri, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.updateProfile(uuid, uri, onSuccessListener, onFailureListener);
    }

    @Override
    public void signUp(@NonNull AuthCredential authCredential, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mApiManager.signUp(authCredential, onSuccessListener, onFailureListener);
    }

    @Override
    public void signUp(@NonNull String email, @NonNull String password, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mApiManager.signUp(email, password, onSuccessListener, onFailureListener);
    }

    @Override
    public void signIn(@NonNull String email, @NonNull String password, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mApiManager.signIn(email, password, onSuccessListener, onFailureListener);
    }

    @Override
    public void deleteUser(@NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mApiManager.deleteUser(onSuccessListener, onFailureListener);
    }

    @Override
    public void updatePassword(@NonNull String oldPassword, @NonNull String newPassword, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mApiManager.updatePassword(oldPassword, newPassword, onSuccessListener, onFailureListener);
    }

    @Override
    public void getUserInfoFromFacebook(@NonNull AccessToken accessToken, @NonNull OnSuccessListener<User> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mApiManager.getUserInfoFromFacebook(accessToken, onSuccessListener, onFailureListener);
    }

    @Override
    public void getUserAndListeningForChanging(@NonNull String uuid,
                                               @NonNull OnSuccessListener<User> onSuccessListener,
                                               @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.getUserAndListeningForChanging(uuid, onSuccessListener, onFailureListener);
    }

    @Override
    public void getUserFromSingleSnapShot(@NonNull String uuid,
                                          @NonNull OnSuccessListener<User> onSuccessListener,
                                          @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.getUserFromSingleSnapShot(uuid, onSuccessListener, onFailureListener);
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
    public void findUserByQueryString(@NonNull String queryString,
                                      @NonNull String findValue,
                                      @NonNull OnSuccessListener<String> onSuccessListener,
                                      @NonNull OnFailureListener onFailureListener) {

        mUserDataManager.findUserByQueryString(queryString, findValue, onSuccessListener, onFailureListener);
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
    public void uploadProfile(@NonNull String uuid, @NonNull Uri imageUri, @NonNull OnSuccessListener<Uri> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.uploadProfile(uuid, imageUri, onSuccessListener, onFailureListener);
    }

    @Override
    public void getProfile(@NonNull Uri uri, @NonNull OnSuccessListener<Bitmap> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.getProfile(uri, onSuccessListener, onFailureListener);
    }

    @Override
    public void getChattingRoom(@NonNull String uuid, @NonNull String destinationUuid, @NonNull OnSuccessListener<String> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mRemoteChattingManager.getChattingRoom(uuid, destinationUuid, onSuccessListener, onFailureListener);
    }

    @Override
    public void getChattingList(@NonNull String uuid, @NonNull OnSuccessListener<List<Chatting>> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mRemoteChattingManager.getChattingList(uuid, onSuccessListener, onFailureListener);
    }

    @Override
    public void getChatMessage(@NonNull String chatRoomId, @NonNull ValueEventListener valueEventListener) {

    }

    @Override
    public void setChattingRoom(@NonNull Chatting chattingUser, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        mRemoteChattingManager.setChattingRoom(chattingUser, onSuccessListener, onFailureListener);
    }

    @Override
    public void setChatMessage(@NonNull Message message, @NonNull ValueEventListener valueEventListener) {

    }

    @Override
    public void createKey(@NonNull OnSuccessListener<String> onSuccessListener,
                          @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.createKey(onSuccessListener, onFailureListener);
    }

    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<byte[]> imageList,
                                @NonNull OnSuccessListener<List<String>> onSuccessListener,
                                @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.uploadRoomImage(uuid, imageList, onSuccessListener, onFailureListener);
    }

    @Override
    public void uploadRoomData(@NonNull String uuid, @NonNull Room room,
                               @NonNull OnSuccessListener<Void> onSuccessListener,
                               @NonNull OnFailureListener onFailureListener) {
        mUserDataManager.uploadRoomData(uuid, room, onSuccessListener, onFailureListener);
    }
}