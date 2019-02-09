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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;
import com.swsnack.catchhouse.util.DataConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.DUPLICATE;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.DUPLICATE_NICK_NAME;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.FAILED_UPDATE;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.NOT_SIGNED_USER;
import static com.swsnack.catchhouse.constants.Constants.FirebaseKey.DB_USER;
import static com.swsnack.catchhouse.constants.Constants.FirebaseKey.NICK_NAME;
import static com.swsnack.catchhouse.constants.Constants.FirebaseKey.STORAGE_PROFILE;

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
    public void uploadProfile(@NonNull String uuid, @NonNull Uri imageUri, @NonNull OnSuccessListener<Uri> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
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
    public void updateProfile(@NonNull String uuid, @NonNull Uri uri, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        getUserFromSingleSnapShot(uuid,
                user -> uploadProfile(uuid, uri,
                        remoteUrl -> {
                            user.setProfile(remoteUrl.toString());
                            setUser(uuid, user,
                                    onSuccessListener,
                                    onFailureListener);
                        }, onFailureListener),
                onFailureListener);
    }


    @Override
    public void setUser(@NonNull String uuid, @NonNull User user, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        db.child(uuid)
                .setValue(user)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void deleteUserData(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        FirebaseDatabase.getInstance()
                .getReference(DB_USER)
                .child(uuid)
                .removeValue()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
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
    public void updateUser(@NonNull String uuid,
                           @NonNull Map<String, Object> updateFields,
                           @NonNull OnSuccessListener<Void> onSuccessListener,
                           @NonNull OnFailureListener onFailureListener) {

        db.child(uuid)
                .updateChildren(updateFields)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void updateNickName(@NonNull String uuid,
                               @NonNull String changeNickName,
                               @NonNull OnSuccessListener<Void> onSuccessListener,
                               @NonNull OnFailureListener onFailureListener) {

        findUserByQueryString(NICK_NAME,
                changeNickName,
                result -> {
                    if (result == null) {
                        Map<String, Object> updateFields = new HashMap<>();
                        updateFields.put(NICK_NAME, changeNickName);
                        updateUser(uuid, updateFields, onSuccessListener, onFailureListener);
                        return;
                    }
                    onFailureListener.onFailure(new RuntimeException(DUPLICATE_NICK_NAME));
                },
                onFailureListener);
    }
}
