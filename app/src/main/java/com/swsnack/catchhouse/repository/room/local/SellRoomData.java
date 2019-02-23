package com.swsnack.catchhouse.repository.room.local;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.data.db.AppDatabase;
import com.swsnack.catchhouse.data.entity.SellRoomEntity;
import com.swsnack.catchhouse.data.mapper.RoomMapperFromSell;
import com.swsnack.catchhouse.data.mapper.SellRoomMapper;
import com.swsnack.catchhouse.data.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;

public class SellRoomData implements SellRoomDataSource {

    private static SellRoomData INSTANCE;
    private SellRoomDao mRoomDao;

    public static SellRoomData getInstance() {
        if (INSTANCE == null) {
            synchronized (SellRoomData.class) {
                INSTANCE = new SellRoomData();
            }
        }
        return INSTANCE;
    }

    private SellRoomData() {
        mRoomDao = AppDatabase.getInstance().getSellRoomDataAccessor();
    }

    @Override
    public void setSellRoom(Room room) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        SellRoomEntity sellRoomEntity = new SellRoomMapper().map(room);
        sellRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new SellRoomDataHelper.AsyncSetSellRoom(mRoomDao).execute(sellRoomEntity);
    }

    @Override
    public void deleteSellRoom(Room room) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        SellRoomEntity sellRoomEntity = new SellRoomMapper().map(room);
        sellRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new SellRoomDataHelper.AsyncDeleteSellRoom(mRoomDao).execute(sellRoomEntity);

    }

    @Override
    public List<Room> getSellRoomList() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }

        try {
            List<SellRoomEntity> roomEntityList = new SellRoomDataHelper
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

        new SellRoomDataHelper.AsyncDeleteUserSellRoom(mRoomDao).execute(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    @Nullable
    @Override
    public Room getSellRoom(String key) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }

        try {
            SellRoomEntity sellRoomEntity = new SellRoomDataHelper.AsyncLoadSellRoom(mRoomDao).execute(key, FirebaseAuth.getInstance().getCurrentUser().getUid()).get();
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
        new SellRoomDataHelper.AsyncUpdateSellRoom(mRoomDao).execute(sellRoomEntity);

    }

}
