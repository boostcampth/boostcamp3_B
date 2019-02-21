package com.swsnack.catchhouse.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.mapper.FirebaseMapper;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;

import androidx.annotation.NonNull;

public class DBValueHelper<T> implements ValueEventListener {

    private Class<T> mDataClass;
    private FirebaseMapper<T> mapper;
    private OnSuccessListener<T> mOnSuccessListener;
    private OnFailedListener mOnFailedListener;

    public DBValueHelper(@NonNull Class<T> data,
                         @NonNull OnSuccessListener onSuccessListener,
                         @NonNull OnFailedListener onFailedListener) {

        mOnSuccessListener = onSuccessListener;
        mOnFailedListener = onFailedListener;
        mDataClass = data;
    }

    public DBValueHelper(@NonNull FirebaseMapper mapper,
                         @NonNull OnSuccessListener onSuccessListener,
                         @NonNull OnFailedListener onFailedListener) {

        this.mapper = mapper;
        this.mOnSuccessListener = onSuccessListener;
        this.mOnFailedListener = onFailedListener;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (mDataClass != null) {
            mOnSuccessListener.onSuccess(dataSnapshot.getValue(mDataClass));
            return;
        }
        mOnSuccessListener.onSuccess(mapper.map(dataSnapshot));
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        mOnFailedListener.onFailed(databaseError.toException());
    }
}
