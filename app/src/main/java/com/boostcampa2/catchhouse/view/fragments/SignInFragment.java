package com.boostcampa2.catchhouse.view.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcampa2.catchhouse.R;
import com.boostcampa2.catchhouse.constants.Constants;
import com.boostcampa2.catchhouse.databinding.FragmentSignInBinding;
import com.boostcampa2.catchhouse.view.BaseFragment;
import com.boostcampa2.catchhouse.viewmodel.userviewmodel.UserViewModel;

public class SignInFragment extends BaseFragment<FragmentSignInBinding, UserViewModel>{

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

        getBinding().setHandler(getViewModel());
        getBinding().setLifecycleOwner(getActivity());

        getBinding().ivSignInGoogle.setOnClickListener(__ ->
                startActivityForResult(getViewModel().getGoogleSignUpInfo().getSignInIntent(), Constants.SignInRequestCode.GOOGLE_SIGN_IN.getRequestCode()));

        getViewModel().getUserInfo().observe(this, v -> Log.d("파베", v.getEmail()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SignInRequestCode.GOOGLE_SIGN_IN.getRequestCode()) {
            getViewModel().handleSignIn(data);
        }
    }

}
