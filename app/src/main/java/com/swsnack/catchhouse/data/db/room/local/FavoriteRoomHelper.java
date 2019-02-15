package com.swsnack.catchhouse.data.db.room.local;

import android.os.AsyncTask;

import com.swsnack.catchhouse.data.entity.RoomEntity;

import java.util.List;

import androidx.lifecycle.LiveData;

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

    public static class AsyncLoadFavoriteRoomList extends AsyncTask<Void, Void, LiveData<List<RoomEntity>>> {

        private RoomDao mRoomDao;

        AsyncLoadFavoriteRoomList(RoomDao roomDao) {
            this.mRoomDao = roomDao;
        }

        @Override
        protected LiveData<List<RoomEntity>> doInBackground(Void... voids) {
            return mRoomDao.loadFavoriteRoom();
        }
    }

    public static class  AsyncLoadFavoriteRoom extends AsyncTask<String, Void, RoomEntity> {

        private RoomDao mRoomDao;

        AsyncLoadFavoriteRoom(RoomDao roomDao) {
            this.mRoomDao = roomDao;
        }

        @Override
        protected RoomEntity doInBackground(String... strings) {
            return mRoomDao.getFavoriteRoom(strings[0]);
        }
    }
}