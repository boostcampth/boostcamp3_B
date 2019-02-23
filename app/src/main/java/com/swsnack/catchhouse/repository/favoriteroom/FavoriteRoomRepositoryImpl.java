package com.swsnack.catchhouse.repository.favoriteroom;

import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

public class FavoriteRoomRepositoryImpl implements FavoriteRoomRepository {

    private static FavoriteRoomRepositoryImpl INSTANCE;
    private FavoriteRoomDaoImpl favoriteRoomDao;

    public static FavoriteRoomRepositoryImpl getInstance() {
        if(INSTANCE == null) {
            synchronized (FavoriteRoomRepositoryImpl.class) {
                INSTANCE = new FavoriteRoomRepositoryImpl();
            }
        }
        return INSTANCE;
    }

    private FavoriteRoomRepositoryImpl() {
        favoriteRoomDao = FavoriteRoomDaoImpl.getInstance();
    }
    @Override
    public void setFavoriteRoom(Room room) {
        favoriteRoomDao.setFavoriteRoom(room);
    }

    @Override
    public void deleteFavoriteRoom(Room room) {
        favoriteRoomDao.deleteFavoriteRoom(room);
    }

    @Override
    public List<Room> getFavoriteRoomList() {
        return favoriteRoomDao.getFavoriteRoomList();
    }

    @Override
    public void deleteFavoriteRoom() {
        favoriteRoomDao.deleteFavoriteRoom();
    }

    @Override
    public Room getFavoriteRoom(String key) {
        return favoriteRoomDao.getFavoriteRoom(key);
    }

    @Override
    public void updateRoom(Room room) {
        favoriteRoomDao.updateRoom(room);
    }
}
