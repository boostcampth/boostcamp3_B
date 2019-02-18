package com.swsnack.catchhouse.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class DBListValueHelper<T> implements ValueEventListener {

    private Class<T> mDataClass;
    private OnSuccessListener<List<T>> mOnSuccessListener;
    private OnFailedListener mOnFailedListener;

    public DBListValueHelper(Class<T> data, OnSuccessListener<List<T>> onSuccessListener, OnFailedListener onFailedListener) {
        mOnSuccessListener = onSuccessListener;
        mOnFailedListener = onFailedListener;
        mDataClass = data;
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        List<T> result = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            result.add(snapshot.getValue(mDataClass));
        }
        if(result.size() == 0) {
            mOnSuccessListener.onSuccess(null);
            return;
        }
        mOnSuccessListener.onSuccess(result);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        mOnFailedListener.onFailed(databaseError.toException());
    }
}
