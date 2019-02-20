package com.swsnack.catchhouse.data.mapper;

import com.swsnack.catchhouse.data.entity.SellRoomEntity;
import com.swsnack.catchhouse.data.model.Room;

public class SellRoomMapper implements Mapper<Room, SellRoomEntity> {

    @Override
    public SellRoomEntity map(Room from) {
        return new SellRoomEntity(from.getKey(),
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
}
