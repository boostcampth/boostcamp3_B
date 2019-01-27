package com.boostcampa2.catchhouse.data.userdata.remote;

import android.support.annotation.NonNull;

import com.boostcampa2.catchhouse.data.userdata.UserDataSource;
import com.boostcampa2.catchhouse.data.userdata.pojo.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.Completable;
import io.reactivex.Single;

public class UserRemoteData implements UserDataSource {

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");

    private static class UserRemoteDataHelper {
        private static final UserRemoteData INSTANCE = new UserRemoteData();
    }

    public static UserRemoteData getInstance() {
        return UserRemoteDataHelper.INSTANCE;

    }

    private UserRemoteData() {
    }

    @NonNull
    @Override
    public Single<User> getUser(String uuid) {

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
    public Completable setUser(User user) {
        return Completable.defer(() -> Completable.create(subscriber -> {
            db.setValue(user);
            subscriber.onComplete();
        }));
    }
}
