package com.swsnack.catchhouse.data;

import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.data.db.chatting.ChattingManager;
import com.swsnack.catchhouse.data.db.searching.SearchingDataManager;
import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.data.model.Message;
import com.swsnack.catchhouse.data.db.location.LocationDataManager;
import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.db.room.RoomDataManager;
import com.swsnack.catchhouse.data.pojo.Address;
import com.swsnack.catchhouse.data.pojo.Room;
import com.swsnack.catchhouse.data.db.user.UserDataManager;
import com.swsnack.catchhouse.data.model.User;
import com.swsnack.catchhouse.data.pojo.RoomData;

import java.util.List;

import io.reactivex.Single;

public class AppDataManager implements DataManager {

    private UserDataManager mUserDataManager;
    private ChattingManager mRemoteChattingManager;
    private RoomDataManager mRoomDataManager;
    private LocationDataManager mLocationDataManager;
    private SearchingDataManager mSearchingDataManager;

    private AppDataManager(UserDataManager userDataManager,
                           ChattingManager remoteChattingManager,
                           RoomDataManager roomDataManager,
                           LocationDataManager locationDataManager,
                           SearchingDataManager searchingDataManager) {

        mUserDataManager = userDataManager;
        mRemoteChattingManager = remoteChattingManager;
        mRoomDataManager = roomDataManager;
        mLocationDataManager = locationDataManager;
        mSearchingDataManager = searchingDataManager;
    }

    private static AppDataManager INSTANCE;

    public static synchronized AppDataManager getInstance(@NonNull UserDataManager userDataManager,
                                                          @NonNull ChattingManager remoteChattingManager,
                                                          @NonNull RoomDataManager roomDataManager,
                                                          @NonNull LocationDataManager locationDataManager,
                                                          @NonNull SearchingDataManager searchingDataManager) {
        if (INSTANCE == null) {
            INSTANCE = new AppDataManager(userDataManager,
                    remoteChattingManager,
                    roomDataManager,
                    locationDataManager,
                    searchingDataManager);
        }
        return INSTANCE;
    }

    @Override
    public void updateProfile(@NonNull String uuid,
                              @NonNull Uri uri,
                              @NonNull User user,
                              @NonNull OnSuccessListener<Void> onSuccessListener,
                              @NonNull OnFailedListener onFailedListener) {

        mUserDataManager.updateProfile(uuid, uri, user, onSuccessListener, onFailedListener);
    }

    @Override
    public void getUserAndListeningForChanging(@NonNull String uuid,
                                               @NonNull OnSuccessListener<User> onSuccessListener,
                                               @NonNull OnFailedListener onFailedListener) {
        mUserDataManager.getUserAndListeningForChanging(uuid, onSuccessListener, onFailedListener);
    }

    @Override
    public void getUserFromSingleSnapShot(@NonNull String uuid,
                                          @NonNull OnSuccessListener<User> onSuccessListener,
                                          @NonNull OnFailedListener onFailedListener) {
        mUserDataManager.getUserFromSingleSnapShot(uuid, onSuccessListener, onFailedListener);
    }

    @Override
    public void setUser(@NonNull String uuid,
                        @NonNull User user,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener) {

        mUserDataManager.setUser(uuid, user, onSuccessListener, onFailedListener);
    }

    @Override
    public void setUser(@NonNull String uuid,
                        @NonNull User user,
                        @NonNull Uri uri,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener) {

        mUserDataManager.setUser(uuid, user, uri, onSuccessListener, onFailedListener);
    }

    @Override
    public void setUserNotAlreadySigned(@NonNull String uuid,
                                        @NonNull User user,
                                        @NonNull com.swsnack.catchhouse.data.listener.OnSuccessListener<Void> onSuccessListener,
                                        @NonNull OnFailedListener onFailedListener) {

        mUserDataManager.setUserNotAlreadySigned(uuid, user, onSuccessListener, onFailedListener);
    }

    @Override
    public void deleteUserData(@NonNull String uuid,
                               @NonNull User user,
                               @NonNull OnSuccessListener<Void> onSuccessListener,
                               @NonNull OnFailedListener onFailedListener) {

        mUserDataManager.deleteUserData(uuid, user, onSuccessListener, onFailedListener);
    }

    @Override
    public void deleteProfile(@NonNull String uuid,
                              @NonNull OnSuccessListener<Void> onSuccessListener,
                              @NonNull OnFailedListener onFailedListener) {

        mUserDataManager.deleteProfile(uuid, onSuccessListener, onFailedListener);
    }

