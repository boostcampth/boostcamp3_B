package com.swsnack.catchhouse.data.userdata;

import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;

public interface APIManager {

    // FIXME Manager의 interface설계 개념자체가 잘못된것 같습니다.
    // APIManager단계에서는 signUp()가 아닌 signUp()과 같은 더 추상화된 개념으로 작성되어야 합니다.
    // 오프라인 튜터링에서 더 자세히 설명드리겠습니다.
    void signUp(@NonNull AuthCredential authCredential, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void signUp(@NonNull String email, @NonNull String password, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void getUserInfoFromFacebook(@NonNull AccessToken accessToken, GraphRequest.GraphJSONObjectCallback facebookUserDataCallback);

    void signIn(@NonNull String email, @NonNull String password, @NonNull OnSuccessListener<AuthResult> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void deleteUser(@NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void updatePassword(@NonNull String oldPassword, @NonNull String newPassword, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);
}
