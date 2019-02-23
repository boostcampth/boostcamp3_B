package com.swsnack.catchhouse.repository;

import android.net.Uri;

import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.data.model.Filter;
import com.swsnack.catchhouse.data.model.Message;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.data.model.User;
import com.swsnack.catchhouse.data.source.chatting.ChattingDataSource;
import com.swsnack.catchhouse.data.source.chatting.remote.RemoteChattingImpl;
import com.swsnack.catchhouse.data.source.location.LocationDataSource;
import com.swsnack.catchhouse.data.source.location.remote.RemoteLocationImpl;
import com.swsnack.catchhouse.data.source.recentroom.RecentRoomRepository;
import com.swsnack.catchhouse.data.source.recentroom.RecentRoomRepositoryImpl;
import com.swsnack.catchhouse.data.source.room.local.SellRoomDataSource;
import com.swsnack.catchhouse.data.source.room.local.SellRoomData;
import com.swsnack.catchhouse.data.source.searching.SearchingDataSource;
import com.swsnack.catchhouse.data.source.searching.remote.SearchingDataImpl;
import com.swsnack.catchhouse.data.source.user.UserDataSource;
import com.swsnack.catchhouse.data.source.user.remote.UserDataImpl;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Single;

public class AppDataSource implements DataSource {
    //FIXME : 이 클래스 삭제할겁니다. 사용해주지 마시고 레포에서 써주세요
    private UserDataSource mUserDataSource;
    private ChattingDataSource mRemoteChattingDataSource;
//    private RemoteRoomDataSource mRemoteRoomDataSource;
//    private FavoriteRoomDataSource mFavoriteRoomDataSource;
//    private RecentRoomDataSource mRecentRoomDataManager;
    private RecentRoomRepository recentRoomRepository;
    private FavoriteRoomRepository favoriteRoomRepository;
    private RoomRepository roomRepository;
    private LocationDataSource mLocationDataSource;
    private SearchingDataSource mSearchingDataSource;
    private SellRoomDataSource mSellRoomDataSource;

    private AppDataSource() {

        mUserDataSource = UserDataImpl.getInstance();
        mRemoteChattingDataSource = RemoteChattingImpl.getInstance();
//        mRemoteRoomDataSource = RoomRepositoryImpl2.getInstance();
//        mFavoriteRoomDataSource = RoomRepositoryImpl2.getInstance();
//        mRecentRoomDataManager = RoomRepositoryImpl2.getInstance();
        mLocationDataSource = RemoteLocationImpl.getInstance();
        mSearchingDataSource = SearchingDataImpl.getInstance();
        mSellRoomDataSource = SellRoomData.getInstance();
        roomRepository = RoomRepositoryImpl.getInstance();
        favoriteRoomRepository = FavoriteRoomRepositoryImpl.getInstance();
        recentRoomRepository = RecentRoomRepositoryImpl.getInstance();
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

//    @Override
//    public String createKey() {
//        return mRemoteRoomDataSource.createKey();
//    }
//
//    @Override
//    public void uploadRoomImage(@NonNull String uuid, @NonNull List<Uri> imageList,
//                                @NonNull OnSuccessListener<List<String>> onSuccessListener,
//                                @NonNull OnFailedListener onFailedListener) {
//
//        mRemoteRoomDataSource.uploadRoomImage(uuid, imageList, onSuccessListener, onFailedListener);
//    }
//
//    @Override
//    public void setRoom(@NonNull String key, @NonNull Room room,
//                        @NonNull OnSuccessListener<Void> onSuccessListener,
//                        @NonNull OnFailedListener onFailedListener) {
//
//        mRemoteRoomDataSource.setRoom(key, room, onSuccessListener, onFailedListener);
//    }
//
//
//    @Override
//    public void getRoom(@NonNull String key,
//                        @NonNull OnSuccessListener<Room> onSuccessListener,
//                        @NonNull OnFailedListener onFailedListener) {
//        mRemoteRoomDataSource.getRoom(key, onSuccessListener, onFailedListener);
//    }
//
//    @Override
//    public void delete(@NonNull String key, @NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
//        mRemoteRoomDataSource.delete(key, room, onSuccessListener, onFailedListener);
//    }

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
    public String createKey() {
        return roomRepository.createKey();
    }

    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<Uri> imageList, @NonNull OnSuccessListener<List<String>> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        roomRepository.uploadRoomImage(uuid, imageList, onSuccessListener, onFailedListener);
    }