    @Override
    public void findUserByQueryString(@NonNull String queryString,
                                      @NonNull String findValue,
                                      @NonNull OnSuccessListener<String> onSuccessListener,
                                      @NonNull OnFailedListener onFailedListener) {

        mUserDataManager.findUserByQueryString(queryString, findValue, onSuccessListener, onFailedListener);
    }

    @Override
    public void updateUser(@NonNull String uuid,
                           @NonNull User user,
                           @NonNull OnSuccessListener<Void> onSuccessListener,
                           @NonNull OnFailedListener onFailedListener) {

        mUserDataManager.updateUser(uuid, user, onSuccessListener, onFailedListener);
    }

    @Override
    public void getProfile(@NonNull Uri uri,
                           @NonNull OnSuccessListener<Bitmap> onSuccessListener,
                           @NonNull OnFailedListener onFailedListener) {

        mUserDataManager.getProfile(uri, onSuccessListener, onFailedListener);
    }

    @Override
    public void getChattingRoom(@NonNull String destinationUuid,
                                @NonNull com.google.android.gms.tasks.OnSuccessListener<String> onSuccessListener,
                                @NonNull OnFailureListener onFailedListener) {

        mRemoteChattingManager.getChattingRoom(destinationUuid, onSuccessListener, onFailedListener);
    }

    @Override
    public void getChattingList(@NonNull com.google.android.gms.tasks.OnSuccessListener<List<Chatting>> onSuccessListener,
                                @NonNull OnFailureListener onFailedListener) {

        mRemoteChattingManager.getChattingList(onSuccessListener, onFailedListener);
    }

    @Override
    public void cancelChattingModelObserving() {
        mRemoteChattingManager.cancelChattingModelObserving();
    }

    @Override
    public void listeningForChangedChatMessage(@NonNull String chatRoomId,
                                               @NonNull com.google.android.gms.tasks.OnSuccessListener<List<Message>> onSuccessListener,
                                               @NonNull OnFailureListener onFailedListener) {

        mRemoteChattingManager.listeningForChangedChatMessage(chatRoomId, onSuccessListener, onFailedListener);

    }

    @Override
    public void cancelMessageModelObserving() {
        mRemoteChattingManager.cancelMessageModelObserving();
    }

    @Override
    public void setChattingRoom(@NonNull String destinationUuid,
                                @NonNull com.google.android.gms.tasks.OnSuccessListener<String> onSuccessListener,
                                @NonNull OnFailureListener onFailedListener) {

        mRemoteChattingManager.setChattingRoom(destinationUuid, onSuccessListener, onFailedListener);
    }

    @Override
    public void setChatMessage(int messagesLength,
                               @Nullable String roomUid,
                               @NonNull String destinationUid,
                               @NonNull String content,
                               @NonNull com.google.android.gms.tasks.OnSuccessListener<String> onSuccessListener,
                               @NonNull OnFailureListener onFailedListener) {

        mRemoteChattingManager.setChatMessage(messagesLength, roomUid, destinationUid, content, onSuccessListener, onFailedListener);

    }

    @Override
    public void createKey(@NonNull OnSuccessListener<String> onSuccessListener,
                          @NonNull OnFailedListener onFailedListener) {

        mRoomDataManager.createKey(onSuccessListener, onFailedListener);
    }

    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<byte[]> imageList,
                                @NonNull OnSuccessListener<List<String>> onSuccessListener,
                                @NonNull OnFailedListener onFailedListener) {

        mRoomDataManager.uploadRoomImage(uuid, imageList, onSuccessListener, onFailedListener);
    }

    @Override
    public void setRoom(@NonNull String key, @NonNull Room room,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener) {

        mRoomDataManager.setRoom(key, room, onSuccessListener, onFailedListener);
    }


    @Override
    public void getRoom(@NonNull String key,
                        @NonNull OnSuccessListener<Room> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener) {
        mRoomDataManager.getRoom(key, onSuccessListener, onFailedListener);
    }

    @Override
    public void uploadLocationData(@NonNull String uuid, @NonNull Address address,
                                   @NonNull OnSuccessListener<String> onSuccessListener,
                                   @NonNull OnFailedListener onFailedListener) {

        mLocationDataManager.uploadLocationData(uuid, address, onSuccessListener, onFailedListener);
    }

    @NonNull
    public Single<List<TMapPOIItem>> getPOIList(@NonNull String keyword) {
        return mSearchingDataManager.getPOIList(keyword);
    }

    @NonNull
    public Single<List<RoomData>> getNearRoomList(@NonNull double latitude,
                                                  @NonNull double longitude,
                                                  @NonNull double distance) {
        return mSearchingDataManager.getNearRoomList(latitude, longitude, distance);
    }

}