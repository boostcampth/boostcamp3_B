package com.swsnack.catchhouse.data.mapper;

import com.swsnack.catchhouse.data.entity.RoomEntity;
import com.swsnack.catchhouse.data.model.Room;

public class RoomEntityMapper implements Mapper<Room, RoomEntity> {

    @Override
    public RoomEntity map(Room from) {
        return new RoomEntity(from.getKey(),
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
}
