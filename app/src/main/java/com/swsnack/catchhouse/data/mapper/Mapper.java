package com.swsnack.catchhouse.data.mapper;

public interface Mapper<T, K> {

    K map(T from);
}
