package com.swsnack.catchhouse.view.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.DialogChangeNickNameBinding;
import com.swsnack.catchhouse.databinding.DialogChangePasswordBinding;
import com.swsnack.catchhouse.databinding.FragmentMyPageBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;

import static com.swsnack.catchhouse.constants.Constants.GALLERY;
import static com.swsnack.catchhouse.constants.Constants.SignInMethod.FACEBOOK;
import static com.swsnack.catchhouse.constants.Constants.SignInMethod.GOOGLE;

public class MyPageFragment extends BaseFragment<FragmentMyPageBinding, UserViewModel> {

    private FragmentManager mFragmentManager;

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_page;
    }

    @Override
    protected Class<UserViewModel> getViewModelClass() {
        return UserViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragmentManager = getActivity().getSupportFragmentManager();
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().setHandler(getViewModel());
        getBinding().setLifecycleOwner(getActivity());

        getViewModel().getUser();

        for (String signInMethod : FirebaseAuth.getInstance().getCurrentUser().getProviders()) {
            if (signInMethod.equals(FACEBOOK) || signInMethod.equals(GOOGLE)) {
                getBinding().tvMyPageChangePassword.setVisibility(View.GONE);
                break;
            }
        }

        getBinding().tvMyPageChangeNickName.setOnClickListener(v -> {
            DialogChangeNickNameBinding dialogBinding = DialogChangeNickNameBinding.inflate(getLayoutInflater());
            dialogBinding.setHandler(getViewModel());

            Dialog dialogChangeNickName = new Dialog(getContext());
            dialogChangeNickName.setContentView(dialogBinding.getRoot());
            dialogChangeNickName.show();

            dialogBinding.tvDialogChangeNickNameNegative.setOnClickListener(negative -> dialogChangeNickName.dismiss());
            dialogBinding.tvDialogChangeNickNamePositive.setOnClickListener(positive -> {
                getViewModel().changeNickName(dialogBinding.etDialogChangeNickName.getText().toString());
                dialogChangeNickName.dismiss();
            });
        });

        getBinding().tvMyPageChangePassword.setOnClickListener(v -> {
            DialogChangePasswordBinding dialogBinding = DialogChangePasswordBinding.inflate(getLayoutInflater());
            dialogBinding.setHandler(getViewModel());

            Dialog dialogChangePassword = new Dialog(getContext());
            dialogChangePassword.setContentView(dialogBinding.getRoot());
            dialogChangePassword.show();

            dialogBinding.tvDialogChangePasswordNegative.setOnClickListener(negative -> dialogChangePassword.dismiss());
            dialogBinding.tvDialogChangePasswordPositive.setOnClickListener(positive -> {
                if (!dialogBinding.etDialogChangePasswordNewPassword.getText().toString()
                        .equals(dialogBinding.etDialogChangePasswordNewPasswordConfirm.getText().toString())) {
                    Snackbar.make(getBinding().getRoot(), R.string.snack_wrong_password, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                getViewModel().updatePassword(dialogBinding.etDialogChangePasswordExPassword.getText().toString(),
                        dialogBinding.etDialogChangePasswordNewPassword.getText().toString());

                dialogChangePassword.dismiss();
            });
        });

        getBinding().tvMyPageChangeProfile.setOnClickListener(v -> startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), GALLERY));

        getBinding().tvMyPageDelete.setOnClickListener(v -> getViewModel().deleteUser());

        getBinding().tvMyPageSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            mFragmentManager.beginTransaction().replace(R.id.fl_bottom_nav_container, new SignInFragment(), SignInFragment.class.getName()).commit();
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                getViewModel().updateProfile(data.getData());
            }
        }
    }
}
