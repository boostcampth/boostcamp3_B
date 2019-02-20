package com.swsnack.catchhouse.repository.room.local;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.data.db.AppDatabase;
import com.swsnack.catchhouse.data.entity.RoomEntity;
import com.swsnack.catchhouse.data.mapper.RoomEntityMapper;
import com.swsnack.catchhouse.data.mapper.RoomMapper;
import com.swsnack.catchhouse.data.model.Room;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;

public class LocalFavoriteRoomImpl implements FavoriteRoomDataSource {

    private static LocalFavoriteRoomImpl INSTANCE;
    private RoomDao mRoomDao;

    public static LocalFavoriteRoomImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (LocalFavoriteRoomImpl.class) {
                INSTANCE = new LocalFavoriteRoomImpl();
            }
        }
        return INSTANCE;
    }

    private LocalFavoriteRoomImpl() {
        mRoomDao = AppDatabase.getInstance().getRoomDataAccessor();
    }

    @Override
    public void setFavoriteRoom(Room room) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }
        RoomEntity roomEntity = new RoomEntityMapper().map(room);
        roomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new FavoriteRoomHelper.AsyncSetFavoriteRoom(mRoomDao).execute(roomEntity);
    }

    @Override
    public void deleteFavoriteRoom(Room room) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        RoomEntity roomEntity = new RoomEntityMapper().map(room);
        roomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new FavoriteRoomHelper.AsyncDeleteFavoriteRoom(mRoomDao).execute(roomEntity);

    }

    @Nullable
    @Override
    public List<Room> getFavoriteRoomList() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }

        try {
            List<RoomEntity> roomEntityList = new FavoriteRoomHelper
                    .AsyncLoadFavoriteRoomList(mRoomDao)
                    .execute(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get();

            if (roomEntityList != null) {
                return new RoomMapper().mapToList(roomEntityList);
            }
            return null;

        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    @Override
    public void deleteFavoriteRoom() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }
        new FavoriteRoomHelper.AsyncDeleteUserFavoriteRoom(mRoomDao).execute(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Nullable
    @Override
    public Room getFavoriteRoom(String key) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }

        try {
            RoomEntity roomEntity = new FavoriteRoomHelper
                    .AsyncLoadFavoriteRoom(mRoomDao)
                    .execute(key, FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get();
            if (roomEntity != null) {
                return new RoomMapper().map(roomEntity);
            }
            return null;

        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    @Override
    public void updateRoom(Room room) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }
        RoomEntity roomEntity = new RoomEntityMapper().map(room);
        roomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());

        new FavoriteRoomHelper.AsyncUpdateFavoriteRoom(mRoomDao).execute(roomEntity);
    }
}
