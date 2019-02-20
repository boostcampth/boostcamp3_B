package com.swsnack.catchhouse.data.mapper;

import com.swsnack.catchhouse.data.entity.SellRoomEntity;
import com.swsnack.catchhouse.data.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomMapperFromSell implements Mapper<SellRoomEntity, Room> {

    @Override
    public Room map(SellRoomEntity from) {
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
                false);
    }

    public List<Room> mapToList(List<SellRoomEntity> from) {
        List<Room> roomList = new ArrayList<>();
        for (SellRoomEntity item : from) {
            roomList.add(map(item));
        }
        return roomList;
    }
}
