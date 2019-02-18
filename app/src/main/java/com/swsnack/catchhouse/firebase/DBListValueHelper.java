package com.swsnack.catchhouse.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.mapper.FirebaseMapper;
import com.swsnack.catchhouse.data.mapper.Mapper;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class DBListValueHelper<T> implements ValueEventListener {

    private FirebaseMapper mMapper;
    private OnSuccessListener<List<T>> mOnSuccessListener;
    private OnFailedListener mOnFailedListener;

    public DBListValueHelper(@NonNull FirebaseMapper mapper,
                             @NonNull OnSuccessListener<List<T>> onSuccessListener,
                             @NonNull OnFailedListener onFailedListener) {

        mMapper = mapper;
        mOnSuccessListener = onSuccessListener;
        mOnFailedListener = onFailedListener;
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        List<T> result = new ArrayList<>();
//        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//            result.add((T) mMapper.map(snapshot));
//        }
        mOnSuccessListener.onSuccess(mMapper.mapToList(dataSnapshot));
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        mOnFailedListener.onFailed(databaseError.toException());
    }
}
