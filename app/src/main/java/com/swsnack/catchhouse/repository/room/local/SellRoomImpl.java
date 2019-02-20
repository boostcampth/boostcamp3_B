package com.swsnack.catchhouse.repository.room.local;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.data.db.AppDatabase;
import com.swsnack.catchhouse.data.entity.SellRoomEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SellRoomImpl implements LocalSellRoomDataSource {

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
    public void setSellRoom(SellRoomEntity sellRoomEntity) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        sellRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new SellRoomHelper.AsyncSetSellRoom(mRoomDao).execute(sellRoomEntity);
    }

    @Override
    public void deleteSellRoom(SellRoomEntity sellRoomEntity) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        sellRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new SellRoomHelper.AsyncDeleteSellRoom(mRoomDao).execute(sellRoomEntity);

    }

    @Override
    public List<SellRoomEntity> getSellRoomList() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }

        try {
            return new SellRoomHelper.AsyncLoadSellRoomList(mRoomDao).execute(FirebaseAuth.getInstance().getCurrentUser().getUid()).get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    @Override
    public void deleteSellRoom() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        new SellRoomHelper.AsyncDeleteUserSellRoom(mRoomDao).execute(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    @Override
    public SellRoomEntity getSellRoom(String key) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return null;
        }

        try {
            return new SellRoomHelper.AsyncLoadSellRoom(mRoomDao).execute(key, FirebaseAuth.getInstance().getCurrentUser().getUid()).get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    @Override
    public void updateRoom(SellRoomEntity sellRoomEntity) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        sellRoomEntity.setFirebaseUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new SellRoomHelper.AsyncUpdateSellRoom(mRoomDao).execute(sellRoomEntity);

    }

}
