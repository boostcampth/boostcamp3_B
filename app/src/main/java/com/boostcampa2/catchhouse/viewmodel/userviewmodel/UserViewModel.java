package com.boostcampa2.catchhouse.viewmodel.userviewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.boostcampa2.catchhouse.R;
import com.boostcampa2.catchhouse.data.userdata.UserRepository;
import com.boostcampa2.catchhouse.data.userdata.pojo.User;
import com.boostcampa2.catchhouse.viewmodel.ReactiveViewModel;
import com.boostcampa2.catchhouse.viewmodel.ViewModelListener;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.boostcampa2.catchhouse.constants.Constants.SIGN_IN_SUCCESS;
import static com.boostcampa2.catchhouse.constants.Constants.SIGN_UP_SUCCESS;

public class UserViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private UserRepository mRepository;
    private ViewModelListener mListener;
    public MutableLiveData<String> mEmail;
    public MutableLiveData<String> mPassword;
    public MutableLiveData<String> mNickName;
    private MutableLiveData<String> mGender;
    public MutableLiveData<Bitmap> mProfile;

    UserViewModel(Application application, UserRepository repository, ViewModelListener listener) {
        super();
        this.mAppContext = application;
        this.mRepository = repository;
        this.mListener = listener;
        this.mEmail = new MutableLiveData<>();
        this.mPassword = new MutableLiveData<>();
        this.mNickName = new MutableLiveData<>();
        this.mGender = new MutableLiveData<>();
        this.mProfile = new MutableLiveData<>();
    }

    public void getBitmapFromData(Uri uri) {
        mListener.isWorking();
        getCompositeDisposable().add(
                Single.create(subscriber -> {
                    Bitmap bitmap = Glide.with(mAppContext)
                            .asBitmap()
                            .load(uri)
                            .submit()
                            .get();
                    subscriber.onSuccess(bitmap);
                })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bitmap -> {
                            mProfile.setValue((Bitmap) bitmap);
                            mListener.isFinished();
                        }, error -> mListener.onError(error)));
    }

    private byte[] getProfileByteArray() {
        if (mProfile.getValue() == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mProfile.getValue().compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public Intent getGoogleSignInIntent() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mAppContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(mAppContext, gso).getSignInIntent();
    }

    public void authWithGoogle(Intent data) {
        mListener.isWorking();
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount account = task.getResult();
        User user = new User(account.getDisplayName());
        if (account.getPhotoUrl() != null) {
            user.setProfile(account.getPhotoUrl().toString());
        }
        authWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null), user);
    }

    public LoginManager requestFacebookSignIn(CallbackManager callbackManager) {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mListener.isWorking();
                authWithFacebook(loginResult, Profile.getCurrentProfile().getProfilePictureUri(300, 300));
            }

            @Override
            public void onCancel() {
                mListener.isFinished();
            }

            @Override
            public void onError(FacebookException error) {
                mListener.isFinished();
                mListener.onError(error);
            }
        });
        return loginManager;
    }

    private void authWithFacebook(LoginResult loginResult, Uri uri) {
        mListener.isWorking();
        getCompositeDisposable()
                .add(mRepository.getDetailInfoFromRemote(loginResult.getAccessToken())
                        .subscribeOn(Schedulers.io())
                        .subscribe(user -> {
                            user.setProfile(uri.toString());
                            authWithCredential(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()), user);
                        }));
    }

    private void authWithCredential(AuthCredential credential, User user) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential)
                .addOnSuccessListener(success -> {
                    String uuid = auth.getCurrentUser().getUid();
                    getCompositeDisposable()
                            .add(mRepository.setUserToRemote(uuid, user)
                                    .subscribe(() -> mListener.onSuccess(SIGN_IN_SUCCESS),
                                            error -> {
                                                mListener.onError(error);
                                                FirebaseAuth.getInstance().getCurrentUser().delete();
                                            }));
                })
                .addOnFailureListener(error -> mListener.onError(error));
    }

    private void setUserToRemote(String uid, User user) {
        getCompositeDisposable().add(mRepository.setUserToRemote(uid, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mListener.onSuccess(SIGN_UP_SUCCESS),
                        error -> {
                            FirebaseAuth.getInstance().getCurrentUser().delete();
                            FirebaseStorage.getInstance().getReference().child(uid).delete();
                            mListener.onError(error);
                        }));
    }

    private void setProfileAndWriteToRemote(String uid, User user) {
        getCompositeDisposable().add(
                mRepository.saveProfileAndGetUrl(uid, getProfileByteArray())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(path -> {
                                    user.setProfile(path.toString());
                                    setUserToRemote(uid, user);
                                },
                                error -> {
                                    mListener.onError(error);
                                    FirebaseAuth.getInstance().getCurrentUser().delete();
                                }));
    }

    public void signUpWithEmail() {
        mListener.isWorking();
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(mEmail.getValue(), mPassword.getValue())
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    User user = new User(mEmail.getValue(), mNickName.getValue(), mGender.getValue());
                    if (mProfile.getValue() != null) {
                        //Firebase 인증 성공시, profile image가 있는지 확인,
                        //확인 후, storage 저장, 그 후 storage의 경로와 함께 유저 정보 db 저장
                        setProfileAndWriteToRemote(uid, user);
                    } else {
                        //profile image 가 없다면, db에 바로 유저 정보 저장
                        setUserToRemote(uid, user);
                    }
                }).addOnFailureListener(error -> mListener.onError(error));
    }

    public void signInWithEmail() {
        mListener.isWorking();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail.getValue(), mPassword.getValue())
                .addOnSuccessListener(authResult -> mListener.onSuccess(SIGN_IN_SUCCESS))
                .addOnFailureListener(error -> mListener.onError(error));
    }

    public void setGender(String gender) {
        mGender.setValue(gender);
    }

    public LiveData<String> getGender() {
        return mGender;
    }
}
