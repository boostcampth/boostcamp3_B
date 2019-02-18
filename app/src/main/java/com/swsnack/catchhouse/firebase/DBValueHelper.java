package com.swsnack.catchhouse.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;

import androidx.annotation.NonNull;

public class DBValueHelper<T> implements ValueEventListener {

    private Class<T> mDataClass;
    private OnSuccessListener<T> mOnSuccessListener;
    private OnFailedListener mOnFailedListener;

    public DBValueHelper(Class<T> data, OnSuccessListener onSuccessListener, OnFailedListener onFailedListener) {
        mOnSuccessListener = onSuccessListener;
        mOnFailedListener = onFailedListener;
        mDataClass = data;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mOnSuccessListener.onSuccess(dataSnapshot.getValue(mDataClass));
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        mOnFailedListener.onFailed(databaseError.toException());
    }
}
