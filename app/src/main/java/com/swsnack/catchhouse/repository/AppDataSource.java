package com.swsnack.catchhouse.repository;

import android.graphics.Bitmap;
import android.net.Uri;

import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.repository.room.local.LocalSellRoomDataSource;
import com.swsnack.catchhouse.repository.room.local.SellRoomImpl;
import com.swsnack.catchhouse.data.entity.SellRoomEntity;
import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.data.model.Filter;
import com.swsnack.catchhouse.data.model.Message;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.data.model.User;
import com.swsnack.catchhouse.repository.chatting.ChattingDataSource;
import com.swsnack.catchhouse.repository.location.LocationDataSource;
import com.swsnack.catchhouse.repository.room.RoomRepository;
import com.swsnack.catchhouse.repository.room.local.FavoriteRoomDataSource;
import com.swsnack.catchhouse.repository.room.local.RecentRoomDataSource;
import com.swsnack.catchhouse.repository.room.remote.RoomDataSource;
import com.swsnack.catchhouse.repository.searching.SearchingDataSource;
import com.swsnack.catchhouse.repository.user.UserDataSource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Single;

public class AppDataSource implements DataSource {

    private UserDataSource mUserDataSource;
    private ChattingDataSource mRemoteChattingDataSource;
    private RoomDataSource mRoomDataSource;
    private FavoriteRoomDataSource mFavoriteRoomDataSource;
    private RecentRoomDataSource mRecentRoomDataManager;
    private LocationDataSource mLocationDataSource;
    private SearchingDataSource mSearchingDataSource;
    private LocalSellRoomDataSource mSellRoomDataManager;

    private AppDataSource(UserDataSource userDataSource,
                          ChattingDataSource remoteChattingDataSource,
                          RoomRepository roomRepository,
                          LocationDataSource locationDataSource,
                          SearchingDataSource searchingDataSource) {

        mUserDataSource = userDataSource;
        mRemoteChattingDataSource = remoteChattingDataSource;
        mRoomDataSource = roomRepository;
        mFavoriteRoomDataSource = roomRepository;
        mRecentRoomDataManager = roomRepository;
        mLocationDataSource = locationDataSource;
        mSearchingDataSource = searchingDataSource;
        mSellRoomDataManager = SellRoomImpl.getInstance();
    }

    private static AppDataSource INSTANCE;

