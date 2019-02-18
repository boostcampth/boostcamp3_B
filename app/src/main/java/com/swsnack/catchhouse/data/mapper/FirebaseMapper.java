package com.swsnack.catchhouse.data.mapper;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

public interface FirebaseMapper<T> extends Mapper<DataSnapshot, T> {

    List<T> mapToList(DataSnapshot from);

    String mapFromKey(DataSnapshot from);
}
