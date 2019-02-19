package com.swsnack.catchhouse.data;

import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.data.db.chatting.ChattingManager;
import com.swsnack.catchhouse.data.db.room.RoomRepository;
import com.swsnack.catchhouse.data.db.room.local.LocalRoomDataSource;
import com.swsnack.catchhouse.data.db.room.local.LocalSellRoomDataSource;
import com.swsnack.catchhouse.data.db.room.local.RecentRoomManager;
import com.swsnack.catchhouse.data.db.room.local.SellRoomImpl;
import com.swsnack.catchhouse.data.db.searching.SearchingDataManager;
import com.swsnack.catchhouse.data.entity.RoomEntity;
import com.swsnack.catchhouse.data.entity.SellRoomEntity;
import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.data.model.Message;
import com.swsnack.catchhouse.data.db.location.LocationDataManager;
import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.db.room.remote.RoomDataManager;
import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.data.model.Filter;
import com.swsnack.catchhouse.data.db.user.UserDataManager;
import com.swsnack.catchhouse.data.model.User;

import java.util.List;

import io.reactivex.Single;

public class AppDataManager implements DataManager {

    private UserDataManager mUserDataManager;
    private ChattingManager mRemoteChattingManager;
    private RoomDataManager mRoomDataManager;
    private LocalRoomDataSource mLocalRoomDataSource;
    private RecentRoomManager mRecentRoomDataManager;
    private LocationDataManager mLocationDataManager;
    private SearchingDataManager mSearchingDataManager;
    private LocalSellRoomDataSource mLocalSellRoomManager;

    private AppDataManager(UserDataManager userDataManager,
                           ChattingManager remoteChattingManager,
                           RoomRepository roomRepository,
                           LocationDataManager locationDataManager,
                           SearchingDataManager searchingDataManager) {

        mUserDataManager = userDataManager;
        mRemoteChattingManager = remoteChattingManager;
        mRoomDataManager = roomRepository;
        mLocalRoomDataSource = roomRepository;
        mRecentRoomDataManager = roomRepository;
        mLocationDataManager = locationDataManager;
        mSearchingDataManager = searchingDataManager;
        mLocalSellRoomManager = SellRoomImpl.getInstance();
    }

    private static AppDataManager INSTANCE;

    public static synchronized AppDataManager getInstance(@NonNull UserDataManager userDataManager,
                                                          @NonNull ChattingManager remoteChattingManager,
                                                          @NonNull RoomRepository roomRepository,
                                                          @NonNull LocationDataManager locationDataManager,
                                                          @NonNull SearchingDataManager searchingDataManager) {
        if (INSTANCE == null) {
            INSTANCE = new AppDataManager(userDataManager,
                    remoteChattingManager,
                    roomRepository,
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
    public void deleteUser(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        mUserDataManager.deleteUser(uuid, onSuccessListener, onFailedListener);
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
    public void listeningChattingListChanged(@NonNull com.google.android.gms.tasks.OnSuccessListener<List<Chatting>> onSuccessListener,
                                             @NonNull OnFailureListener onFailedListener) {

        mRemoteChattingManager.listeningChattingListChanged(onSuccessListener, onFailedListener);
    }

    @Override
    public void cancelObservingChattingList() {
        mRemoteChattingManager.cancelObservingChattingList();
    }

    @Override
    public void listeningChatMessageChanged(@NonNull String chatRoomId,
                                            @NonNull com.google.android.gms.tasks.OnSuccessListener<List<Message>> onSuccessListener,
                                            @NonNull OnFailureListener onFailedListener) {

        mRemoteChattingManager.listeningChatMessageChanged(chatRoomId, onSuccessListener, onFailedListener);

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
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<Uri> imageList,
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
    public Single<List<Room>> getNearRoomList(@NonNull Filter filter) {
        return mSearchingDataManager.getNearRoomList(filter);
    }

    @Override
    public void setFavoriteRoom(RoomEntity roomEntity) {
        mLocalRoomDataSource.setFavoriteRoom(roomEntity);
    }

    @Override
    public void deleteFavoriteRoom(RoomEntity roomEntity) {
        mLocalRoomDataSource.deleteFavoriteRoom(roomEntity);

    }

    @Override
    public void deleteFavoriteRoom() {
        mLocalRoomDataSource.deleteFavoriteRoom();
    }

    @Override
    public List<RoomEntity> getFavoriteRoomList() {
        return mLocalRoomDataSource.getFavoriteRoomList();
    }

    @Override
    public RoomEntity getFavoriteRoom(String key) {
        return mLocalRoomDataSource.getFavoriteRoom(key);
    }

    @Override
    public void updateRoom(RoomEntity roomEntity) {
        mLocalRoomDataSource.updateRoom(roomEntity);
    }

    @Override
    public void setRecentRoom(Room room) {
        mRecentRoomDataManager.setRecentRoom(room);
    }

    @Override
    public List<Room> getRecentRoom() {
        return mRecentRoomDataManager.getRecentRoom();
    }

    @Override
    public void deleteRecentRoomList() {
        mRecentRoomDataManager.deleteRecentRoomList();
    }

    @Override
    public void setSellRoom(SellRoomEntity sellRoomEntity) {
        mLocalSellRoomManager.setSellRoom(sellRoomEntity);
    }

    @Override
    public void deleteSellRoom(SellRoomEntity sellRoomEntity) {
        mLocalSellRoomManager.deleteSellRoom(sellRoomEntity);
    }

    @Override
    public List<SellRoomEntity> getSellRoomList() {
        return mLocalSellRoomManager.getSellRoomList();
    }

    @Override
    public void deleteSellRoom() {
        mLocalSellRoomManager.deleteSellRoom();
    }

    @Override
    public SellRoomEntity getSellRoom(String key) {
        return mLocalSellRoomManager.getSellRoom(key);
    }

    @Override
    public void updateRoom(SellRoomEntity sellRoomEntity) {
        mLocalSellRoomManager.updateRoom(sellRoomEntity);
    }
}