    @Override
    public void setRoom(@NonNull String key, @NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        roomRepository.setRoom(key, room, onSuccessListener, onFailedListener);
    }

    @Override
    public void getRoom(@NonNull String key, @NonNull OnSuccessListener<Room> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        roomRepository.getRoom(key, onSuccessListener, onFailedListener);
    }

    @Override
    public void deleteRoom(@NonNull String key, @NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        roomRepository.deleteRoom(key, room, onSuccessListener, onFailedListener);
    }

    @Override
    public void updateRoom(@NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        roomRepository.updateRoom(room, onSuccessListener, onFailedListener);
    }

    @Override
    public List<Room> getSellList() {
        return roomRepository.getSellList();
    }

    @Override
    public void setFavoriteRoom(Room room) {
        favoriteRoomRepository.setFavoriteRoom(room);
    }

    @Override
    public void deleteFavoriteRoom(Room room) {
        favoriteRoomRepository.deleteFavoriteRoom(room);
    }

    @Override
    public List<Room> getFavoriteRoomList() {
        return favoriteRoomRepository.getFavoriteRoomList();
    }

    @Override
    public void deleteFavoriteRoom() {
        favoriteRoomRepository.deleteFavoriteRoom();
    }

    @Override
    public Room getFavoriteRoom(String key) {
        return favoriteRoomRepository.getFavoriteRoom(key);
    }

    @Override
    public void updateRoom(Room room) {
        favoriteRoomRepository.updateRoom(room);
    }

    @Override
    public void setRecentRoom(Room room) {
        recentRoomRepository.setRecentRoom(room);
    }

    @Override
    public List<Room> getRecentRoom() {
        return recentRoomRepository.getRecentRoom();
    }

    @Override
    public void deleteRecentRoomList() {
        recentRoomRepository.deleteRecentRoomList();
    }

    @Override
    public void deleteRoom(Room room) {
        recentRoomRepository.deleteRoom(room);
    }

//    @Override
//    public void setFavoriteRoom(Room room) {
//        mFavoriteRoomDataSource.setFavoriteRoom(room);
//    }
//
//    @Override
//    public void deleteFavoriteRoom(Room room) {
//        mFavoriteRoomDataSource.deleteFavoriteRoom(room);
//
//    }
//
//    @Override
//    public void deleteFavoriteRoom() {
//        mFavoriteRoomDataSource.deleteFavoriteRoom();
//    }
//
//    @Override
//    public List<Room> getFavoriteRoomList() {
//        return mFavoriteRoomDataSource.getFavoriteRoomList();
//    }
//
//    @Override
//    public Room getFavoriteRoom(String key) {
//        return mFavoriteRoomDataSource.getFavoriteRoom(key);
//    }
//
//    @Override
//    public void updateRoom(Room room) {
//        mFavoriteRoomDataSource.updateRoom(room);
//    }
//
//    @Override
//    public void setRecentRoom(Room room) {
//        mRecentRoomDataManager.setRecentRoom(room);
//    }
//
//    @Override
//    public List<Room> getRecentRoom() {
//        return mRecentRoomDataManager.getRecentRoom();
//    }
//
//    @Override
//    public void deleteRecentRoomList() {
//        mRecentRoomDataManager.deleteRecentRoomList();
//    }

//    @Override
//    public void deleteRoom(Room room) {
//        deleteRoom(room);
//    }
//
//    @Override
//    public void setSellRoom(Room room) {
//        mSellRoomDataSource.setSellRoom(room);
//    }
//
//    @Override
//    public void deleteSellRoom(Room room) {
//        mSellRoomDataSource.deleteSellRoom(room);
//    }
//
//    @Override
//    public List<Room> getSellRoomList() {
//        return mSellRoomDataSource.getSellRoomList();
//    }
//
//    @Override
//    public void deleteSellRoom() {
//        mSellRoomDataSource.deleteSellRoom();
//    }
//
//    @Override
//    public Room getSellRoom(String key) {
//        return mSellRoomDataSource.getSellRoom(key);
//    }

}