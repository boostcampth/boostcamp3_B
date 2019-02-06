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
import static com.swsnack.catchhouse.constants.Constants.SignInMethod.E_MAIL;

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

        // FIXME 사용자가 여러 방법으로 인증을 완료한 상태라면 Email로그인한 정보가 0번째에 들어 있지 않을 수도 있습니다.
        // for문으로 provider list를 검삭해서 email 정보가 있는경우에 작업을 처리하도록 해주세요
        if (!FirebaseAuth.getInstance().getCurrentUser().getProviders().get(0).equals(E_MAIL)) {
            getBinding().tvMyPageChangePassword.setVisibility(View.GONE);
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
                return;
            }
            // FIXME 이미지 선택을 하려고 눌렀다가 뒤로가기로 종료했을때 아래 토스트가 발생하는데 맞지않는 상황에 메세지가 출력됩니다.
            Snackbar.make(getBinding().getRoot(), R.string.snack_failed_load_image, Snackbar.LENGTH_SHORT).show();
        }
    }
}
