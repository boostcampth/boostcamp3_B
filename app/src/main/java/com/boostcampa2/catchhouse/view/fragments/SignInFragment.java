package com.boostcampa2.catchhouse.view.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcampa2.catchhouse.R;
import com.boostcampa2.catchhouse.constants.Constants;
import com.boostcampa2.catchhouse.databinding.FragmentSignInBinding;
import com.boostcampa2.catchhouse.view.BaseFragment;
import com.boostcampa2.catchhouse.viewmodel.userviewmodel.UserViewModel;
import com.facebook.CallbackManager;

import java.util.Arrays;

import static com.boostcampa2.catchhouse.constants.Constants.E_MAIL;
import static com.boostcampa2.catchhouse.constants.Constants.PUBLIC_PROFILE;

public class SignInFragment extends BaseFragment<FragmentSignInBinding, UserViewModel> {

    private CallbackManager mCallbackManager;
    private FragmentManager mFragmentManager;

    @Override
    protected int setLayout() {
        return R.layout.fragment_sign_in;
    }

    @Override
    protected Class<UserViewModel> setViewModel() {
        return UserViewModel.class;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFragmentManager = getActivity().getSupportFragmentManager();

        getBinding().setHandler(getViewModel());
        getBinding().setLifecycleOwner(getActivity());

        getBinding().tvSignInLogin.setOnClickListener(v -> {
            if (signInInfoCheck()) {
                Snackbar.make(v, R.string.snack_fill_info, Snackbar.LENGTH_SHORT);
                return;
            }
            mViewModel.signInWithEmail();
        });

        getBinding().ivSignInGoogle.setOnClickListener(__ ->
                startActivityForResult(getViewModel().gettGoogleSignInIntent(), Constants.SignInRequestCode.GOOGLE_SIGN_IN.getRequestCode()));

        getBinding().ivSignInFacebook.setOnClickListener(__ -> {
            mCallbackManager = CallbackManager.Factory.create();
            getViewModel().requestFacebookSignIn(mCallbackManager)
                    .logInWithReadPermissions(this, Arrays.asList(E_MAIL, PUBLIC_PROFILE));
        });

        getBinding().ivSignInEmail.setOnClickListener(__ ->
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.fl_home_container, new SignUpFragment())
                        .addToBackStack(SignUpFragment.class.getName())
                        .commit());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SignInRequestCode.GOOGLE_SIGN_IN.getRequestCode()) {
            if (resultCode == Activity.RESULT_OK) {
                getViewModel().signUpFirebaseWithGoogle(data);
            }
            return;
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean signInInfoCheck() {
        return getBinding().etSignInEmail.getText().toString().trim().equals("") &&
                getBinding().etSignInPassword.getText().toString().trim().equals("");
    }

}
