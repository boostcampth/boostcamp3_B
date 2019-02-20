package com.swsnack.catchhouse.data.mapper;

import com.swsnack.catchhouse.data.entity.FavoriteRoomEntity;
import com.swsnack.catchhouse.data.model.Room;

public class FavoriteRoomMapper implements Mapper<Room, FavoriteRoomEntity> {

    @Override
    public FavoriteRoomEntity map(Room from) {
        return new FavoriteRoomEntity(from.getKey(),
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
