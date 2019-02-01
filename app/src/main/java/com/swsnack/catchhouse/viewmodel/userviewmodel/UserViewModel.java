package com.swsnack.catchhouse.viewmodel.userviewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;
import com.swsnack.catchhouse.util.DataConverter;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.IN_SUFFICIENT_INFO;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.NOT_SIGNED_USER;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.SHORT_PASSWORD;
import static com.swsnack.catchhouse.constants.Constants.FacebookData.GENDER;
import static com.swsnack.catchhouse.constants.Constants.FacebookData.NAME;
import static com.swsnack.catchhouse.constants.Constants.UserStatus.SIGN_IN_SUCCESS;
import static com.swsnack.catchhouse.constants.Constants.UserStatus.SIGN_UP_SUCCESS;

public class UserViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private ViewModelListener mListener;
    private MutableLiveData<String> mGender;
    public MutableLiveData<String> mEmail;
    public MutableLiveData<String> mPassword;
    public MutableLiveData<String> mNickName;
    public MutableLiveData<Bitmap> mProfile;

    UserViewModel(Application application, DataManager dataManager, ViewModelListener listener) {
        super(dataManager);
        this.mAppContext = application;
        this.mListener = listener;
        this.mGender = new MutableLiveData<>();
        this.mEmail = new MutableLiveData<>();
        this.mPassword = new MutableLiveData<>();
        this.mNickName = new MutableLiveData<>();
        this.mProfile = new MutableLiveData<>();
    }

    public void setGender(String gender) {
        mGender.setValue(gender);
    }

    public void setProfile(Uri uri) {
        mListener.isWorking();
        Glide.with(mAppContext).asBitmap().load(uri).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                mListener.onError(e);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                mListener.isFinished();
                mProfile.postValue(resource);
                return true;
            }
        }).submit();
    }

    public void signInWithGoogle(Intent data) {
        mListener.isWorking();
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount account = task.getResult();
        User user = new User(account.getDisplayName());
        if (account.getPhotoUrl() != null) {
            user.setProfile(account.getPhotoUrl().toString());
        }
        signUpWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null), user);
    }

    public void signInWithFacebook(LoginResult loginResult, Uri uri) {
        mListener.isWorking();
        getDataManager().facebookUserProfile(loginResult.getAccessToken(),
                (result, response) -> {
                    if (result == null) {
                        mListener.onError(response.getError().getException());
                        return;
                    }
                    User user = new User(result.optString(NAME), result.optString(GENDER));
                    user.setProfile(uri.toString());
                    signUpWithCredential(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()), user);
                });
    }

    private void signUpWithCredential(AuthCredential authCredential, User user) {
        getDataManager()
                .firebaseSignUp(authCredential, result -> setUser(result.getUser().getUid(), user, SIGN_IN_SUCCESS), mListener::onError);
    }

    public void signUpWithEmail(View v) {
        if (mPassword.getValue() == null || mPassword.getValue().length() < 6) {
            mListener.onError(new InSufficientException(SHORT_PASSWORD));
            return;
        }
        if (mEmail.getValue() == null || mPassword.getValue() == null ||
                mNickName.getValue() == null || mGender.getValue() == null ||
                mEmail.getValue().trim().equals("") || mPassword.getValue().trim().equals("") ||
                mNickName.getValue().trim().equals("") || mGender.getValue() == null) {
            mListener.onError(new InSufficientException(IN_SUFFICIENT_INFO));
            return;
        }
        mListener.isWorking();
        getDataManager()
                .firebaseSignUp(mEmail.getValue(), mPassword.getValue()
                        , result -> {
                            User user = new User(mEmail.getValue(), mNickName.getValue(), mGender.getValue());
                            if (mProfile.getValue() != null) {
                                setProfile(result.getUser().getUid(), user);
                                return;
                            }
                            setUser(result.getUser().getUid(), user, SIGN_UP_SUCCESS);
                        }, mListener::onError);
    }

    private void setUser(String uuid, User user, String userStatusFlag) {
        getDataManager()
                .setUser(uuid, user, result -> mListener.onSuccess(userStatusFlag), error -> {
                    if (mProfile.getValue() != null) {
                        getDataManager().firebaseDeleteUser(result -> {}, mListener::onError);
                    }
                    getDataManager().deleteProfile(uuid, result -> mListener.isFinished(), mListener::onError);
                });
    }

    private void setProfile(String uuid, User user) {
        if (mProfile.getValue() == null) {
            setUser(uuid, user, SIGN_UP_SUCCESS);
            return;
        }
        getDataManager()
                .setProfile(uuid, DataConverter.getByteArray(DataConverter.getScaledBitmap(mProfile.getValue())),
                        uri -> {
                            user.setProfile(uri.toString());
                            setUser(uuid, user, SIGN_UP_SUCCESS);
                        }, mListener::onError);
    }

    public void signInWithEmail(View v) {
        if (mEmail.getValue() == null || mPassword.getValue() == null
                || mEmail.getValue().trim().equals("") || mPassword.getValue().trim().equals("")) {
            mListener.onError(new InSufficientException(IN_SUFFICIENT_INFO));
            return;
        }

        if (mPassword.getValue() == null || mPassword.getValue().length() < 6) {
            mListener.onError(new InSufficientException(SHORT_PASSWORD));
            return;
        }
        mListener.isWorking();
    }

    public void deleteUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            mListener.onError(new FirebaseException(NOT_SIGNED_USER));
            return;
        }
        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getDataManager()
                .firebaseDeleteUser(result ->
                                getDataManager().deleteProfile(uuid, deleteUserResult ->
                                                getDataManager().deleteProfile(uuid, deleteProfileResult -> mListener.isFinished(), mListener::onError),
                                        mListener::onError),
                        mListener::onError);
//        );
    }
}
