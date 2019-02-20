package com.swsnack.catchhouse.repository.room.local;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.data.db.AppDatabase;
import com.swsnack.catchhouse.data.entity.FavoriteRoomEntity;
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
        FavoriteRoomEntity favoriteRoomEntity = new RoomEntityMapper().map(room);
        favoriteRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new FavoriteRoomHelper.AsyncSetFavoriteRoom(mRoomDao).execute(favoriteRoomEntity);
    }

    @Override
    public void deleteFavoriteRoom(Room room) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        FavoriteRoomEntity favoriteRoomEntity = new RoomEntityMapper().map(room);
        favoriteRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new FavoriteRoomHelper.AsyncDeleteFavoriteRoom(mRoomDao).execute(favoriteRoomEntity);

    }

    @Nullable
    @Override
    public List<Room> getFavoriteRoomList() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }

        try {
            List<FavoriteRoomEntity> favoriteRoomEntityList = new FavoriteRoomHelper
                    .AsyncLoadFavoriteRoomList(mRoomDao)
                    .execute(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get();

            if (favoriteRoomEntityList != null) {
                return new RoomMapper().mapToList(favoriteRoomEntityList);
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
            FavoriteRoomEntity favoriteRoomEntity = new FavoriteRoomHelper
                    .AsyncLoadFavoriteRoom(mRoomDao)
                    .execute(key, FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get();
            if (favoriteRoomEntity != null) {
                return new RoomMapper().map(favoriteRoomEntity);
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
        FavoriteRoomEntity favoriteRoomEntity = new RoomEntityMapper().map(room);
        favoriteRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());

        new FavoriteRoomHelper.AsyncUpdateFavoriteRoom(mRoomDao).execute(favoriteRoomEntity);
    }
}
