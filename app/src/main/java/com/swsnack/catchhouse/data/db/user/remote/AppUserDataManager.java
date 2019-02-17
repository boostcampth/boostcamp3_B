package com.swsnack.catchhouse.data.db.user.remote;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.data.db.user.UserDataManager;
import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.model.User;
import com.swsnack.catchhouse.util.DataConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.swsnack.catchhouse.Constant.ExceptionReason.DUPLICATE;
import static com.swsnack.catchhouse.Constant.ExceptionReason.FAILED_UPDATE;
import static com.swsnack.catchhouse.Constant.ExceptionReason.NOT_SIGNED_USER;
import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_ROOM;
import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_USER;
import static com.swsnack.catchhouse.Constant.FirebaseKey.SIGNED;
import static com.swsnack.catchhouse.Constant.FirebaseKey.STORAGE_PROFILE;
import static com.swsnack.catchhouse.Constant.FirebaseKey.STORAGE_ROOM_IMAGE;

public class AppUserDataManager implements UserDataManager {

    private DatabaseReference db;
    private StorageReference fs;
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
        fs = FirebaseStorage.getInstance().getReference().child(STORAGE_PROFILE);
        mAppContext = AppApplication.getAppContext();
    }

    private void uploadProfile(@NonNull String uuid,
                               @NonNull Uri imageUri,
                               @NonNull OnSuccessListener<Uri> onSuccessListener,
                               @NonNull OnFailedListener onFailedListener) {

        StorageReference reference = fs.child(uuid);
        getProfile(imageUri,
                bitmap -> {
                    try {
                        UploadTask uploadTask = reference.putBytes(DataConverter.getByteArray(bitmap));
                        uploadTask.continueWithTask(task -> {
                            if (!task.isSuccessful()) {
                                onFailedListener.onFailed(new Exception(FAILED_UPDATE));
                                return null;
                            }
                            return reference.getDownloadUrl();
                        }).addOnSuccessListener(onSuccessListener::onSuccess)
                                .addOnFailureListener(onFailedListener::onFailed);
                    } catch (IOException e) {
                        onFailedListener.onFailed(e);
                    }
                },
                onFailedListener);
    }

    @Override
    public void deleteUser(@NonNull String uuid,
                            @NonNull OnSuccessListener<Void> onSuccessListener,
                            @NonNull OnFailedListener onFailedListener) {

        Map<String, Object> deleteField = new HashMap<>();
        deleteField.put(SIGNED, 0);

        db.child(uuid)
                .updateChildren(deleteField)
                .addOnSuccessListener(onSuccessListener::onSuccess)
                .addOnFailureListener(onFailedListener::onFailed);
    }

    @Override
    public void getUserAndListeningForChanging(@NonNull String uuid,
                                               @NonNull OnSuccessListener<User> onSuccessListener,
                                               @NonNull OnFailedListener onFailedListener) {

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
                onFailedListener.onFailed(databaseError.toException());
            }
        });
    }

    @Override
    public void getUserFromSingleSnapShot(@NonNull String uuid,
                                          @NonNull OnSuccessListener<User> onSuccessListener,
                                          @NonNull OnFailedListener onFailedListener) {

        db.child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
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
                onFailedListener.onFailed(databaseError.toException());
            }
        });
    }

    @Override
    public void getProfile(@NonNull Uri uri, @NonNull OnSuccessListener<Bitmap> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        Glide.with(mAppContext)
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
                              @NonNull OnFailedListener OnFailedListener) {

        setUser(uuid, user, uri, onSuccessListener, OnFailedListener);
    }

    @Override
    public void updateUser(@NonNull String uuid,
                           @NonNull User user,
                           @NonNull OnSuccessListener<Void> onSuccessListener,
                           @NonNull OnFailedListener OnFailedListener) {

        this.setUser(uuid, user, onSuccessListener, OnFailedListener);
    }

    @Override
    public void setUser(@NonNull String uuid, @NonNull User user, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener OnFailedListener) {
        db.child(uuid)
                .setValue(user)
                .addOnSuccessListener(onSuccessListener::onSuccess)
                .addOnFailureListener(OnFailedListener::onFailed);
    }

    @Override
    public void setUser(@NonNull String uuid, @NonNull User user, @NonNull Uri uri, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener OnFailedListener) {
        uploadProfile(uuid, uri,
                storageUri -> {
                    user.setProfile(storageUri.toString());
                    setUser(uuid, user, onSuccessListener, OnFailedListener);
                },
                OnFailedListener);
    }

    @Override
    public void setUserNotAlreadySigned(@NonNull String uuid,
                                        @NonNull User user,
                                        @NonNull OnSuccessListener<Void> onSuccessListener,
                                        @NonNull OnFailedListener onFailedListener) {

        getUserFromSingleSnapShot(uuid,
                signedUser -> {
                    if (signedUser == null) {
                        setUser(uuid, user, onSuccessListener, onFailedListener);
                        return;
                    }
                    onSuccessListener.onSuccess(null);
                },
                error -> {
                    if (error.getMessage().equals(NOT_SIGNED_USER)) {
                        setUser(uuid, user, onSuccessListener, onFailedListener);
                    }
                    onFailedListener.onFailed(error);
                });

    }

    @Override
    public void findUserByQueryString(@NonNull String queryString,
                                      @NonNull String findValue,
                                      @NonNull OnSuccessListener<String> onSuccessListener,
                                      @NonNull OnFailedListener OnFailedListener) {

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
                        OnFailedListener.onFailed(databaseError.toException());
                    }
                });
    }
}