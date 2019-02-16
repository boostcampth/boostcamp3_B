package com.swsnack.catchhouse.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swsnack.catchhouse.Constant;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentSignUpBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.app.Activity.RESULT_OK;

public class SignUpFragment extends BaseFragment<FragmentSignUpBinding, UserViewModel> {

    @Override
    protected int getLayout() {
        return R.layout.fragment_sign_up;
    }

    @Override
    protected Class<UserViewModel> getViewModelClass() {
        return UserViewModel.class;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getBinding().tbSignIn.setNavigationIcon(R.drawable.action_back_white);
        getBinding().tbSignIn.setNavigationOnClickListener(__ -> getActivity().getSupportFragmentManager().popBackStack());
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().setHandler(getViewModel());
        getBinding().ivSignUpProfile.setOnClickListener(__ -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, Constant.GALLERY);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.GALLERY) {
            if (resultCode == RESULT_OK) {
                getViewModel().getProfileFromUri(data.getData());
            }
        }
    }
}
