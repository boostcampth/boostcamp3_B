package com.swsnack.catchhouse.data.userdata.remote;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.swsnack.catchhouse.constants.Constants;
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;

import io.reactivex.Completable;
import io.reactivex.Single;

import static com.swsnack.catchhouse.constants.Constants.FirebaseKey.STORAGE_PROFILE;

public class AppUserDataManager implements UserDataManager {

    private DatabaseReference db;
    private StorageReference fs;

    private static class UserRemoteDataHelper {
        private static final AppUserDataManager INSTANCE = new AppUserDataManager();
    }

    public static AppUserDataManager getInstance() {
        return UserRemoteDataHelper.INSTANCE;

    }

    private AppUserDataManager() {
        db = FirebaseDatabase.getInstance().getReference().child(Constants.FirebaseKey.DB_USER);
        fs = FirebaseStorage.getInstance().getReference().child(Constants.FirebaseKey.STORAGE_PROFILE);
    }

    @NonNull
    @Override
    public Single<User> getUser(@NonNull String uuid) {
        return Single.defer(() -> Single.create(subscriber -> db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                subscriber.onSuccess(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                subscriber.onError(databaseError.toException());
            }
        })));
    }

    @NonNull
    @Override
    public Single<String> setProfile(@NonNull String uuid, @NonNull byte[] profile) {
        return Single.defer(() ->
                Single.create(subscriber -> {
                    StorageReference ref = fs.child(uuid);
                    UploadTask uploadTask = ref.putBytes(profile);
                    uploadTask.addOnSuccessListener(snapshot -> uploadTask.continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            subscriber.onError(task.getException());
                        }
                        return ref.getDownloadUrl();
                    }).addOnSuccessListener(uri -> subscriber.onSuccess(uri.toString()))
                            .addOnFailureListener(subscriber::onError));
                })
        );
    }

    @NonNull
    @Override
    public Completable setUser(@NonNull String uuid, @NonNull User user) {
        return Completable.defer(() -> Completable.create(subscriber -> {
            db.child(uuid).setValue(user);
            subscriber.onComplete();
        }));
    }

    @NonNull
    @Override
    public Completable deleteUser(@NonNull String uuid) {
        return Completable.defer(() ->
                Completable.create(subscriber -> FirebaseStorage.getInstance()
                        .getReference(STORAGE_PROFILE).child(uuid).delete()
                        .addOnSuccessListener(result -> subscriber.onComplete())
                        .addOnFailureListener(subscriber::onError)));
    }
}
