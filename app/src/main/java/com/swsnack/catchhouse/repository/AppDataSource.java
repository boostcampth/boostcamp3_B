package com.swsnack.catchhouse.repository;

import android.net.Uri;

import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.data.model.Filter;
import com.swsnack.catchhouse.data.model.Message;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.data.model.User;
import com.swsnack.catchhouse.repository.chatting.ChattingDataSource;
import com.swsnack.catchhouse.repository.chatting.remote.RemoteChattingImpl;
import com.swsnack.catchhouse.repository.location.LocationDataSource;
import com.swsnack.catchhouse.repository.location.remote.RemoteLocationImpl;
import com.swsnack.catchhouse.repository.room.RoomRepository;
import com.swsnack.catchhouse.repository.room.local.FavoriteRoomDataSource;
import com.swsnack.catchhouse.repository.room.local.RecentRoomDataSource;
import com.swsnack.catchhouse.repository.room.local.SellRoomDataSource;
import com.swsnack.catchhouse.repository.room.local.SellRoomImpl;
import com.swsnack.catchhouse.repository.room.remote.RemoteRoomDataSource;
import com.swsnack.catchhouse.repository.searching.SearchingDataSource;
import com.swsnack.catchhouse.repository.searching.remote.SearchingDataImpl;
import com.swsnack.catchhouse.repository.user.UserDataSource;
import com.swsnack.catchhouse.repository.user.remote.UserDataImpl;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Single;

public class AppDataSource implements DataSource {
    //FIXME : 이 클래스 삭제할겁니다. 사용해주지 마시고 레포에서 써주세요
    private UserDataSource mUserDataSource;
    private ChattingDataSource mRemoteChattingDataSource;
    private RemoteRoomDataSource mRemoteRoomDataSource;
    private FavoriteRoomDataSource mFavoriteRoomDataSource;
    private RecentRoomDataSource mRecentRoomDataManager;
    private LocationDataSource mLocationDataSource;
    private SearchingDataSource mSearchingDataSource;
    private SellRoomDataSource mSellRoomDataSource;

    private AppDataSource() {

        mUserDataSource = UserDataImpl.getInstance();
        mRemoteChattingDataSource = RemoteChattingImpl.getInstance();
        mRemoteRoomDataSource = RoomRepository.getInstance();
        mFavoriteRoomDataSource = RoomRepository.getInstance();
        mRecentRoomDataManager = RoomRepository.getInstance();
        mLocationDataSource = RemoteLocationImpl.getInstance();
        mSearchingDataSource = SearchingDataImpl.getInstance();
        mSellRoomDataSource = SellRoomImpl.getInstance();
    }

    private static AppDataSource INSTANCE;

    public static synchronized AppDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppDataSource();
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
        return mRemoteRoomDataSource.createKey();
    }

    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<Uri> imageList,
                                @NonNull OnSuccessListener<List<String>> onSuccessListener,
                                @NonNull OnFailedListener onFailedListener) {

        mRemoteRoomDataSource.uploadRoomImage(uuid, imageList, onSuccessListener, onFailedListener);
    }

    @Override
    public void setRoom(@NonNull String key, @NonNull Room room,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener) {

        mRemoteRoomDataSource.setRoom(key, room, onSuccessListener, onFailedListener);
    }


    @Override
    public void getRoom(@NonNull String key,
                        @NonNull OnSuccessListener<Room> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener) {
        mRemoteRoomDataSource.getRoom(key, onSuccessListener, onFailedListener);
    }

    @Override
    public void delete(@NonNull String key, @NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        mRemoteRoomDataSource.delete(key, room, onSuccessListener, onFailedListener);
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
    public void setSellRoom(Room room) {
        mSellRoomDataSource.setSellRoom(room);
    }

    @Override
    public void deleteSellRoom(Room room) {
        mSellRoomDataSource.deleteSellRoom(room);
    }

    @Override
    public List<Room> getSellRoomList() {
        return mSellRoomDataSource.getSellRoomList();
    }

    @Override
    public void deleteSellRoom() {
        mSellRoomDataSource.deleteSellRoom();
    }

    @Override
    public Room getSellRoom(String key) {
        return mSellRoomDataSource.getSellRoom(key);
    }

}