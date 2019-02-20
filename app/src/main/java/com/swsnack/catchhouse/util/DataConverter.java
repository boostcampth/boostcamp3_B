package com.swsnack.catchhouse.util;

import android.graphics.Bitmap;

import com.swsnack.catchhouse.data.entity.RoomEntity;
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

    public static RoomEntity convertToRoomEntity(Room room) {
        return new RoomEntity(room.getKey(),
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

    public static Room convertToRoom(RoomEntity roomEntity) {
        return new Room(roomEntity.getRoomUid(),
                roomEntity.getPrice(),
                roomEntity.getFrom(),
                roomEntity.getTo(),
                roomEntity.getTitle(),
                roomEntity.getContent(),
                roomEntity.getImages(),
                roomEntity.getUuid(),
                roomEntity.getAddress(),
                roomEntity.getAddressName(),
                roomEntity.getSize(),
                roomEntity.isOptionStandard(),
                roomEntity.isOptionGender(),
                roomEntity.isOptionPet(),
                roomEntity.isOptionSmoking(),
                roomEntity.getLatitude(),
                roomEntity.getLongitude(),
                roomEntity.isDeleted());
    }
}
