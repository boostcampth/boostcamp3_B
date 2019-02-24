package com.swsnack.catchhouse.data.source.user.remote;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.swsnack.catchhouse.data.model.User;
import com.swsnack.catchhouse.firebase.DBValueHelper;
import com.swsnack.catchhouse.firebase.StorageHelper;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;
import com.swsnack.catchhouse.data.source.user.UserDataSource;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.swsnack.catchhouse.Constant.ExceptionReason.NOT_SIGNED_USER;
import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_USER;
import static com.swsnack.catchhouse.Constant.FirebaseKey.SIGNED;
import static com.swsnack.catchhouse.Constant.FirebaseKey.STORAGE_PROFILE;

public class UserDataImpl implements UserDataSource {

    private DatabaseReference db;
    private StorageReference fs;

    private static UserDataImpl INSTANCE;

    public static synchronized UserDataImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserDataImpl();
        }
        return INSTANCE;
    }

    private UserDataImpl() {
        db = FirebaseDatabase.getInstance().getReference().child(DB_USER);
        fs = FirebaseStorage.getInstance().getReference().child(STORAGE_PROFILE);
    }

    private void uploadProfile(@NonNull String uuid,
                               @NonNull Uri imageUri,
                               @NonNull OnSuccessListener<Uri> onSuccessListener,
                               @NonNull OnFailedListener onFailedListener) {

        StorageReference reference = fs.child(uuid);
        new StorageHelper(reference, imageUri)
                .getStorageStatus(onSuccessListener, onFailedListener);
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

        db.child(uuid)
                .addValueEventListener(new DBValueHelper<>(User.class, onSuccessListener, onFailedListener));
    }

    @Override
    public void getUserFromSingleSnapShot(@NonNull String uuid,
                                          @NonNull OnSuccessListener<User> onSuccessListener,
                                          @NonNull OnFailedListener onFailedListener) {

        db.child(uuid).addListenerForSingleValueEvent(new DBValueHelper<>(User.class, onSuccessListener, onFailedListener));
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
                        return;
                    }
                    onFailedListener.onFailed(error);
                });

    }
}