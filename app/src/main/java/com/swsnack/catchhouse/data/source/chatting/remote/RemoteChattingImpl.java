package com.swsnack.catchhouse.data.source.chatting.remote;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.mapper.ChattingMapper;
import com.swsnack.catchhouse.data.mapper.MessageMapper;
import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.data.model.Message;
import com.swsnack.catchhouse.firebase.DBListValueHelper;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;
import com.swsnack.catchhouse.data.source.chatting.ChattingDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.swsnack.catchhouse.Constant.ExceptionReason.FAILED_LISTENING;
import static com.swsnack.catchhouse.Constant.ExceptionReason.NOT_SIGNED_USER;
import static com.swsnack.catchhouse.Constant.FirebaseKey.CHATTING;
import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_USER;
import static com.swsnack.catchhouse.Constant.FirebaseKey.MESSAGE;

public class RemoteChattingImpl implements ChattingDataSource {

    private DatabaseReference db;
    private Query mChattingListQuery;
    private Query mMessageListQuery;
    private ValueEventListener mChattingObservingListener;
    private ValueEventListener mMessageObservingListener;

    private static RemoteChattingImpl INSTANCE;

    public static synchronized RemoteChattingImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteChattingImpl();
        }
        return INSTANCE;
    }

    private RemoteChattingImpl() {
        this.db = FirebaseDatabase.getInstance().getReference().child(CHATTING);
    }

    @Override
    public void getChattingRoom(@NonNull String destinationUuid,
                                @NonNull OnSuccessListener<String> onSuccessListener,
                                @NonNull OnFailedListener onFailedListener) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailedListener.onFailed(new RuntimeException(NOT_SIGNED_USER));
            return;
        }

        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.orderByChild(DB_USER + "/" + uuid)
                .equalTo(true)
                .addListenerForSingleValueEvent(new DBListValueHelper<Chatting>(new ChattingMapper(),
                        roomKeys -> {
                            for (Chatting chatting : roomKeys) {
                                if (chatting.getUsers().containsKey(destinationUuid)) {
                                    onSuccessListener.onSuccess(chatting.getRoomUid());
                                    return;
                                }
                            }
                            onSuccessListener.onSuccess(null);
                        }, onFailedListener));

    }

    @Override
    public void listeningChattingListChanged(@NonNull OnSuccessListener<List<Chatting>> onSuccessListener, @NonNull OnFailedListener onFailedListener) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailedListener.onFailed(new RuntimeException(NOT_SIGNED_USER));
            return;
        }

        if (mChattingListQuery != null || mChattingObservingListener != null) {
            onFailedListener.onFailed(new DatabaseException(FAILED_LISTENING));
            return;
        }

        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mChattingListQuery
                = db.orderByChild(DB_USER + "/" + uuid).equalTo(true);

        mChattingObservingListener = new DBListValueHelper<>(new ChattingMapper(),
                onSuccessListener, onFailedListener);

        mChattingListQuery.addValueEventListener(mChattingObservingListener);
    }

    @Override
    public void cancelObservingChattingList() {
        if (mChattingListQuery != null && mChattingObservingListener != null) {
            mChattingListQuery.removeEventListener(mChattingObservingListener);
            mChattingListQuery = null;
            mChattingObservingListener = null;
        }
    }

    @Override
    public void listeningChatMessageChanged(@Nullable String chatRoomId,
                                            @NonNull OnSuccessListener<List<Message>> onSuccessListener,
                                            @NonNull OnFailedListener onFailedListener) {

        if (chatRoomId == null) {
            return;
        }

        if (mMessageListQuery != null || mMessageObservingListener != null) {
            onFailedListener.onFailed(new DatabaseException(FAILED_LISTENING));
            return;
        }

        mMessageListQuery =
                db.child(chatRoomId)
                        .child(MESSAGE);

        mMessageObservingListener = new DBListValueHelper<>(new MessageMapper(),
                onSuccessListener, onFailedListener);

        mMessageListQuery.addValueEventListener(mMessageObservingListener);
    }

    @Override
    public void cancelMessageModelObserving() {
        if (mMessageListQuery != null && mMessageObservingListener != null) {
            mMessageListQuery.removeEventListener(mMessageObservingListener);
            mMessageListQuery = null;
            mMessageObservingListener = null;
        }
    }

    @Override
    public void setChattingRoom(@NonNull String destinationUuid,
                                @NonNull OnSuccessListener<String> onSuccessListener,
                                @NonNull OnFailedListener onFailedListener) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailedListener.onFailed(new RuntimeException());
            return;
        }

        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Boolean> users = new HashMap<>();
        users.put(uuid, true);
        users.put(destinationUuid, true);

        db
                .push()
                .setValue(new Chatting(users))
                .addOnSuccessListener(success ->
                        getChattingRoom(destinationUuid, onSuccessListener, onFailedListener))
                .addOnFailureListener(onFailedListener::onFailed);
    }

    @Override
    public void setChatMessage(int messagesLength,
                               @Nullable String roomUid,
                               @NonNull String destinationUid,
                               @NonNull String content,
                               @NonNull OnSuccessListener<String> onSuccessListener,
                               @NonNull OnFailedListener onFailedListener) {

        if (roomUid == null) {
            getChattingRoom(destinationUid,
                    roomId -> {
                        if (roomId == null) {
                            setChattingRoom(destinationUid,
                                    newRoomId ->
                                            setChatMessage(messagesLength,
                                                    newRoomId,
                                                    destinationUid,
                                                    content,
                                                    onSuccessListener,
                                                    onFailedListener),
                                    onFailedListener);
                        }
                    }, onFailedListener);
            return;
        }

        Map<String, Object> setMessageField = new HashMap<>();
        setMessageField.put(String.valueOf(messagesLength), new Message(FirebaseAuth.getInstance().getCurrentUser().getUid(), content));

        db.child(roomUid)
                .child(MESSAGE)
                .updateChildren(setMessageField)
                .addOnSuccessListener(success -> onSuccessListener.onSuccess(roomUid))
                .addOnFailureListener(onFailedListener::onFailed);
    }
}
