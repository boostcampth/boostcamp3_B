package com.swsnack.catchhouse.data.chattingdata.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.chattingdata.ChattingManager;
import com.swsnack.catchhouse.data.chattingdata.model.Chatting;
import com.swsnack.catchhouse.data.chattingdata.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.swsnack.catchhouse.Constant.ExceptionReason.NOT_SIGNED_USER;
import static com.swsnack.catchhouse.Constant.FirebaseKey.CHATTING;
import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_USER;
import static com.swsnack.catchhouse.Constant.FirebaseKey.MESSAGE;

public class RemoteChattingManager implements ChattingManager {

    private DatabaseReference db;
    private Query mChattingListQuery;
    private Query mMessageListQuery;
    private ValueEventListener mChattingObservingListener;
    private ValueEventListener mMessageObservingListener;

    private static RemoteChattingManager INSTANCE;

    public static synchronized RemoteChattingManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteChattingManager();
        }
        return INSTANCE;
    }

    private RemoteChattingManager() {
        this.db = FirebaseDatabase.getInstance().getReference().child(CHATTING);
    }

    @Override
    public void getChattingRoom(@NonNull String destinationUuid,
                                @NonNull OnSuccessListener<String> onSuccessListener,
                                @NonNull OnFailureListener onFailureListener) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailureListener.onFailure(new RuntimeException(NOT_SIGNED_USER));
            return;
        }

        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.orderByChild(DB_USER + "/" + uuid)
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            setChattingRoom(destinationUuid, onSuccessListener, onFailureListener);
                            return;
                        }

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Chatting chatting = snapshot.getValue(Chatting.class);
                            if (chatting.getUsers().containsKey(destinationUuid)) {
                                onSuccessListener.onSuccess(snapshot.getKey());
                                return;
                            }
                        }
                        setChattingRoom(destinationUuid, onSuccessListener, onFailureListener);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onFailureListener.onFailure(databaseError.toException());
                    }
                });
    }

    @Override
    public void getChattingList(@NonNull OnSuccessListener<List<Chatting>> onSuccessListener, @NonNull OnFailureListener onFailureListener) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailureListener.onFailure(new RuntimeException(NOT_SIGNED_USER));
            return;
        }

        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mChattingListQuery
                = db.orderByChild(DB_USER + "/" + uuid).equalTo(true);

        mChattingObservingListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Chatting> chattingList = new ArrayList<>();
                if (dataSnapshot.getValue() == null) {
                    onSuccessListener.onSuccess(chattingList);
                    return;
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chatting chatting = snapshot.getValue(Chatting.class);
                    chatting.setRoomUid(snapshot.getKey());
                    chattingList.add(chatting);
                }
                onSuccessListener.onSuccess(chattingList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailureListener.onFailure(databaseError.toException());
            }
        };

        mChattingListQuery.addValueEventListener(mChattingObservingListener);
    }

    @Override
    public void cancelChattingModelObserving() {
        if (mChattingListQuery != null && mChattingObservingListener != null) {
            mChattingListQuery.removeEventListener(mChattingObservingListener);
            mChattingListQuery = null;
            mChattingObservingListener = null;
        }
    }

    @Override
    public void listeningForChangedChatMessage(@Nullable String chatRoomId,
                                               @NonNull OnSuccessListener<List<Message>> onSuccessListener,
                                               @NonNull OnFailureListener onFailureListener) {

        if (chatRoomId == null) {
            return;
        }

        mMessageListQuery =
                db.child(chatRoomId)
                        .child(MESSAGE);

        mMessageObservingListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Message> messages = new ArrayList<>();
                if (dataSnapshot.getValue() == null) {
                    onSuccessListener.onSuccess(messages);
                    return;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    messages.add(snapshot.getValue(Message.class));
                }

                onSuccessListener.onSuccess(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailureListener.onFailure(databaseError.toException());
            }
        };

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
                                @NonNull OnFailureListener onFailureListener) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailureListener.onFailure(new RuntimeException());
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
                        getChattingRoom(destinationUuid, onSuccessListener, onFailureListener))
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void setChatMessage(int messagesLength,
                               @Nullable String roomUid,
                               @NonNull String destinationUid,
                               @NonNull String content,
                               @NonNull OnSuccessListener<String> onSuccessListener,
                               @NonNull OnFailureListener onFailureListener) {

        if (roomUid == null) {
            getChattingRoom(destinationUid, newRoomId ->
                            setChatMessage(messagesLength, newRoomId, destinationUid, content, onSuccessListener, onFailureListener),
                    onFailureListener);
            return;
        }

        Map<String, Object> setMessageField = new HashMap<>();
        setMessageField.put(String.valueOf(messagesLength), new Message(FirebaseAuth.getInstance().getCurrentUser().getUid(), content));

        db.child(roomUid)
                .child(MESSAGE)
                .updateChildren(setMessageField)
                .addOnSuccessListener(success -> onSuccessListener.onSuccess(roomUid))
                .addOnFailureListener(onFailureListener);
    }
}
