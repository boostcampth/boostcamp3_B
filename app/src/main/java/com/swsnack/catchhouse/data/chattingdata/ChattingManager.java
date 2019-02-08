package com.swsnack.catchhouse.data.chattingdata;

import android.support.annotation.NonNull;

import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.chattingdata.pojo.Chatting;
import com.swsnack.catchhouse.data.chattingdata.pojo.Message;

public interface ChattingManager {

    void getChattingList(@NonNull String uuid, @NonNull ValueEventListener valueEventListener);

    void getChatData(@NonNull String chatRoomId, @NonNull ValueEventListener valueEventListener);

    void setChattingRoom(@NonNull Chatting chattingUser, @NonNull ValueEventListener valueEventListener);

    void setChatMessage(@NonNull Message message, @NonNull ValueEventListener valueEventListener);
}
