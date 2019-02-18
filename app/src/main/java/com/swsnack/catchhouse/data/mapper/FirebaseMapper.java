package com.swsnack.catchhouse.data.mapper;

import java.util.List;

public interface FirebaseMapper<T, K> extends Mapper<T, K> {

    List<K> mapToList(T from);
}
