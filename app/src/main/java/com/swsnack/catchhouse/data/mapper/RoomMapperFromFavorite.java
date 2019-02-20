package com.swsnack.catchhouse.data.mapper;

import com.swsnack.catchhouse.data.entity.FavoriteRoomEntity;
import com.swsnack.catchhouse.data.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomMapperFromFavorite implements Mapper<FavoriteRoomEntity, Room> {

    @Override
    public Room map(FavoriteRoomEntity from) {
        return new Room(from.getRoomUid(),
                from.getPrice(),
                from.getFrom(),
                from.getTo(),
                from.getTitle(),
                from.getContent(),
                from.getImages(),
                from.getUuid(),
                from.getAddress(),
                from.getAddressName(),
                from.getSize(),
                from.isOptionStandard(),
                from.isOptionGender(),
                from.isOptionPet(),
                from.isOptionSmoking(),
                from.getLatitude(),
                from.getLongitude(),
                from.isDeleted());
    }

    public List<Room> mapToList(List<FavoriteRoomEntity> from) {
        List<Room> roomList = new ArrayList<>();
        for (FavoriteRoomEntity item : from) {
            roomList.add(map(item));
        }
        return roomList;
    }
}
