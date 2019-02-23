package com.swsnack.catchhouse.repository.favoriteroom;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.data.db.AppDatabase;
import com.swsnack.catchhouse.data.entity.FavoriteRoomEntity;
import com.swsnack.catchhouse.data.mapper.FavoriteRoomMapper;
import com.swsnack.catchhouse.data.mapper.RoomMapperFromFavorite;
import com.swsnack.catchhouse.data.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;

public class FavoriteRoomDaoImpl implements FavoriteRoomDataSource {

    private static FavoriteRoomDaoImpl INSTANCE;
    private FavoriteRoomDao mRoomDao;

    public static FavoriteRoomDaoImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (FavoriteRoomDaoImpl.class) {
                INSTANCE = new FavoriteRoomDaoImpl();
            }
        }
        return INSTANCE;
    }

    private FavoriteRoomDaoImpl() {
        mRoomDao = AppDatabase.getInstance().getRoomDataAccessor();
    }

    @Override
    public void setFavoriteRoom(Room room) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }
        FavoriteRoomEntity favoriteRoomEntity = new FavoriteRoomMapper().map(room);
        favoriteRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new FavoriteRoomHelper.AsyncSetFavoriteRoom(mRoomDao).execute(favoriteRoomEntity);
    }

    @Override
    public void deleteFavoriteRoom(Room room) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        FavoriteRoomEntity favoriteRoomEntity = new FavoriteRoomMapper().map(room);
        favoriteRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new FavoriteRoomHelper.AsyncDeleteFavoriteRoom(mRoomDao).execute(favoriteRoomEntity);

    }

    @Override
    public List<Room> getFavoriteRoomList() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }

        try {
            List<FavoriteRoomEntity> roomEntityList = new FavoriteRoomHelper
                    .AsyncLoadFavoriteRoomList(mRoomDao)
                    .execute(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get();

            if (roomEntityList != null) {
                return new RoomMapperFromFavorite().mapToList(roomEntityList);
            }
            return new ArrayList<>();

        } catch (ExecutionException | InterruptedException e) {
            return new ArrayList<>();
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
                return new RoomMapperFromFavorite().map(favoriteRoomEntity);
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
        FavoriteRoomEntity favoriteRoomEntity = new FavoriteRoomMapper().map(room);
        favoriteRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());

        new FavoriteRoomHelper.AsyncUpdateFavoriteRoom(mRoomDao).execute(favoriteRoomEntity);
    }
}
