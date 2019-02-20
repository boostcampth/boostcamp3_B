package com.swsnack.catchhouse.repository.room.remote;

import android.app.Application;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.firebase.StorageHelper;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_ROOM;
import static com.swsnack.catchhouse.Constant.FirebaseKey.STORAGE_ROOM_IMAGE;

public class RoomDataImpl implements RemoteRoomDataSource {

    private DatabaseReference db;
    private StorageReference fs;
    private Application mApplication;

    private static RoomDataImpl INSTANCE;

    public static synchronized RoomDataImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RoomDataImpl();
        }
        return INSTANCE;
    }

    private RoomDataImpl() {
        db = FirebaseDatabase.getInstance().getReference().child(DB_ROOM);
        fs = FirebaseStorage.getInstance().getReference().child(STORAGE_ROOM_IMAGE);
        mApplication = AppApplication.getAppContext();
    }

    @Override
    public String createKey() {
        return db.push().getKey();
    }

    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<Uri> imageList,
                                @NonNull OnSuccessListener<List<String>> onSuccessListener,
                                @NonNull OnFailedListener onFailedListener) {

        List<String> downloadUrls = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            int count = i;
            new StorageHelper(fs.child(uuid + "/" + i), imageList.get(i))
                    .getStorageStatus(uri -> {
                        if (uri != null) {
                            downloadUrls.add(uri.toString());
                        }
                        if (count == imageList.size() - 1) {
                            onSuccessListener.onSuccess(downloadUrls);
                        }
                    }, onFailedListener);
        }
    }

    @Override
    public void setRoom(@NonNull String key, @NonNull Room room,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener) {
        db.child(key).setValue(room)
                .addOnSuccessListener(onSuccessListener::onSuccess)
                .addOnFailureListener(onFailedListener::onFailed);
    }

    @Override
    public void getRoom(@NonNull String key,
                        @NonNull OnSuccessListener<Room> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener) {

        db.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Room room = dataSnapshot.getValue(Room.class);
                if (room == null) {
                    onFailedListener.onFailed(new DatabaseException("not registered room"));
                }
                room.setKey(dataSnapshot.getKey());
                onSuccessListener.onSuccess(room);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailedListener.onFailed(new RuntimeException(databaseError.getMessage()));
            }
        });
    }

    @Override
    public void delete(@NonNull String key,
                       @NonNull Room room,
                       @NonNull OnSuccessListener<Void> onSuccessListener,
                       @NonNull OnFailedListener onFailedListener) {

        room.setDeleted(true);
        setRoom(key, room, onSuccessListener, onFailedListener);

    }
}