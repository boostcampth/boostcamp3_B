package com.boostcampa2.catchhouse.view.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcampa2.catchhouse.R;
import com.boostcampa2.catchhouse.databinding.FragmentSignUpBinding;
import com.boostcampa2.catchhouse.view.BaseFragment;
import com.boostcampa2.catchhouse.viewmodel.userviewmodel.UserViewModel;

import static android.app.Activity.RESULT_OK;
import static com.boostcampa2.catchhouse.constants.Constants.FEMALE;
import static com.boostcampa2.catchhouse.constants.Constants.GALLERY;
import static com.boostcampa2.catchhouse.constants.Constants.MALE;

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
        getBinding().setLifecycleOwner(getActivity());

        getBinding().ivSignUpProfile.setOnClickListener(__ -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY);
        });

        getBinding().rbSignUpMale.setOnCheckedChangeListener((__, isChecked) -> {
            if (isChecked) {
                mViewModel.setGender(MALE);
            }
        });

        getBinding().rbSignUpFemale.setOnCheckedChangeListener((__, isChecked) -> {
            if (isChecked) {
                mViewModel.setGender(FEMALE);
            }
        });

        getBinding().tvSignUp.setOnClickListener(v -> {
            if (signUpInfoCheck()) {
                return;
            }
            getViewModel().signUpWithEmail();
        });
    }

    private boolean signUpInfoCheck() {
        if (getBinding().etSignUpPassword.getText().toString().length() < 6) {
            Snackbar.make(getBinding().getRoot(), getString(R.string.snack_wrong_password_length), Snackbar.LENGTH_SHORT).show();
            return true;
        }
        if (getBinding().etSignUpEmail.getText().toString().trim().equals("")
                && getBinding().etSignUpPassword.getText().toString().trim().equals("")
                && getBinding().etSignUpNickName.getText().toString().trim().equals("")) {
            Snackbar.make(getBinding().getRoot(), getString(R.string.snack_fill_info), Snackbar.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY) {
            if (resultCode == RESULT_OK) {
                getViewModel().getBitmapFromData(data.getData());
                return;
            }
            Snackbar.make(getBinding().getRoot(), R.string.snack_failed_load_image, Snackbar.LENGTH_SHORT);
        }
    }
}
