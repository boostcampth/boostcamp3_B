package com.swsnack.catchhouse.data.mapper;

import com.swsnack.catchhouse.data.entity.RoomEntity;
import com.swsnack.catchhouse.data.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomMapper implements Mapper<RoomEntity, Room> {

    @Override
    public Room map(RoomEntity from) {
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
                from.getLongitude());
    }

    public List<Room> mapToList(List<RoomEntity> from) {
        List<Room> roomList = new ArrayList<>();
        for (RoomEntity item : from) {
            roomList.add(map(item));
        }
        return roomList;
    }
}
