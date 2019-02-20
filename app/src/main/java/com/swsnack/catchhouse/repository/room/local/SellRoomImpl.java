package com.swsnack.catchhouse.repository.room.local;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.data.db.AppDatabase;
import com.swsnack.catchhouse.data.entity.FavoriteRoomEntity;
import com.swsnack.catchhouse.data.entity.SellRoomEntity;
import com.swsnack.catchhouse.data.mapper.RoomMapperFromFavorite;
import com.swsnack.catchhouse.data.mapper.RoomMapperFromSell;
import com.swsnack.catchhouse.data.mapper.SellRoomMapper;
import com.swsnack.catchhouse.data.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;

public class SellRoomImpl implements SellRoomDataSource {

    private static SellRoomImpl INSTANCE;
    private SellRoomDao mRoomDao;

    public static SellRoomImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (SellRoomImpl.class) {
                INSTANCE = new SellRoomImpl();
            }
        }
        return INSTANCE;
    }

    private SellRoomImpl() {
        mRoomDao = AppDatabase.getInstance().getSellRoomDataAccessor();
    }

    @Override
    public void setSellRoom(Room room) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        SellRoomEntity sellRoomEntity = new SellRoomMapper().map(room);
        sellRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new SellRoomHelper.AsyncSetSellRoom(mRoomDao).execute(sellRoomEntity);
    }

    @Override
    public void deleteSellRoom(Room room) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        SellRoomEntity sellRoomEntity = new SellRoomMapper().map(room);
        sellRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new SellRoomHelper.AsyncDeleteSellRoom(mRoomDao).execute(sellRoomEntity);

    }

    @Override
    public List<Room> getSellRoomList() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }

        try {
            List<SellRoomEntity> roomEntityList = new SellRoomHelper
                    .AsyncLoadSellRoomList(mRoomDao)
                    .execute(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get();
            if (roomEntityList != null) {
                return new RoomMapperFromSell().mapToList(roomEntityList);
            }
            return new ArrayList<>();
        } catch (ExecutionException | InterruptedException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteSellRoom() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        new SellRoomHelper.AsyncDeleteUserSellRoom(mRoomDao).execute(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    @Nullable
    @Override
    public Room getSellRoom(String key) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }

        try {
            SellRoomEntity sellRoomEntity = new SellRoomHelper.AsyncLoadSellRoom(mRoomDao).execute(key, FirebaseAuth.getInstance().getCurrentUser().getUid()).get();
            if(sellRoomEntity != null) {
                return new RoomMapperFromSell().map(sellRoomEntity);
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
        SellRoomEntity sellRoomEntity = new SellRoomMapper().map(room);
        sellRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new SellRoomHelper.AsyncUpdateSellRoom(mRoomDao).execute(sellRoomEntity);

    }

}
