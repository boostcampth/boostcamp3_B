package com.boostcampa2.catchhouse.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import io.reactivex.Single;

public class DataConverter {

    public static Bitmap getScaledBitmap(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.7), (int) (bitmap.getHeight() * 0.7), true);
    }

    public static Single<byte[]> getByteArray(Bitmap bitmap) {
        return Single.defer(() ->
                Single.create(subscriber -> {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    subscriber.onSuccess(outputStream.toByteArray());
                }));
    }
}
