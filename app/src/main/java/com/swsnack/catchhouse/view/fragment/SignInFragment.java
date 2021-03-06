package com.swsnack.catchhouse.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentSignInBinding;
import com.swsnack.catchhouse.util.KeyboardUtil;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import static com.swsnack.catchhouse.Constant.FacebookData.E_MAIL;
import static com.swsnack.catchhouse.Constant.FacebookData.PROFILE;
import static com.swsnack.catchhouse.Constant.RequestCode.GOOGLE_SIGN_IN;

public class SignInFragment extends BaseFragment<FragmentSignInBinding, UserViewModel> {

    private CallbackManager mCallbackManager;
    private FragmentManager mFragmentManager;

    @Override
    protected int getLayout() {
        return R.layout.fragment_sign_in;
    }

    @Override
    protected Class<UserViewModel> getViewModelClass() {
        return UserViewModel.class;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        init();
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFragmentManager = getActivity().getSupportFragmentManager();

        getBinding().setHandler(getViewModel());
        getBinding().btnSignInGoogle.setOnClickListener(__ -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            Intent intent = GoogleSignIn.getClient(getContext(), gso).getSignInIntent();
            startActivityForResult(intent, GOOGLE_SIGN_IN);
        });

        getBinding().btnSignInFacebook.setOnClickListener(__ ->
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(E_MAIL, PROFILE)));

        getBinding().tvSignInSignUp.setOnClickListener(__ ->
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.fl_sign_container, new SignUpFragment())
                        .addToBackStack(SignUpFragment.class.getName())
                        .commit());

        getBinding().tvSignInLogin.setOnClickListener(v -> {
            mViewModel.signInWithEmail(v);
            KeyboardUtil.keyBoardClose(getActivity());
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                getViewModel().signInWithGoogle(data);
            }
            return;
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        LoginManager loginManager = LoginManager.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null) {
                    getViewModel().signInWithFacebook(loginResult, null);
                    return;
                }
                getViewModel().signInWithFacebook(loginResult, Profile.getCurrentProfile().getProfilePictureUri(300, 300));
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Snackbar.make(getBinding().getRoot(), R.string.snack_try_again, Snackbar.LENGTH_SHORT).show();
            }
        });
        getBinding().tbSignIn.setNavigationIcon(R.drawable.back_button_primary);
        getBinding().tbSignIn.setNavigationOnClickListener(__ -> getActivity().finish());
    }
}
