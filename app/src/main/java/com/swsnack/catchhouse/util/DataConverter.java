package com.swsnack.catchhouse.util;

import android.os.Build;

import com.swsnack.catchhouse.data.model.Chatting;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;

public class DataConverter {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Chatting> reOrderedListByTimeStamp(List<Chatting> chattingList) {
        if (chattingList == null) {
            return null;
        }

        Comparator<Chatting> comparator = (c1, c2) -> {
            assert c1.getMessages() != null;
            assert c2.getMessages() != null;

            return (c1.getMessages().get(c1.getMessages().size() - 1).getTimestamp()
                    .compareTo(c2.getMessages().get(c2.getMessages().size() - 1).getTimestamp()));
        };
        Comparator<Chatting> reverse = comparator.reversed();

        return chattingList.stream()
                .sorted(reverse)
                .collect(Collectors.toList());
    }
}
