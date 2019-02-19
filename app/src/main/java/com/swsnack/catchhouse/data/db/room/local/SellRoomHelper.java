package com.swsnack.catchhouse.data.db.room.local;

import android.os.AsyncTask;

import com.swsnack.catchhouse.data.entity.SellRoomEntity;

import java.util.List;

class SellRoomHelper {

    public static class AsyncSetSellRoom extends AsyncTask<SellRoomEntity, Void, Void> {

        private SellRoomDao mSellRoomDao;

        AsyncSetSellRoom(SellRoomDao sellRoomDao) {
            this.mSellRoomDao = sellRoomDao;
        }

        @Override
        protected Void doInBackground(SellRoomEntity... roomEntities) {
            mSellRoomDao.setFavoriteRoom(roomEntities[0]);
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
            mSellRoomDao.deleteFavoriteRoom(roomEntities[0]);
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
            return mSellRoomDao.loadFavoriteRoom(strings[0]);
        }
    }

    public static class AsyncLoadSellRoom extends AsyncTask<String, Void, SellRoomEntity> {

        private SellRoomDao mSellRoomDao;

        AsyncLoadSellRoom(SellRoomDao sellRoomDao) {
            this.mSellRoomDao = sellRoomDao;
        }

        @Override
        protected SellRoomEntity doInBackground(String... strings) {
            return mSellRoomDao.getFavoriteRoom(strings[0], strings[1]);
        }
    }

    public static class AsyncDeleteUserSellRoom extends AsyncTask<String, Void, Void> {

        private SellRoomDao mSellRoomDao;

        AsyncDeleteUserSellRoom(SellRoomDao sellRoomDao) {
            this.mSellRoomDao = sellRoomDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mSellRoomDao.deleteFavoriteRoom(strings[0]);
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
            mSellRoomDao.update(roomEntities[0]);
            return null;
        }
    }
}