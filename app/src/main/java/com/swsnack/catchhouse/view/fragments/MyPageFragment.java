package com.swsnack.catchhouse.view.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class MyPageFragment extends BaseFragment<FragmentMyPageBinding, UserViewModel> {

    private FragmentManager mFragmentManager;

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_page;
    }

    @Override
    protected Class<UserViewModel> setViewModel() {
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


        getBinding().tvMyPageChangeNickName.setOnClickListener(v -> {
            DialogChangeNickNameBinding dialogBinding = DialogChangeNickNameBinding.inflate(getLayoutInflater());
            dialogBinding.setHandler(getViewModel());
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(dialogBinding.getRoot());
            dialog.show();

            dialogBinding.tvDialogChangeNickNameNegative.setOnClickListener(negative -> dialog.dismiss());
            dialogBinding.tvDialogChangeNickNamePositive.setOnClickListener(view1 -> getViewModel().changeNickName(dialogBinding.etDialogChangeNickName.getText().toString()));
        });

        getBinding().tvMyPageChangePassword.setOnClickListener(v -> {
            DialogChangePasswordBinding dialogBinding = DialogChangePasswordBinding.inflate(getLayoutInflater());
            dialogBinding.setHandler(getViewModel());
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(dialogBinding.getRoot());
            dialog.show();

            dialogBinding.tvDialogChangePasswordNegative.setOnClickListener(negative -> dialog.dismiss());
        });

        getBinding().tvMyPageDelete.setOnClickListener(v -> getViewModel().deleteUser());

        getBinding().tvMyPageSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            mFragmentManager.beginTransaction().replace(R.id.fl_bottom_nav_container, new SignInFragment(), SignInFragment.class.getName()).commit();
        });

    }
}
