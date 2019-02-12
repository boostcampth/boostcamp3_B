package com.swsnack.catchhouse.data.roomdata.remote;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.swsnack.catchhouse.data.roomdata.RoomDataManager;
import com.swsnack.catchhouse.data.roomsdata.pojo.Room;

import java.util.ArrayList;
import java.util.List;

import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_ROOM;
import static com.swsnack.catchhouse.Constant.FirebaseKey.STORAGE_ROOM_IMAGE;
import static com.swsnack.catchhouse.Constant.PostException.NETWORK_ERROR;

public class AppRoomDataManager implements RoomDataManager {

    private DatabaseReference db;
    private StorageReference fs;

    private static AppRoomDataManager INSTANCE;

    public static synchronized AppRoomDataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppRoomDataManager();
        }
        return INSTANCE;
    }

    private AppRoomDataManager() {
        db = FirebaseDatabase.getInstance().getReference().child(DB_ROOM);
        fs = FirebaseStorage.getInstance().getReference().child(STORAGE_ROOM_IMAGE);
    }

    @Override
    public void createKey(@NonNull OnSuccessListener<String> onSuccessListener,
                          @NonNull OnFailureListener onFailureListener) {
        db.push().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                if (key != null) {
                    onSuccessListener.onSuccess(key);
                } else {
                    onFailureListener.onFailure(new RuntimeException(NETWORK_ERROR));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailureListener.onFailure(new RuntimeException(NETWORK_ERROR));
            }
        });
    }

    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<byte[]> imageList,
                                @NonNull OnSuccessListener<List<String>> onSuccessListener,
                                @NonNull OnFailureListener onFailureListener) {

        List<String> list = new ArrayList<>();
        List<StorageReference> innerRef = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            innerRef.add(fs.child(uuid + "/" + i));
        }
        UploadTask uploadTask = innerRef.get(0).putBytes(imageList.remove(0));

        Continuation<UploadTask.TaskSnapshot, Task<Uri>> continuation = task -> {
            if (!task.isSuccessful()) {
                onFailureListener.onFailure(new RuntimeException(NETWORK_ERROR));
            }
            return innerRef.remove(0).getDownloadUrl();
        };


        OnSuccessListener<Uri> loopListener = new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                list.add(uri.toString());

                if (!imageList.isEmpty()) {
                    innerRef.get(0).putBytes(imageList.remove(0))
                            .continueWithTask(continuation)
                            .addOnSuccessListener(this)
                            .addOnFailureListener(onFailureListener);
                } else {
                    onSuccessListener.onSuccess(list);
                }
            }
        };

        uploadTask
                .continueWithTask(continuation)
                .addOnSuccessListener(loopListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void uploadRoomData(@NonNull String uuid, @NonNull Room room,
                               @NonNull OnSuccessListener<Void> onSuccessListener,
                               @NonNull OnFailureListener onFailureListener) {
        db.child(uuid).setValue(room)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void readRoomData(@NonNull String uuid,
                             @NonNull OnSuccessListener<Room> onSuccessListener,
                             @NonNull OnFailureListener onFailureListener) {

        db.child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Room room = dataSnapshot.getValue(Room.class);
                onSuccessListener.onSuccess(room);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailureListener.onFailure(new RuntimeException(databaseError.getMessage()));
            }
        });
    }
}