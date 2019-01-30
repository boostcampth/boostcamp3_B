package com.swsnack.catchhouse.view.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentSignUpBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;
import com.swsnack.catchhouse.constants.Constants;

import static android.app.Activity.RESULT_OK;

public class SignUpFragment extends BaseFragment<FragmentSignUpBinding, UserViewModel> {

    @Override
    protected int setLayout() {
        return R.layout.fragment_sign_up;
    }

    @Override
    protected Class<UserViewModel> setViewModel() {
        return UserViewModel.class;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getBinding().tbSignIn.setNavigationIcon(R.drawable.action_back);
        getBinding().tbSignIn.setNavigationOnClickListener(__ -> getActivity().getSupportFragmentManager().popBackStack());
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().setHandler(getViewModel());
        getBinding().setLifecycleOwner(this);

        getBinding().ivSignUpProfile.setOnClickListener(__ -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, Constants.GALLERY);
        });

        getBinding().rbSignUpMale.setOnCheckedChangeListener((__, isChecked) -> {
            if (isChecked) {
                mViewModel.setGender("male");
            }
        });

        getBinding().rbSignUpFemale.setOnCheckedChangeListener((__, isChecked) -> {
            if (isChecked) {
                mViewModel.setGender("female");
            }
        });

        getBinding().tvSignUp.setOnClickListener(v -> {
            if (signUpInfoCheck()) {
                Snackbar.make(v, "모든 정보를 입력해 주세요", Snackbar.LENGTH_SHORT).show();
                return;
            }
            getViewModel().signUpWithEmail();
        });
    }

    private boolean signUpInfoCheck() {
        return getBinding().etSignUpEmail.getText().toString().trim().equals("") && getBinding().etSignUpPassword.getText().toString().trim().equals("") &&
                getBinding().etSignUpNickName.getText().toString().trim().equals("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GALLERY) {
            if (resultCode == RESULT_OK) {
                getViewModel().getBitmapFromData(data.getData());
                return;
            }
            Snackbar.make(getBinding().getRoot(), "이미지 로드에 실패했습니다.", Snackbar.LENGTH_SHORT);
        }
    }
}
