package com.swsnack.catchhouse.viewmodel.userviewmodel;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RadioGroup;

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
import com.swsnack.catchhouse.data.model.User;
import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.DataSource;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static com.swsnack.catchhouse.Constant.Gender.FEMALE;
import static com.swsnack.catchhouse.Constant.Gender.MALE;
import static com.swsnack.catchhouse.Constant.SuccessKey.DELETE_USER_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.SIGN_IN_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.SIGN_OUT_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.SIGN_UP_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.UPDATE_NICK_NAME_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.UPDATE_PASSWORD_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.UPDATE_PROFILE_SUCCESS;
import static com.swsnack.catchhouse.util.StringUtil.getStringFromResource;

public class UserViewModel extends ReactiveViewModel {

    private ViewModelListener mListener;
    public MutableLiveData<Boolean> mIsSigned;
    public MutableLiveData<Uri> mProfileUri;
    private MutableLiveData<String> mGender;
    private MutableLiveData<User> mUser;
    public MutableLiveData<String> mEmail;
    public MutableLiveData<String> mPassword;
    public MutableLiveData<String> mNickName;

    UserViewModel(DataSource dataManager, APIManager apiManager, ViewModelListener listener) {
        super(dataManager, apiManager);
        this.mProfileUri = new MutableLiveData<>();
        this.mUser = new MutableLiveData<>();
        this.mGender = new MutableLiveData<>();
        this.mIsSigned = new MutableLiveData<>();
        this.mEmail = new MutableLiveData<>();
        this.mPassword = new MutableLiveData<>();
        this.mNickName = new MutableLiveData<>();
        mIsSigned.setValue(false);
        this.mListener = listener;
    }

    public void getUserData() {
        mIsSigned.setValue(true);
        getDataManager()
                .getUserAndListeningForChanging(FirebaseAuth.getInstance().getCurrentUser().getUid(),

                        user -> {
                            if (user == null) {
                                return;
                            }
                            mUser.setValue(user);
                            mEmail.setValue(user.getEmail());
                            mNickName.setValue(user.getNickName());
                            mGender.setValue(user.getGender());
                            if (user.getProfile() != null) {
                                mProfileUri.setValue(Uri.parse(user.getProfile()));
                            }
                            mIsSigned.setValue(true);
                        },
                        error -> mListener.onError(getStringFromResource(R.string.snack_database_exception)));
    }

    public void onCheckedChangedListener(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_sign_up_male:
                mGender.setValue(MALE);
                break;
            case R.id.rb_sign_up_female:
                mGender.setValue(FEMALE);
                break;
        }
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
        getApiManager()
                .getUserInfoFromFacebook(loginResult.getAccessToken(),
                        user -> {
                            if (uri != null) {
                                user.setProfile(uri.toString());
                            }
                            signUpWithCredential(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()), user);
                        },
                        error -> mListener.onError(getStringFromResource(R.string.snack_not_found_info)));
    }

    private void signUpWithCredential(AuthCredential authCredential, User user) {
        getApiManager()
                .firebaseSignUp(authCredential,
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

        User newUser = new User(mEmail.getValue(), mNickName.getValue(), mGender.getValue());
        getApiManager()
                .firebaseSignUp(mEmail.getValue(),
                        mPassword.getValue(),
                        newUser,
                        mProfileUri.getValue(),
                        result -> mListener.onSuccess(SIGN_UP_SUCCESS),
                        error -> {
                            if (error instanceof FirebaseAuthUserCollisionException) {
                                mListener.onError(getStringFromResource(R.string.snack_already_exist_email));
                                return;
                            }
                            mListener.onError(getStringFromResource(R.string.snack_error_occured));
                        }
                );
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

        getApiManager()
                .firebaseSignIn(mEmail.getValue(),
                        mPassword.getValue(),
                        result -> {
                            mListener.onSuccess(SIGN_IN_SUCCESS);
                            mIsSigned.setValue(true);
                        },
                        error -> {
                            if (error instanceof FirebaseAuthInvalidUserException
                                    || error instanceof FirebaseAuthInvalidCredentialsException) {
                                mListener.onError(getStringFromResource(R.string.snack_invalid_user));
                                return;
                            }
                            mListener.onError(getStringFromResource(R.string.snack_fb_sign_in_failed));
                        });
    }

    public void signOut() {
        mListener.isWorking();
        getApiManager().firebaseSignOut();
        mIsSigned.setValue(false);
        clear();
        mListener.onSuccess(SIGN_OUT_SUCCESS);
    }

    private void clear() {
        mIsSigned.setValue(false);
        mUser.setValue(null);
        mGender.setValue("");
        mEmail.setValue("");
//        mProfileUri.setValue(null);
        mPassword.setValue("");
        mNickName.setValue("");
    }

    public void deleteUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            mListener.onError(getStringFromResource(R.string.snack_fb_not_signed_user));
            return;
        }
        mListener.isWorking();

        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getApiManager()
                .firebaseDeleteUser(uuid,
                        deleteResult -> {
                            clear();
                            mListener.onSuccess(DELETE_USER_SUCCESS);
                        },
                        error -> mListener.onError(getStringFromResource(R.string.snack_error_occured)));
    }

    public void changeNickName(String changeNickName) {
        if (changeNickName.equals(mNickName.getValue())) {
            mListener.onError(getStringFromResource(R.string.snack_same_nick_name));
            return;
        }
        User currentUser = mUser.getValue();
        currentUser.setNickName(changeNickName);
        mUser.setValue(currentUser);
        getDataManager()
                .updateUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        mUser.getValue(),
                        success -> mListener.onSuccess(UPDATE_NICK_NAME_SUCCESS),
                        error -> mListener.onError(getStringFromResource(R.string.snack_duplicate_nick_name)));
    }

    public void updatePassword(@NonNull String oldPassword, @NonNull String newPassword) {
        getApiManager()
                .updatePassword(oldPassword, newPassword,
                        result -> mListener.onSuccess(UPDATE_PASSWORD_SUCCESS),
                        error -> mListener.onError(getStringFromResource(R.string.snack_error_update_password)));
    }

    public void updateProfile(Uri uri) {
        mListener.isWorking();
        User user = mUser.getValue();
        getDataManager().
                updateProfile(FirebaseAuth.getInstance().getCurrentUser().getUid(), uri, mUser.getValue(),
                        result -> mListener.onSuccess(UPDATE_PROFILE_SUCCESS),
                        error -> mListener.onError(getStringFromResource(R.string.snack_update_profile_failed)));
    }

    public LiveData<User> getUser() {
        return mUser;
    }

    private LiveData<Uri> getProfileUri() {
        return mProfileUri;
    }
}