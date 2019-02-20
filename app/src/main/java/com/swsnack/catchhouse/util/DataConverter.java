package com.swsnack.catchhouse.util;

import android.graphics.Bitmap;

import com.swsnack.catchhouse.data.entity.FavoriteRoomEntity;
import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.data.model.Message;
import com.swsnack.catchhouse.data.model.Room;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DataConverter {

    public static Bitmap getScaledBitmap(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()), (int) (bitmap.getHeight()), true);
    }

    public static byte[] getByteArray(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArrayFromBitmap = outputStream.toByteArray();
        outputStream.close();
        return byteArrayFromBitmap;
    }

    public static List<Chatting> reOrderedListByTimeStamp(List<Chatting> chattingList) {
        if (chattingList == null) {
            return null;
        }

        Collections.sort(chattingList, (rowIndexChatting, highIndexChatting) -> {
            if (rowIndexChatting.getMessages() == null || highIndexChatting.getMessages() == null) {
                return 0;
            }

            Message rowIndexLastMessage = rowIndexChatting.getMessages().get(rowIndexChatting.getMessages().size() - 1);
            Message highIndexLastMessage = highIndexChatting.getMessages().get(highIndexChatting.getMessages().size() - 1);

            return rowIndexLastMessage.getTimestamp().compareTo(highIndexLastMessage.getTimestamp());
        });

        Collections.reverse(chattingList);

        return chattingList;
    }

    public static FavoriteRoomEntity convertToRoomEntity(Room room) {
        return new FavoriteRoomEntity(room.getKey(),
                room.getPrice(),
                room.getFrom(),
                room.getTo(),
                room.getTitle(),
                room.getContent(),
                room.getImages(),
                room.getUuid(),
                room.getAddress(),
                room.getAddressName(),
                room.getSize(),
                room.isOptionStandard(),
                room.isOptionGender(),
                room.isOptionPet(),
                room.isOptionSmoking(),
                room.getLatitude(),
                room.getLongitude(),
                room.isDeleted());
    }

    public static Room convertToRoom(FavoriteRoomEntity favoriteRoomEntity) {
        return new Room(favoriteRoomEntity.getRoomUid(),
                favoriteRoomEntity.getPrice(),
                favoriteRoomEntity.getFrom(),
                favoriteRoomEntity.getTo(),
                favoriteRoomEntity.getTitle(),
                favoriteRoomEntity.getContent(),
                favoriteRoomEntity.getImages(),
                favoriteRoomEntity.getUuid(),
                favoriteRoomEntity.getAddress(),
                favoriteRoomEntity.getAddressName(),
                favoriteRoomEntity.getSize(),
                favoriteRoomEntity.isOptionStandard(),
                favoriteRoomEntity.isOptionGender(),
                favoriteRoomEntity.isOptionPet(),
                favoriteRoomEntity.isOptionSmoking(),
                favoriteRoomEntity.getLatitude(),
                favoriteRoomEntity.getLongitude(),
                favoriteRoomEntity.isDeleted());
    }
}
