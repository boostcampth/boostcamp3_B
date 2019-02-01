package com.swsnack.catchhouse.data.userdata.remote;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;

import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.NOT_SIGNED_USER;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.SIGN_UP_EXCEPTION;
import static com.swsnack.catchhouse.constants.Constants.FirebaseKey.DB_USER;
import static com.swsnack.catchhouse.constants.Constants.FirebaseKey.STORAGE_PROFILE;

public class AppUserDataManager implements UserDataManager {

    private DatabaseReference db;
    private StorageReference fs;

    private static class Singleton {
        private static final AppUserDataManager INSTANCE = new AppUserDataManager();
    }

    public static AppUserDataManager getInstance() {
        return Singleton.INSTANCE;
    }

    private AppUserDataManager() {
        db = FirebaseDatabase.getInstance().getReference().child(DB_USER);
        fs = FirebaseStorage.getInstance().getReference().child(STORAGE_PROFILE);
    }

    @Override
    public void getUser(@NonNull String uuid, @NonNull ValueEventListener valueEventListener) {
        db.child(uuid).addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void setProfile(@NonNull String uuid, @NonNull byte[] profile, @NonNull OnSuccessListener<Uri> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        StorageReference reference = fs.child(uuid);
        UploadTask uploadTask = reference.putBytes(profile);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                onFailureListener.onFailure(new Exception(SIGN_UP_EXCEPTION));
            }
            return reference.getDownloadUrl();
        })
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }


    @Override
    public void setUser(@NonNull String uuid, @NonNull User user, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        db.child(uuid).setValue(user)
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
    public void deleteUser(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            onFailureListener.onFailure(new FirebaseException(NOT_SIGNED_USER));
            return;
        }
        getUser(uuid, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    onFailureListener.onFailure(new FirebaseException(NOT_SIGNED_USER));
                    return;
                }
                deleteUserData(uuid, deleteDBResult ->
                        deleteProfile(uuid, deleteProfileResult ->
                                        FirebaseAuth.getInstance().getCurrentUser()
                                                .delete()
                                                .addOnSuccessListener(onSuccessListener)
                                                .addOnFailureListener(onFailureListener),
                                error -> setUser(uuid, user, failedToDeleteData -> onFailureListener.onFailure(error), onFailureListener)), onFailureListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailureListener.onFailure(databaseError.toException());
            }
        });
    }
}
