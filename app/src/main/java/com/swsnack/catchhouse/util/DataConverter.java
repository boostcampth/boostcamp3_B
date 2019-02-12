package com.swsnack.catchhouse.util;

import android.graphics.Bitmap;

import com.swsnack.catchhouse.data.chattingdata.model.Chatting;
import com.swsnack.catchhouse.data.chattingdata.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DataConverter {

    public static Bitmap getScaledBitmap(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.2), (int) (bitmap.getHeight() * 0.2), true);
    }

    public static byte[] getByteArray(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArrayFromBitmap = outputStream.toByteArray();
        outputStream.close();
        return byteArrayFromBitmap;
    }

    public static List<Chatting> reOrderedListByTimeStamp(List<Chatting> chattingList) {
        if(chattingList == null) {
            return null;
        }

        Collections.sort(chattingList, (rowIndexChatting, highIndexChatting) -> {
            if(rowIndexChatting.getMessages() == null || highIndexChatting.getMessages() == null) {
                return 0;
            }

            Message rowIndexLastMessage = rowIndexChatting.getMessages().get(rowIndexChatting.getMessages().size() - 1);
            Message highIndexLastMessage = highIndexChatting.getMessages().get(highIndexChatting.getMessages().size() - 1);

            return rowIndexLastMessage.getTimestamp().compareTo(highIndexLastMessage.getTimestamp());
        });

        Collections.reverse(chattingList);

        return chattingList;
    }
}
