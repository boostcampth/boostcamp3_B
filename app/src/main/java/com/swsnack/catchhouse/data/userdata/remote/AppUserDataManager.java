package com.swsnack.catchhouse.data.userdata.remote;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;
import com.swsnack.catchhouse.util.DataConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.DUPLICATE_NICK_NAME;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.FAILED_LOAD_IMAGE;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.NOT_SIGNED_USER;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.SIGN_UP_EXCEPTION;
import static com.swsnack.catchhouse.constants.Constants.FirebaseKey.DB_USER;
import static com.swsnack.catchhouse.constants.Constants.FirebaseKey.NICK_NAME;
import static com.swsnack.catchhouse.constants.Constants.FirebaseKey.STORAGE_PROFILE;

public class AppUserDataManager implements UserDataManager {

    private DatabaseReference db;
    private StorageReference fs;
    private Application mAppContext;

    private static AppUserDataManager INSTANCE;

    public static synchronized AppUserDataManager getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new AppUserDataManager(application);
        }
        return INSTANCE;
    }

    // FIXME 생성자에서 Application을 가져올 필요가 없습니다.
    // Application클래스를 생성하고 singleton으로 만들고 해당 Context를 사용하면 인자로 받을 필요 없습니다.
    private AppUserDataManager(Application application) {
        db = FirebaseDatabase.getInstance().getReference().child(DB_USER);
        fs = FirebaseStorage.getInstance().getReference().child(STORAGE_PROFILE);
        mAppContext = application;
    }

    @Override
    public void getUserForListening(@NonNull String uuid, @NonNull ValueEventListener valueEventListener) {
        db.child(uuid).addValueEventListener(valueEventListener);
    }

    @Override
    public void getUserForSingle(@NonNull String uuid, @NonNull ValueEventListener valueEventListener) {
        db.child(uuid).addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void setProfile(@NonNull String uuid, @NonNull byte[] profile, @NonNull OnSuccessListener<Uri> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        StorageReference reference = fs.child(uuid);
        // FIXME 근본적인 Profile 이미지 set하는 로직을 대대적으로 수정해야할것 같습니다.
        // 이미지데이터를 byte 스트림으로 계속 넘겨주는것이 아니라 이미지의 uri만 넘겨주어 실제 업로드할때는 이 uri를 가지고 업로드 하도록 해주세요
        // reference.putFile(profileUri) 와 같은 방식으로 사용할 수 있습니다.
        UploadTask uploadTask = reference.putBytes(profile);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                onFailureListener.onFailure(new Exception(SIGN_UP_EXCEPTION));
            }
            return reference.getDownloadUrl();
        })
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void getProfile(@NonNull Uri uri, RequestListener<Bitmap> requestListener) {
        Glide.with(mAppContext).asBitmap().load(uri).listener(requestListener).submit();
    }

    @Override
    public void updateProfile(@NonNull String uuid, @NonNull Uri uri, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        getUserForSingle(uuid, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    onFailureListener.onFailure(new DatabaseException(NOT_SIGNED_USER));
                    return;
                }
                getProfile(uri, new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        onFailureListener.onFailure(new GlideException(FAILED_LOAD_IMAGE));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        try {
                            setProfile(uuid, DataConverter.getByteArray(DataConverter.getScaledBitmap(resource)),
                                    uri -> {
                                        user.setProfile(uri.toString());
                                        setUser(uuid, user, onSuccessListener, onFailureListener);
                                    },
                                    onFailureListener);
                        } catch (IOException e) {
                            e.printStackTrace();
                            onFailureListener.onFailure(new GlideException(FAILED_LOAD_IMAGE));
                            return false;
                        }
                        return true;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailureListener.onFailure(new DatabaseException(NOT_SIGNED_USER));
            }
        });
    }


    @Override
    public void setUser(@NonNull String uuid, @NonNull User user, @Nullable OnSuccessListener<Void> onSuccessListener, @Nullable OnFailureListener onFailureListener) {
        db.child(uuid)
                .setValue(user)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void deleteUserData(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        FirebaseDatabase.getInstance()
                .getReference(DB_USER)
                .child(uuid)
                .removeValue()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void deleteProfile(@NonNull String uuid, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        FirebaseStorage.getInstance()
                .getReference(STORAGE_PROFILE)
                .child(uuid)
                .delete()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void queryUserBy(@NonNull String queryBy, @NonNull String findValue, @NonNull ValueEventListener valueEventListener) {
        db.orderByChild(queryBy)
                .equalTo(findValue)
                .addListenerForSingleValueEvent(valueEventListener);
    }


    @Override
    public void updateUser(@NonNull String uuid, @NonNull Map<String, Object> updateFields, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        db.child(uuid)
                .updateChildren(updateFields)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void updateNickName(@NonNull String uuid, @NonNull String changeNickName, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener) {
        queryUserBy(NICK_NAME, changeNickName, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    /* in this block, no one has same nickName*/
                    Map<String, Object> updateFields = new HashMap<>();
                    updateFields.put(NICK_NAME, changeNickName);
                    updateUser(uuid, updateFields, onSuccessListener, onFailureListener);
                } else {
                    onFailureListener.onFailure(new RuntimeException(DUPLICATE_NICK_NAME));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFailureListener.onFailure(databaseError.toException());
            }
        });
    }
}
