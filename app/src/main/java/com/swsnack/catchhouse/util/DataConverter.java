package com.swsnack.catchhouse.util;

import android.graphics.Bitmap;

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

    public static List<Message> sortByValueFromMapToList(Map<String, Message> map) {
        List<String> keys = new ArrayList<>(map.keySet());
        List<Message> messages = new ArrayList<>();

        Collections.sort(keys, (key1, key2) -> {
            Message message1 = map.get(key1);
            Message message2 = map.get(key2);
            return message1.getTimestamp().compareTo(message2.getTimestamp());
        });

        for (String key : keys) {
            messages.add(map.get(key));
        }
        return messages;
    }
}
