package com.swsnack.catchhouse.repository.favoriteroom;

import android.os.AsyncTask;

import com.swsnack.catchhouse.data.entity.FavoriteRoomEntity;

import java.util.List;

class FavoriteRoomHelper {

    public static class AsyncSetFavoriteRoom extends AsyncTask<FavoriteRoomEntity, Void, Void> {

        private FavoriteRoomDao mFavoriteRoomDao;

        AsyncSetFavoriteRoom(FavoriteRoomDao favoriteRoomDao) {
            this.mFavoriteRoomDao = favoriteRoomDao;
        }

        @Override
        protected Void doInBackground(FavoriteRoomEntity... roomEntities) {
            mFavoriteRoomDao.setFavoriteRoom(roomEntities[0]);
            return null;
        }
    }

    public static class AsyncDeleteFavoriteRoom extends AsyncTask<FavoriteRoomEntity, Void, Void> {

        private FavoriteRoomDao mFavoriteRoomDao;

        AsyncDeleteFavoriteRoom(FavoriteRoomDao favoriteRoomDao) {
            mFavoriteRoomDao = favoriteRoomDao;
        }

        @Override
        protected Void doInBackground(FavoriteRoomEntity... roomEntities) {
            mFavoriteRoomDao.deleteFavoriteRoom(roomEntities[0]);
            return null;
        }
    }

    public static class AsyncLoadFavoriteRoomList extends AsyncTask<String, Void, List<FavoriteRoomEntity>> {

        private FavoriteRoomDao mFavoriteRoomDao;

        AsyncLoadFavoriteRoomList(FavoriteRoomDao favoriteRoomDao) {
            this.mFavoriteRoomDao = favoriteRoomDao;
        }

        @Override
        protected List<FavoriteRoomEntity> doInBackground(String... strings) {
            return mFavoriteRoomDao.loadFavoriteRoom(strings[0]);
        }
    }

    public static class  AsyncLoadFavoriteRoom extends AsyncTask<String, Void, FavoriteRoomEntity> {

        private FavoriteRoomDao mFavoriteRoomDao;

        AsyncLoadFavoriteRoom(FavoriteRoomDao favoriteRoomDao) {
            this.mFavoriteRoomDao = favoriteRoomDao;
        }

        @Override
        protected FavoriteRoomEntity doInBackground(String... strings) {
            return mFavoriteRoomDao.getFavoriteRoom(strings[0], strings[1]);
        }
    }

    public static class AsyncDeleteUserFavoriteRoom extends AsyncTask<String, Void, Void> {

        private FavoriteRoomDao mFavoriteRoomDao;

        AsyncDeleteUserFavoriteRoom(FavoriteRoomDao favoriteRoomDao) {
            this.mFavoriteRoomDao = favoriteRoomDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mFavoriteRoomDao.deleteFavoriteRoom(strings[0]);
            return null;
        }
    }

    public static class AsyncUpdateFavoriteRoom extends AsyncTask<FavoriteRoomEntity, Void, Void> {

        private FavoriteRoomDao mFavoriteRoomDao;

        AsyncUpdateFavoriteRoom(FavoriteRoomDao favoriteRoomDao) {
            this.mFavoriteRoomDao = favoriteRoomDao;
        }

        @Override
        protected Void doInBackground(FavoriteRoomEntity... roomEntities) {
            mFavoriteRoomDao.update(roomEntities[0]);
            return null;
        }
    }
}