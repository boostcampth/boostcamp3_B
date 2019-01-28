package com.boostcampa2.catchhouse.viewmodel.userviewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.boostcampa2.catchhouse.R;
import com.boostcampa2.catchhouse.data.userdata.UserRepository;
import com.boostcampa2.catchhouse.data.userdata.pojo.User;
import com.boostcampa2.catchhouse.viewmodel.ReactiveViewModel;
import com.boostcampa2.catchhouse.viewmodel.ViewModelListener;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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
    private MutableLiveData<FirebaseUser> mFirebaseUser;
    private User mUser;
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
        this.mFirebaseUser = new MutableLiveData<>();
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

    private void setUserToRemote(String uid) {
        getCompositeDisposable().add(mRepository.setUserToRemote(uid, mUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mListener.onSuccess(SIGN_UP_SUCCESS),
                        error -> mListener.onError(error)));
    }

    private void setProfileImageToRemote(String uid) {
        getCompositeDisposable().add(
                mRepository.saveProfileAndGetUrl(uid, getProfileByteArray())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(path -> {
                                    mUser.setProfile(path.toString());
                                    setUserToRemote(uid);
                                },
                                error -> mListener.onError(error)));
    }

    public Intent getGoogleSignInIntent() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mAppContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(mAppContext, gso).getSignInIntent();
    }

    public void signUpFirebaseWithGoogle(Intent data) {
        mListener.isWorking();
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount account = task.getResult();
        mUser = new User(account.getDisplayName());
        if (account.getPhotoUrl() != null) {
            mUser.setProfile(account.getPhotoUrl().toString());
        }
        handlingSignUpData(GoogleAuthProvider.getCredential(account.getIdToken(), null));
    }

    public LoginManager requestFacebookSignIn(CallbackManager callbackManager) {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mListener.isWorking();
                getDetailDataFromFaceBook(loginResult);
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

    private void getDetailDataFromFaceBook(LoginResult loginResult) {
        mListener.isWorking();
        getCompositeDisposable().add(mRepository.getDetailInfoFromRemote(loginResult.getAccessToken())
                .subscribeOn(Schedulers.io())
                .subscribe(info -> mUser = info));
        handlingSignUpData(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()));
    }

    public void signUpWithEmail() {
        mListener.isWorking();
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(mEmail.getValue(), mPassword.getValue())
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    mUser = new User(mEmail.getValue(), mNickName.getValue(), mGender.getValue());
                    if (mProfile.getValue() != null) {
                        //Firebase 인증 성공시, profile image가 있는지 확인,
                        //확인 후, storage 저장, 그 후 storage의 경로와 함께 유저 정보 db 저장
                        setProfileImageToRemote(uid);
                    } else {
                        //profile image 가 없다면, db에 바로 유저 정보 저장
                        setUserToRemote(uid);
                    }
                }).addOnFailureListener(error -> mListener.onError(error));
    }

    public void signInWithEmail() {
        mListener.isWorking();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail.getValue(), mPassword.getValue())
                .addOnSuccessListener(authResult -> mListener.onSuccess(SIGN_IN_SUCCESS))
                .addOnFailureListener(error -> mListener.onError(error));
    }

    private void handlingSignUpData(AuthCredential credential) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential).addOnSuccessListener(success -> {
            String uuid = auth.getCurrentUser().getUid();
            getCompositeDisposable().add(mRepository.setUserToRemote(uuid, mUser)
                    .subscribe(() -> {
                        mListener.onSuccess(SIGN_IN_SUCCESS);
                        mFirebaseUser.setValue(auth.getCurrentUser());
                    }, error -> mListener.onError(error)));
        })
                .addOnFailureListener(error -> mListener.onError(error));
    }

    public LiveData<FirebaseUser> getUserInfo() {
        return mFirebaseUser;
    }

    public void setGender(String gender) {
        mGender.setValue(gender);
    }

    public LiveData<String> getGender() {
        return mGender;
    }
}
