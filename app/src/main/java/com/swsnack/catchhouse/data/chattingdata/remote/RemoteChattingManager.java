package com.swsnack.catchhouse.data.chattingdata.remote;

import android.support.annotation.NonNull;

import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.chattingdata.ChattingManager;
import com.swsnack.catchhouse.data.chattingdata.pojo.Chatting;
import com.swsnack.catchhouse.data.chattingdata.pojo.Message;

public class RemoteChattingManager implements ChattingManager {

    @Override
    public void getChattingList(@NonNull String uuid, @NonNull ValueEventListener valueEventListener) {

    }

    @Override
    public void getChatData(@NonNull String chatRoomId, @NonNull ValueEventListener valueEventListener) {

    }

    @Override
    public void setChattingRoom(@NonNull Chatting chattingUser, @NonNull ValueEventListener valueEventListener) {

    }

    @Override
    public void setChatMessage(@NonNull Message message, @NonNull ValueEventListener valueEventListener) {

    }
}
