package com.swsnack.catchhouse.data.db.room.remote;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.pojo.Room;

import java.util.ArrayList;
import java.util.List;

import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_ROOM;
import static com.swsnack.catchhouse.Constant.FirebaseKey.STORAGE_ROOM_IMAGE;
import static com.swsnack.catchhouse.Constant.PostException.NETWORK_ERROR;

public class AppRoomRemoteDataManager implements RoomDataManager {

    private DatabaseReference db;
    private StorageReference fs;

    private static AppRoomRemoteDataManager INSTANCE;

    public static synchronized AppRoomRemoteDataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppRoomRemoteDataManager();
        }
        return INSTANCE;
    }

    private AppRoomRemoteDataManager() {
        db = FirebaseDatabase.getInstance().getReference().child(DB_ROOM);
        fs = FirebaseStorage.getInstance().getReference().child(STORAGE_ROOM_IMAGE);
    }

    @Override
    public void createKey(@NonNull OnSuccessListener<String> onSuccessListener,
                          @NonNull OnFailedListener onFailedListener) {
        db.push().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                if (key != null) {
                    onSuccessListener.onSuccess(key);
                } else {
                    onFailedListener.onFailed(new RuntimeException(NETWORK_ERROR));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailedListener.onFailed(new RuntimeException(NETWORK_ERROR));
            }
        });
    }


    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<byte[]> imageList,
                                @NonNull OnSuccessListener<List<String>> onSuccessListener,
                                @NonNull OnFailedListener onFailedListener) {

        List<String> downloadUrl = new ArrayList<>();

        int size = imageList.size();
        int count = 0;

        for (byte[] image : imageList) {
            StorageReference ref = fs.child(uuid + "/" + count++);
            UploadTask uploadTask = ref.putBytes(image);

            uploadTask.continueWithTask(
                    task -> {
                        if (!task.isSuccessful()) {
                            downloadUrl.add("");
                            ref.delete();
                        }

                        return ref.getDownloadUrl();
                    }
            ).addOnSuccessListener(url -> {
                downloadUrl.add(url.toString());

                if (size == downloadUrl.size()) {
                    onSuccessListener.onSuccess(downloadUrl);
                }

            }).addOnFailureListener(onFailedListener::onFailed);
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
                onSuccessListener.onSuccess(room);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailedListener.onFailed(new RuntimeException(databaseError.getMessage()));
            }
        });
    }
}