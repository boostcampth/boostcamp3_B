package com.swsnack.catchhouse.firebase;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;

import androidx.annotation.NonNull;

public class StorageHelper {

    private StorageReference mStorageReference;
    private Uri mData;

    public StorageHelper(@NonNull StorageReference storageReference,
                         @NonNull Uri data) {

        this.mStorageReference = storageReference;
        this.mData = data;
    }

    public void getStorageStatus(@NonNull OnSuccessListener onSuccessListener,
                                 @NonNull OnFailedListener onFailedListener) {

        UploadTask uploadTask = mStorageReference.putFile(mData);
        uploadTask.continueWithTask(task -> {
            if(!task.isSuccessful()) {
                onFailedListener.onFailed(task.getException());
                return null;
            }
            return mStorageReference.getDownloadUrl();
        }).addOnSuccessListener(onSuccessListener::onSuccess)
                .addOnFailureListener(onFailedListener::onFailed);
    }


}