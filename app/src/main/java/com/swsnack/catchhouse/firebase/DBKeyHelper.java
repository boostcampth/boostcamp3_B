package com.swsnack.catchhouse.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.mapper.FirebaseMapper;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;

import androidx.annotation.NonNull;

public class DBKeyHelper<T> implements ValueEventListener {

    private FirebaseMapper mMapper;
    private OnSuccessListener<String> mOnSuccessListener;
    private OnFailedListener mOnFailedListener;

    public DBKeyHelper(@NonNull FirebaseMapper mapper,
                       @NonNull OnSuccessListener<String> onSuccessListener,
                       @NonNull OnFailedListener onFailedListener) {

        mMapper = mapper;
        mOnSuccessListener = onSuccessListener;
        mOnFailedListener = onFailedListener;
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mOnSuccessListener.onSuccess(mMapper.mapFromKey(dataSnapshot));
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        mOnFailedListener.onFailed(databaseError.toException());
    }
}