    public static synchronized AppDataSource getInstance(@NonNull UserDataSource userDataSource,
                                                         @NonNull ChattingDataSource remoteChattingDataSource,
                                                         @NonNull RoomRepository roomRepository,
                                                         @NonNull LocationDataSource locationDataSource,
                                                         @NonNull SearchingDataSource searchingDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new AppDataSource(userDataSource,
                    remoteChattingDataSource,
                    roomRepository,
                    locationDataSource,
                    searchingDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void updateProfile(@NonNull String uuid,
                              @NonNull Uri uri,
                              @NonNull User user,
                              @NonNull OnSuccessListener<Void> onSuccessListener,
                              @NonNull OnFailedListener onFailedListener) {

        mUserDataSource.updateProfile(uuid, uri, user, onSuccessListener, onFailedListener);
    }

    @Override
    public void deleteUser(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        mUserDataSource.deleteUser(uuid, onSuccessListener, onFailedListener);
    }

    @Override
    public void getUserAndListeningForChanging(@NonNull String uuid,
                                               @NonNull OnSuccessListener<User> onSuccessListener,
                                               @NonNull OnFailedListener onFailedListener) {
        mUserDataSource.getUserAndListeningForChanging(uuid, onSuccessListener, onFailedListener);
    }

    @Override
    public void getUserFromSingleSnapShot(@NonNull String uuid,
                                          @NonNull OnSuccessListener<User> onSuccessListener,
                                          @NonNull OnFailedListener onFailedListener) {
        mUserDataSource.getUserFromSingleSnapShot(uuid, onSuccessListener, onFailedListener);
    }

    @Override
    public void setUser(@NonNull String uuid,
                        @NonNull User user,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener) {

        mUserDataSource.setUser(uuid, user, onSuccessListener, onFailedListener);
    }

    @Override
    public void setUser(@NonNull String uuid,
                        @NonNull User user,
                        @NonNull Uri uri,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener) {

        mUserDataSource.setUser(uuid, user, uri, onSuccessListener, onFailedListener);
    }

    @Override
    public void setUserNotAlreadySigned(@NonNull String uuid,
                                        @NonNull User user,
                                        @NonNull OnSuccessListener<Void> onSuccessListener,
                                        @NonNull OnFailedListener onFailedListener) {

        mUserDataSource.setUserNotAlreadySigned(uuid, user, onSuccessListener, onFailedListener);
    }

    @Override
    public void updateUser(@NonNull String uuid,
                           @NonNull User user,
                           @NonNull OnSuccessListener<Void> onSuccessListener,
                           @NonNull OnFailedListener onFailedListener) {

        mUserDataSource.updateUser(uuid, user, onSuccessListener, onFailedListener);
    }

    @Override
    public void getProfile(@NonNull Uri uri,
                           @NonNull OnSuccessListener<Bitmap> onSuccessListener,
                           @NonNull OnFailedListener onFailedListener) {

        mUserDataSource.getProfile(uri, onSuccessListener, onFailedListener);
    }

    @Override
    public void getChattingRoom(@NonNull String destinationUuid,
                                @NonNull OnSuccessListener onSuccessListener,
                                @NonNull OnFailedListener onFailedListener) {

        mRemoteChattingDataSource.getChattingRoom(destinationUuid, onSuccessListener, onFailedListener);
    }

    @Override
    public void listeningChattingListChanged(@NonNull OnSuccessListener onSuccessListener,
                                             @NonNull OnFailedListener onFailedListener) {

        mRemoteChattingDataSource.listeningChattingListChanged(onSuccessListener, onFailedListener);
    }

    @Override
    public void cancelObservingChattingList() {
        mRemoteChattingDataSource.cancelObservingChattingList();
    }

    @Override
    public void listeningChatMessageChanged(@NonNull String chatRoomId,
                                            @NonNull OnSuccessListener<List<Message>> onSuccessListener,
                                            OnFailedListener onFailedListener) {

        mRemoteChattingDataSource.listeningChatMessageChanged(chatRoomId, onSuccessListener, onFailedListener);

    }

    @Override
    public void cancelMessageModelObserving() {
        mRemoteChattingDataSource.cancelMessageModelObserving();
    }

    @Override
    public void setChattingRoom(@NonNull String destinationUuid,
                                @NonNull OnSuccessListener<String> onSuccessListener,
                                @NonNull OnFailedListener onFailedListener) {

        mRemoteChattingDataSource.setChattingRoom(destinationUuid, onSuccessListener, onFailedListener);
    }

    @Override
    public void setChatMessage(int messagesLength,
                               @Nullable String roomUid,
                               @NonNull String destinationUid,
                               @NonNull String content,
                               @NonNull OnSuccessListener<String> onSuccessListener,
                               @NonNull OnFailedListener onFailedListener) {

        mRemoteChattingDataSource.setChatMessage(messagesLength, roomUid, destinationUid, content, onSuccessListener, onFailedListener);

    }

    @Override
    public String createKey() {
        return mRoomDataSource.createKey();
    }

    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<Uri> imageList,
                                @NonNull OnSuccessListener<List<String>> onSuccessListener,
                                @NonNull OnFailedListener onFailedListener) {

        mRoomDataSource.uploadRoomImage(uuid, imageList, onSuccessListener, onFailedListener);
    }

    @Override
    public void setRoom(@NonNull String key, @NonNull Room room,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener) {

        mRoomDataSource.setRoom(key, room, onSuccessListener, onFailedListener);
    }


    @Override
    public void getRoom(@NonNull String key,
                        @NonNull OnSuccessListener<Room> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener) {
        mRoomDataSource.getRoom(key, onSuccessListener, onFailedListener);
    }

    @Override
    public void uploadLocationData(@NonNull String uuid, @NonNull Address address,
                                   @NonNull OnSuccessListener<String> onSuccessListener,
                                   @NonNull OnFailedListener onFailedListener) {

        mLocationDataSource.uploadLocationData(uuid, address, onSuccessListener, onFailedListener);
    }

    @NonNull
    public Single<List<TMapPOIItem>> getPOIList(@NonNull String keyword) {
        return mSearchingDataSource.getPOIList(keyword);
    }

    @NonNull
    public Single<List<Room>> getNearRoomList(@NonNull Filter filter) {
        return mSearchingDataSource.getNearRoomList(filter);
    }

    @Override
    public void setFavoriteRoom(Room room) {
        mFavoriteRoomDataSource.setFavoriteRoom(room);
    }

    @Override
    public void deleteFavoriteRoom(Room room) {
        mFavoriteRoomDataSource.deleteFavoriteRoom(room);

    }

    @Override
    public void deleteFavoriteRoom() {
        mFavoriteRoomDataSource.deleteFavoriteRoom();
    }

    @Override
    public List<Room> getFavoriteRoomList() {
        return mFavoriteRoomDataSource.getFavoriteRoomList();
    }

    @Override
    public Room getFavoriteRoom(String key) {
        return mFavoriteRoomDataSource.getFavoriteRoom(key);
    }

    @Override
    public void updateRoom(Room room) {
        mFavoriteRoomDataSource.updateRoom(room);
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
        mSellRoomDataManager.setSellRoom(sellRoomEntity);
    }

    @Override
    public void deleteSellRoom(SellRoomEntity sellRoomEntity) {
        mSellRoomDataManager.deleteSellRoom(sellRoomEntity);
    }

    @Override
    public List<SellRoomEntity> getSellRoomList() {
        return mSellRoomDataManager.getSellRoomList();
    }

    @Override
    public void deleteSellRoom() {
        mSellRoomDataManager.deleteSellRoom();
    }

    @Override
    public SellRoomEntity getSellRoom(String key) {
        return mSellRoomDataManager.getSellRoom(key);
    }

    @Override
    public void updateRoom(SellRoomEntity sellRoomEntity) {
        mSellRoomDataManager.updateRoom(sellRoomEntity);
    }
}