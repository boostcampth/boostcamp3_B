package com.swsnack.catchhouse.data.userdata.remote;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.swsnack.catchhouse.data.roomsdata.pojo.Room;
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.model.User;
import com.swsnack.catchhouse.util.DataConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.swsnack.catchhouse.Constant.ExceptionReason.DUPLICATE;
import static com.swsnack.catchhouse.Constant.ExceptionReason.DUPLICATE_NICK_NAME;
import static com.swsnack.catchhouse.Constant.ExceptionReason.FAILED_UPDATE;
import static com.swsnack.catchhouse.Constant.ExceptionReason.NOT_SIGNED_USER;
import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_ROOM;
import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_USER;
import static com.swsnack.catchhouse.Constant.FirebaseKey.NICK_NAME;
import static com.swsnack.catchhouse.Constant.FirebaseKey.STORAGE_PROFILE;
import static com.swsnack.catchhouse.Constant.FirebaseKey.STORAGE_ROOM_IMAGE;
import static com.swsnack.catchhouse.Constant.PostException.NETWORK_ERROR;

public class AppUserDataManager implements UserDataManager {

    private DatabaseReference db;
    private DatabaseReference dbRooms;
    private StorageReference fs;
    private StorageReference fsRooms;
    private Application mAppContext;

    private static AppUserDataManager INSTANCE;

    public static synchronized AppUserDataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppUserDataManager();
        }
        return INSTANCE;
    }

    private AppUserDataManager() {
        db = FirebaseDatabase.getInstance().getReference().child(DB_USER);
        dbRooms = FirebaseDatabase.getInstance().getReference().child(DB_ROOM);
        fs = FirebaseStorage.getInstance().getReference().child(STORAGE_PROFILE);
        fsRooms = FirebaseStorage.getInstance().getReference().child(STORAGE_ROOM_IMAGE);
        mAppContext = AppApplication.getAppContext();
    }

    private void uploadProfile(@NonNull String uuid,
                               @NonNull Uri imageUri,
                               @NonNull OnSuccessListener<Uri> onSuccessListener,
                               @NonNull OnFailureListener onFailureListener) {

        StorageReference reference = fs.child(uuid);
        getProfile(imageUri,
                bitmap -> {
                    try {
                        UploadTask uploadTask = reference.putBytes(DataConverter.getByteArray(DataConverter.getScaledBitmap(bitmap)));
                        uploadTask.continueWithTask(task -> {
                            if (!task.isSuccessful()) {
                                onFailureListener.onFailure(new Exception(FAILED_UPDATE));
                                return null;
                            }
                            return reference.getDownloadUrl();
                        }).addOnSuccessListener(onSuccessListener)
                                .addOnFailureListener(onFailureListener);
                    } catch (IOException e) {
                        onFailureListener.onFailure(e);
                    }
                },
                onFailureListener);
    }

    private void deleteUser(@NonNull String uuid,
                            @NonNull OnSuccessListener<Void> onSuccessListener,
                            @NonNull OnFailureListener onFailureListener) {

        FirebaseDatabase.getInstance()
                .getReference(DB_USER)
                .child(uuid)
                .removeValue()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void getUserAndListeningForChanging(@NonNull String uuid,
                                               @NonNull OnSuccessListener<User> onSuccessListener,
                                               @NonNull OnFailureListener onFailureListener) {

        db.child(uuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    onSuccessListener.onSuccess(null);
                    return;
                }
                onSuccessListener.onSuccess(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailureListener.onFailure(databaseError.toException());
            }
        });
    }

    @Override
    public void getUserFromSingleSnapShot(@NonNull String uuid,
                                          @NonNull OnSuccessListener<User> onSuccessListener,
                                          @NonNull OnFailureListener onFailureListener) {

        db.child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    onFailureListener.onFailure(new DatabaseException(NOT_SIGNED_USER));
                    return;
                }
                onSuccessListener.onSuccess(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailureListener.onFailure(databaseError.toException());
            }
        });
    }

    @Override
    public void getProfile(@NonNull Uri uri, @NonNull OnSuccessListener<Bitmap> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        Glide.with(mAppContext)
                .asBitmap()
                .load(uri)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        onFailureListener.onFailure(e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        onSuccessListener.onSuccess(resource);
                        return true;
                    }
                }).submit();
    }

    @Override
    public void updateProfile(@NonNull String uuid,
                              @NonNull Uri uri,
                              @NonNull User user,
                              @NonNull OnSuccessListener<Void> onSuccessListener,
                              @NonNull OnFailureListener onFailureListener) {

        setUser(uuid, user, uri, onSuccessListener, onFailureListener);
    }

    @Override
    public void updateUser(@NonNull String uuid,
                           @NonNull User user,
                           @NonNull OnSuccessListener<Void> onSuccessListener,
                           @NonNull OnFailureListener onFailureListener) {

        this.setUser(uuid, user, onSuccessListener, onFailureListener);
    }

    @Override
    public void setUser(@NonNull String uuid, @NonNull User user, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        db.child(uuid)
                .setValue(user)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void setUser(@NonNull String uuid, @NonNull User user, @NonNull Uri uri, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        uploadProfile(uuid, uri,
                storageUri -> {
                    user.setProfile(storageUri.toString());
                    setUser(uuid, user, onSuccessListener, onFailureListener);
                },
                onFailureListener);
    }

    @Override
    public void deleteUserData(@NonNull String uuid,
                               @NonNull User user,
                               @NonNull OnSuccessListener<Void> onSuccessListener,
                               @NonNull OnFailureListener onFailureListener) {

        if (user.getProfile() == null) {
            deleteUser(uuid, onSuccessListener, onFailureListener);
            return;
        }

        deleteUser(uuid,
                deleteDbSuccess -> deleteProfile(uuid, onSuccessListener, onFailureListener),
                onFailureListener);
    }

    @Override
    public void deleteProfile(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        FirebaseStorage.getInstance()
                .getReference(STORAGE_PROFILE)
                .child(uuid)
                .delete()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void findUserByQueryString(@NonNull String queryString,
                                      @NonNull String findValue,
                                      @NonNull OnSuccessListener<String> onSuccessListener,
                                      @NonNull OnFailureListener onFailureListener) {

        db.orderByChild(queryString)
                .equalTo(findValue)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            onSuccessListener.onSuccess(null);
                            return;
                        }
                        onSuccessListener.onSuccess(DUPLICATE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onFailureListener.onFailure(databaseError.toException());
                    }
                });
    }

    @Override
    public void createKey(@NonNull OnSuccessListener<String> onSuccessListener,
                          @NonNull OnFailureListener onFailureListener) {
        dbRooms.push().addListenerForSingleValueEvent(new ValueEventListener() {
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
            innerRef.add(fsRooms.child(uuid + "/" + i));
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
        dbRooms.child(uuid).setValue(room)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
}
