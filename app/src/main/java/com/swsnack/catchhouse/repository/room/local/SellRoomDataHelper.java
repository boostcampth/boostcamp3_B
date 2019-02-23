package com.swsnack.catchhouse.repository.room.local;

import android.os.AsyncTask;

import com.swsnack.catchhouse.data.entity.SellRoomEntity;

import java.util.List;

class SellRoomDataHelper {

    public static class AsyncSetSellRoom extends AsyncTask<SellRoomEntity, Void, Void> {

        private SellRoomDao mSellRoomDao;

        AsyncSetSellRoom(SellRoomDao sellRoomDao) {
            this.mSellRoomDao = sellRoomDao;
        }

        @Override
        protected Void doInBackground(SellRoomEntity... roomEntities) {
            mSellRoomDao.setSellRoom(roomEntities[0]);
            return null;
        }
    }

    public static class AsyncDeleteSellRoom extends AsyncTask<SellRoomEntity, Void, Void> {

        private SellRoomDao mSellRoomDao;

        AsyncDeleteSellRoom(SellRoomDao sellRoomDao) {
            mSellRoomDao = sellRoomDao;
        }

        @Override
        protected Void doInBackground(SellRoomEntity... roomEntities) {
            mSellRoomDao.deleteSellRoom(roomEntities[0]);
            return null;
        }
    }

    public static class AsyncLoadSellRoomList extends AsyncTask<String, Void, List<SellRoomEntity>> {

        private SellRoomDao mSellRoomDao;

        AsyncLoadSellRoomList(SellRoomDao sellRoomDao) {
            this.mSellRoomDao = sellRoomDao;
        }

        @Override
        protected List<SellRoomEntity> doInBackground(String... strings) {
            return mSellRoomDao.getSellRoom(strings[0]);
        }
    }

    public static class AsyncLoadSellRoom extends AsyncTask<String, Void, SellRoomEntity> {

        private SellRoomDao mSellRoomDao;

        AsyncLoadSellRoom(SellRoomDao sellRoomDao) {
            this.mSellRoomDao = sellRoomDao;
        }

        @Override
        protected SellRoomEntity doInBackground(String... strings) {
            return mSellRoomDao.getSellRoom(strings[0], strings[1]);
        }
    }

    public static class AsyncDeleteUserSellRoom extends AsyncTask<String, Void, Void> {

        private SellRoomDao mSellRoomDao;

        AsyncDeleteUserSellRoom(SellRoomDao sellRoomDao) {
            this.mSellRoomDao = sellRoomDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mSellRoomDao.deleteSellRoom(strings[0]);
            return null;
        }
    }

    public static class AsyncUpdateSellRoom extends AsyncTask<SellRoomEntity, Void, Void> {

        private SellRoomDao mSellRoomDao;

        AsyncUpdateSellRoom(SellRoomDao sellRoomDao) {
            this.mSellRoomDao = sellRoomDao;
        }

        @Override
        protected Void doInBackground(SellRoomEntity... roomEntities) {
            mSellRoomDao.updateSellRoom(roomEntities[0]);
            return null;
        }
    }
}