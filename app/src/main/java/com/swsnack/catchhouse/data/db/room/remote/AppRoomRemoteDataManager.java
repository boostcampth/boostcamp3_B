package com.swsnack.catchhouse.data.db.room.remote;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.util.DataConverter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_ROOM;
import static com.swsnack.catchhouse.Constant.FirebaseKey.STORAGE_ROOM_IMAGE;

public class AppRoomRemoteDataManager implements RoomDataManager {

    private DatabaseReference db;
    private StorageReference fs;
    private Application mApplication;

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
        List<StorageReference> refs = new ArrayList<>();

        imageUriToBitmap(imageList, byteList -> {
            int size = byteList.size();
            int count = 0;


            for (byte[] image : byteList) {
                StorageReference ref = fs.child(uuid + "/" + count++);
                refs.add(ref);
                UploadTask uploadTask = ref.putBytes(image);

                uploadTask.continueWithTask(
                        task -> {
                            if (!task.isSuccessful()) {
                                downloadUrls.add("");
                            }
                            return ref.getDownloadUrl();
                        }
                ).addOnSuccessListener(url -> {
                            downloadUrls.add(url.toString());

                            if (size == downloadUrls.size()) {
                                boolean errorFlag = false;

                                for (String downloadUrl : downloadUrls) {
                                    if (TextUtils.isEmpty(downloadUrl)) {
                                        for (StorageReference storageReference : refs) {
                                            storageReference.delete();
                                        }
                                        errorFlag = true;
                                        break;
                                    }
                                }

                                if (errorFlag) {
                                    onFailedListener.onFailed(new Exception("upload error"));
                                } else {
                                    onSuccessListener.onSuccess(downloadUrls);
                                }
                            }
                        }
                ).addOnFailureListener(__ ->
                        downloadUrls.add("")
                );
            }
        }, onFailedListener);
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

    private void imageUriToBitmap(List<Uri> uris,
                                  @NonNull OnSuccessListener<List<byte[]>> onSuccessListener,
                                  @NonNull OnFailedListener onFailedListener) {
        List<byte[]> bitmapBytesList = new ArrayList<>();

        for (Uri uri : uris) {
            Glide.with(mApplication)
                    .asBitmap()
                    .load(uri)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            onFailedListener.onFailed(e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            try {
                                byte[] bytes = DataConverter.getByteArray(DataConverter.getScaledBitmap(resource));
                                bitmapBytesList.add(bytes);

                                if (bitmapBytesList.size() == uris.size()) {
                                    onSuccessListener.onSuccess(bitmapBytesList);
                                }
                            } catch (Exception e) {
                                onFailedListener.onFailed(e);
                            }
                            return false;
                        }
                    }).submit();

        }
    }
}