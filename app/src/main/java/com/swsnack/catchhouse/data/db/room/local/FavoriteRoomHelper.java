package com.swsnack.catchhouse.data.db.room.local;

import android.os.AsyncTask;

import com.swsnack.catchhouse.data.entity.RoomEntity;

import java.util.List;

class FavoriteRoomHelper {

    public static class AsyncSetFavoriteRoom extends AsyncTask<RoomEntity, Void, Void> {

        private RoomDao mRoomDao;

        AsyncSetFavoriteRoom(RoomDao roomDao) {
            this.mRoomDao = roomDao;
        }

        @Override
        protected Void doInBackground(RoomEntity... roomEntities) {
            mRoomDao.setFavoriteRoom(roomEntities[0]);
            return null;
        }
    }

    public static class AsyncDeleteFavoriteRoom extends AsyncTask<RoomEntity, Void, Void> {

        private RoomDao mRoomDao;

        AsyncDeleteFavoriteRoom(RoomDao roomDao) {
            mRoomDao = roomDao;
        }

        @Override
        protected Void doInBackground(RoomEntity... roomEntities) {
            mRoomDao.deleteFavoriteRoom(roomEntities[0]);
            return null;
        }
    }

    public static class AsyncLoadFavoriteRoomList extends AsyncTask<String, Void, List<RoomEntity>> {

        private RoomDao mRoomDao;

        AsyncLoadFavoriteRoomList(RoomDao roomDao) {
            this.mRoomDao = roomDao;
        }

        @Override
        protected List<RoomEntity> doInBackground(String... strings) {
            return mRoomDao.loadFavoriteRoom(strings[0]);
        }
    }

    public static class  AsyncLoadFavoriteRoom extends AsyncTask<String, Void, RoomEntity> {

        private RoomDao mRoomDao;

        AsyncLoadFavoriteRoom(RoomDao roomDao) {
            this.mRoomDao = roomDao;
        }

        @Override
        protected RoomEntity doInBackground(String... strings) {
            return mRoomDao.getFavoriteRoom(strings[0], strings[1]);
        }
    }

    public static class AsyncDeleteUserFavoriteRoom extends AsyncTask<String, Void, Void> {

        private RoomDao mRoomDao;

        AsyncDeleteUserFavoriteRoom(RoomDao roomDao) {
            this.mRoomDao = roomDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mRoomDao.deleteFavoriteRoom(strings[0]);
            return null;
        }
    }

    public static class AsyncUpdateFavoriteRoom extends AsyncTask<RoomEntity, Void, Void> {

        private RoomDao mRoomDao;

        AsyncUpdateFavoriteRoom(RoomDao roomDao) {
            this.mRoomDao = roomDao;
        }

        @Override
        protected Void doInBackground(RoomEntity... roomEntities) {
            mRoomDao.update(roomEntities[0]);
            return null;
        }
    }
}