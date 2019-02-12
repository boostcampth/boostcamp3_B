package com.swsnack.catchhouse.viewmodel.userviewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.userdata.model.User;
import com.swsnack.catchhouse.util.StringUtil;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import static com.swsnack.catchhouse.Constant.SuccessKey.DELETE_USER_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.SIGN_IN_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.SIGN_UP_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.UPDATE_NICK_NAME_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.UPDATE_PASSWORD_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.UPDATE_PROFILE_SUCCESS;

public class UserViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private ViewModelListener mListener;
    private Uri mProfileUri;
    private MutableLiveData<String> mGender;
    private User mUser;
    public MutableLiveData<Boolean> mIsSigned;
    public MutableLiveData<String> mEmail;
    public MutableLiveData<String> mPassword;
    public MutableLiveData<String> mNickName;
    public MutableLiveData<Bitmap> mProfile;

    UserViewModel(Application application, DataManager dataManager, ViewModelListener listener) {
        super(dataManager);
        this.mAppContext = application;
        this.mListener = listener;
        this.mGender = new MutableLiveData<>();
        this.mIsSigned = new MutableLiveData<>();
        this.mEmail = new MutableLiveData<>();
        this.mPassword = new MutableLiveData<>();
        this.mNickName = new MutableLiveData<>();
        this.mProfile = new MutableLiveData<>();
        mIsSigned.setValue(false);
    }

    public void setGender(String gender) {
        mGender.setValue(gender);
    }

    public LiveData getGender() {
        return mGender;
    }

    private String getStringFromResource(int stringResource) {
        return mAppContext.getString(stringResource);
    }

    public void getProfileFromUri(Uri uri) {
        mListener.isWorking();
        mProfileUri = uri;
        getDataManager()
                .getProfile(uri,
                        bitmap -> {
                            mListener.isFinished();
                            mProfile.setValue(bitmap);
                        },
                        error -> mListener.onError(StringUtil.getStringFromResource(R.string.snack_failed_load_image)));
    }

    public void getUser() {
        mIsSigned.setValue(true);
        getDataManager()
                .getUserAndListeningForChanging(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        user -> {
                            if (user == null) {
                                return;
                            }
                            mUser = user;
                            mEmail.setValue(user.getEMail());
                            mNickName.setValue(user.getNickName());
                            mGender.setValue(user.getGender());
                            if (user.getProfile() != null) {
                                getProfileFromUri(Uri.parse(user.getProfile()));
                            }
                            mIsSigned.setValue(true);
                        },
                        error -> mListener.onError(getStringFromResource(R.string.snack_database_exception)));
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
        getDataManager()
                .getUserInfoFromFacebook(loginResult.getAccessToken(),
                        user -> {
                            user.setProfile(uri.toString());
                            signUpWithCredential(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()), user);
                        },
                        error -> mListener.onError(StringUtil.getStringFromResource(R.string.snack_not_found_info)));
    }

    private void signUpWithCredential(AuthCredential authCredential, User user) {
        getDataManager()
                .signUpAndSetUser(authCredential,
                        user,
                        result -> mListener.onSuccess(SIGN_IN_SUCCESS),
                        error -> mListener.onError(getStringFromResource(R.string.snack_fb_sign_up_failed)));
    }

    public void signUpWithEmail(View v) {
        if (mPassword.getValue() == null || mPassword.getValue().length() < 6) {
            mListener.onError(getStringFromResource(R.string.snack_short_password));
            return;
        }

        if (mEmail.getValue() == null || mPassword.getValue() == null ||
                mNickName.getValue() == null || mGender.getValue() == null ||
                mEmail.getValue().trim().equals("") || mPassword.getValue().trim().equals("") ||
                mNickName.getValue().trim().equals("") || mGender.getValue() == null) {
            mListener.onError(getStringFromResource(R.string.snack_fill_info));
            return;
        }

        mListener.isWorking();

        User user = new User(mEmail.getValue(), mNickName.getValue(), mGender.getValue());

        getDataManager()
                .signUpAndSetUser(mPassword.getValue(),
                        user,
                        mProfileUri,
                        result -> mListener.onSuccess(SIGN_UP_SUCCESS),
                        error -> {
                            if (error instanceof FirebaseAuthUserCollisionException) {
                                mListener.onError(getStringFromResource(R.string.snack_already_exist_email));
                                return;
                            }
                            mListener.onError(getStringFromResource(R.string.snack_error_occured));
                        });
    }

    public void signInWithEmail(View v) {
        if (mEmail.getValue() == null || mPassword.getValue() == null
                || mEmail.getValue().trim().equals("") || mPassword.getValue().trim().equals("")) {
            mListener.onError(getStringFromResource(R.string.snack_fill_info));
            return;
        }

        if (mPassword.getValue() == null || mPassword.getValue().length() < 6) {
            mListener.onError(getStringFromResource(R.string.snack_short_password));
            return;
        }
        mListener.isWorking();

        getDataManager()
                .signIn(mEmail.getValue(),
                        mPassword.getValue(),
                        authResult -> {
                            mListener.onSuccess(SIGN_IN_SUCCESS);
                            mIsSigned.setValue(true);
                        },
                        error -> {
                            if (error instanceof FirebaseAuthInvalidUserException
                                    || error instanceof FirebaseAuthInvalidCredentialsException) {
                                mListener.onError(getStringFromResource(R.string.snack_invalid_user));
                            }
                        });
    }

    public void deleteUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            mListener.onError(getStringFromResource(R.string.snack_fb_not_signed_user));
            return;
        }
        mListener.isWorking();

        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getDataManager()
                .deleteUserAll(uuid, mUser,
                        deleteResult -> {
                            mListener.onSuccess(DELETE_USER_SUCCESS);
                            mIsSigned.setValue(false);
                        },
                        error -> mListener.onError(getStringFromResource(R.string.snack_error_occured)));
    }

    public void changeNickName(String changeNickName) {
        if (changeNickName.equals(mNickName.getValue())) {
            mListener.onError(getStringFromResource(R.string.snack_same_nick_name));
            return;
        }

        mUser.setNickName(changeNickName);
        getDataManager()
                .updateUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        mUser,
                        success -> mListener.onSuccess(UPDATE_NICK_NAME_SUCCESS),
                        error -> mListener.onError(getStringFromResource(R.string.snack_duplicate_nick_name)));
    }

    public void updatePassword(@NonNull String oldPassword, @NonNull String newPassword) {
        getDataManager()
                .updatePassword(oldPassword, newPassword,
                        result -> mListener.onSuccess(UPDATE_PASSWORD_SUCCESS),
                        error -> mListener.onError(getStringFromResource(R.string.snack_error_update_password)));
    }

    public void updateProfile(Uri uri) {
        mListener.isWorking();
        getDataManager().
                updateProfile(FirebaseAuth.getInstance().getCurrentUser().getUid(), uri, mUser,
                        result -> mListener.onSuccess(UPDATE_PROFILE_SUCCESS),
                        error -> mListener.onError(getStringFromResource(R.string.snack_update_profile_failed)));
    }
}
