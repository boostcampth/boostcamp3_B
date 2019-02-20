package com.swsnack.catchhouse.repository;

@FunctionalInterface
public interface OnSuccessListener<T> {
    void onSuccess(T result);
}
